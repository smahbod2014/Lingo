package com.koda.lingo.logic;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.koda.lingo.Lingo;
import com.koda.lingo.internal.Resources;

public class Tile {

    public enum Mark { NONE, CORRECT, WRONG, BONUS, INVALID }

    private static GlyphLayout layout = new GlyphLayout();

    private Board board;
    private String value;
    private float renderX;
    private float renderY;
    private float width;
    private float height;
    private Mark mark;
    private int row;
    private int col;

    public Tile(String value, Board board) {
        this.value = value;
        this.board = board;
        renderX = board.getX() + board.getColumn() * board.tilePadSize + board.padding;
        renderY = board.getY() + board.getHeight() - board.tilePadSize * (board.getRow() + 1) + board.padding;

        layout.setText(Lingo.getLetterFont(), value);
        width = layout.width;
        height = layout.height;
        mark = Mark.NONE;

        row = board.getRow();
        col = board.getColumn();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        layout.setText(Lingo.getLetterFont(), value);
        width = layout.width;
        height = layout.height;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark type) {
        mark = type;
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        float x = renderX;
        float y = renderY;
        sb.draw(Resources.getTexture("blank"), x, y, board.getTileSize(), board.getTileSize());
        Lingo.getLetterFont().getData().setScale(board.getReduction());
        Lingo.getLetterFont().draw(sb, value, x + board.getTileSize() / 2 - width / 2, y + board.getTileSize() / 2 + height / 2);

        switch (mark) {
            case NONE: break;
            case CORRECT:
                sb.draw(Resources.getTexture("correct"), board.getTileX(col), board.getTileY(row), board.tilePadSize, board.tilePadSize);
                break;
            case WRONG:
                sb.draw(Resources.getTexture("wrong"), board.getTileX(col), board.getTileY(row), board.tilePadSize, board.tilePadSize);
                break;
            case BONUS:
                sb.draw(Resources.getTexture("bonus"), board.getTileX(col), board.getTileY(row), board.tilePadSize, board.tilePadSize);
                break;
            case INVALID:
                sb.draw(Resources.getTexture("invalid"), board.getTileX(col), board.getTileY(row), board.tilePadSize, board.tilePadSize);
                break;
        }

        sb.end();
    }
}
