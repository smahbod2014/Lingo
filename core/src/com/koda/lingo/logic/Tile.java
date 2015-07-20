package com.koda.lingo.logic;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.koda.lingo.Lingo;

public class Tile {

    private static GlyphLayout layout = new GlyphLayout();

    private String value;
    private float renderX;
    private float renderY;
    private float width;
    private float height;

    public Tile(String value, Board board) {
        this.value = value;
        renderX = board.getX() + board.getColumn() * Board.TILE_PAD_SIZE + Board.PADDING;
        renderY = board.getY() + board.getHeight() - Board.TILE_PAD_SIZE * (board.getRow() + 1) + Board.PADDING;

        layout.setText(Lingo.getFont(), value);
        width = layout.width;
        height = layout.height;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        layout.setText(Lingo.getFont(), value);
        width = layout.width;
        height = layout.height;
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        float x = renderX;
        float y = renderY;
        sb.draw(Lingo.getBlankTile(), x, y);
        Lingo.getFont().draw(sb, value, x + Lingo.TILE_SIZE / 2 - width / 2, y + Lingo.TILE_SIZE / 2 + height / 2);
        sb.end();
    }
}
