package com.koda.lingo.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.koda.lingo.Lingo;

import java.util.ArrayList;

public class Board {

    public static final int WORD_LENGTH = 5;
    public static final float PADDING = Lingo.TILE_SIZE / 10f;
    public static final float TILE_PAD_SIZE = Lingo.TILE_SIZE + PADDING * 2;

    private ArrayList<Tile> tiles;
    private int rows;
    private int currentRow;
    private int currentColumn;
    private float renderX;
    private float renderY;

    public Board(int rows, float x, float y) {
        this.rows = rows;
        tiles = new ArrayList<Tile>();
        renderX = x;
        renderY = y;
        currentRow = 0;
    }

    public void initializeRow(String letter) {
        for (int i = 0; i < WORD_LENGTH; i++) {
            currentColumn = i;
            Tile t = new Tile("", this);
            tiles.add(t);
            if (i == 0)
                t.setValue(letter);
            else
                t.setValue(".");
        }

        currentColumn = 1;
    }

    public void addLetter(String letter) {
        Lingo.log("Adding letter " + letter);
        Tile t = getTile(currentRow, currentColumn);
        t.setValue(letter);
        currentColumn++;
    }

    public void removeLast() {
        currentColumn--;
        Tile t = getTile(currentRow, currentColumn);
        t.setValue(".");
    }

    public void advanceRow() {
        currentRow++;
        currentColumn = 0;
    }

    public String getWord() {
        String result = "";
        int offset = currentRow * WORD_LENGTH;
        for (int i = offset; i < offset + WORD_LENGTH; i++) {
            result += tiles.get(i).getValue();
        }

        return result;
    }

    public void render(SpriteBatch sb) {
        if (Lingo.DEBUG) {
            Lingo.debugSr.setColor(Color.BLACK);
            Lingo.debugSr.begin(ShapeRenderer.ShapeType.Line);
            //Lingo.debugSr.rect(renderX, renderY, WORD_LENGTH * TILE_PAD_SIZE + PADDING, rows * TILE_PAD_SIZE);


            for (int i = 0; i < 6; i++) {
                float x = renderX + TILE_PAD_SIZE * i;
                Lingo.debugSr.line(x, renderY, x, getHeight() + renderY);
            }

            for (int i = 0; i < 6; i++) {
                float y = i * TILE_PAD_SIZE + renderY;
                Lingo.debugSr.line(renderX, y, WORD_LENGTH * TILE_PAD_SIZE + renderX, y);
            }

            Lingo.debugSr.end();
        }

        for (Tile t : tiles) {
            t.render(sb);
        }
    }

    public float getX() {
        return renderX;
    }

    public float getY() {
        return renderY;
    }

    public int getRow() {
        return currentRow;
    }

    public int getColumn() {
        return currentColumn;
    }

    public float getHeight() {
        return rows * TILE_PAD_SIZE;
    }

    public Tile getTile(int row, int col) {
        int index = row * WORD_LENGTH + col;
        return tiles.get(index);
    }
}
