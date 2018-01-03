package com.koda.lingo.logic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.koda.lingo.internal.Resources;

public class Cursor {

    private Board board;
    private float alpha;
    private float renderX;
    private float renderY;
    private int row;
    private int col;

    public Cursor(Board board) {
        this.board = board;
        alpha = 1f;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col) {
        //if (col == Board.wordLength)
            //col--;

        renderX = board.getTileX(col);
        renderY = board.getTileY(row);

        this.row = row;
        this.col = col;
    }

    public void advance() {
        //if (col < Board.wordLength - 1)
            col++;

        setPosition(row, col);
    }

    public void backspace() {
        if (col > 0)
            col--;

        setPosition(row, col);
    }

    public void render(SpriteBatch sb) {
        if (col >= board.getWordLength())
            return;

        sb.begin();
        sb.draw(Resources.getTexture("cursor"), renderX, renderY, board.tilePadSize, board.tilePadSize);
        sb.end();
    }
}
