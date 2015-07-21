package com.koda.lingo.logic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.koda.lingo.internal.Resources;

public class Cursor {

    private Board board;
    private float alpha;
    private float renderX;
    private float renderY;

    public Cursor(Board board) {
        this.board = board;
        alpha = 1f;
    }

    public void setPosition(int row, int col) {
        renderX = board.getTileX(col);
        renderY = board.getTileY(row);
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(Resources.getTexture("cursor"), renderX, renderY, Board.TILE_PAD_SIZE, Board.TILE_PAD_SIZE);
        sb.end();
    }
}
