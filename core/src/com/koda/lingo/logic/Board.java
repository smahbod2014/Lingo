package com.koda.lingo.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.koda.lingo.Lingo;
import com.koda.lingo.internal.Resources;

import java.util.ArrayList;

public class Board {

    public static final int WORD_LENGTH = 5;
    public static final float PADDING = Lingo.TILE_SIZE / 10f;
    public static final float TILE_PAD_SIZE = Lingo.TILE_SIZE + PADDING * 2;
    public static final int BOARD_VICTORY = 0;
    public static final int BOARD_INCORRECT = 1;
    public static final int BOARD_INVALID_GUESS = 2;

    private ArrayList<Tile> tiles;
    private ArrayList<MarkerTile> markerTiles;
    private Cursor cursor;
    private int rows;
    private int currentRow;
    private int currentColumn;
    private String currentWord;
    private float renderX;
    private float renderY;

    public Board(int rows, float x, float y) {
        this.rows = rows;
        tiles = new ArrayList<Tile>();
        renderX = x;
        renderY = y;
        currentRow = 0;
        cursor = new Cursor(this);
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
        cursor.setPosition(currentRow, currentColumn);
    }

    public void addLetter(String letter) {
        Lingo.log("Adding letter " + letter);
        Tile t = getTile(currentRow, currentColumn);
        t.setValue(letter);
        currentColumn++;
        cursor.setPosition(currentRow, currentColumn);
    }

    public void removeLast() {
        currentColumn--;
        Tile t = getTile(currentRow, currentColumn);
        t.setValue(".");
        cursor.setPosition(currentRow, currentColumn);
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

    public int submitGuess() {
        for (int i = 0; i < WORD_LENGTH; i++) {
            String letter = getTile(currentRow, i).getValue();
            if (letter.equals(".")) {
                return BOARD_INVALID_GUESS;
            }
        }

        String submission = getWord();
        if (submission.equalsIgnoreCase(currentWord)) {
            return BOARD_VICTORY;
        }

        evaluateWord(submission);
        return BOARD_INCORRECT;
    }

    public void evaluateWord(String word) {
        word = word.substring(1).toLowerCase();
        String target = currentWord.substring(1).toLowerCase();

        //check for letters that are correct AND in the right spot
        for (int i = 0; i < word.length(); i++) {
            char a = word.charAt(i);
            char b = target.charAt(i);
            if (a == b) {
                markerTiles.add(new MarkerTile("correct", getTileX(i), getTileY(currentRow)));
                //TODO: guard against index out of bounds for the i + 1
                word = word.substring(0, i) + word.substring(i + 1);
                target = target.substring(0, i) + target.substring(i + 1);
                i--;
            }
        }


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

        sb.begin();
        sb.draw(Resources.getTexture("correct"), getTileX(1), getTileY(0), TILE_PAD_SIZE, TILE_PAD_SIZE);
        sb.draw(Resources.getTexture("wrong"), getTileX(3), getTileY(0), TILE_PAD_SIZE, TILE_PAD_SIZE);
        sb.end();

        cursor.render(sb);
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

    public float getTileX(int col) {
        return renderX + col * TILE_PAD_SIZE;
    }

    public float getTileY(int row) {
        return renderY + (rows - row - 1) * TILE_PAD_SIZE;
    }
}
