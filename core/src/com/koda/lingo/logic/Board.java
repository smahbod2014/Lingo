package com.koda.lingo.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.koda.lingo.Lingo;
import com.koda.lingo.internal.Resources;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    public static final int WORD_LENGTH = 5;
    public static final float PADDING = Lingo.TILE_SIZE / 10f;
    public static final float TILE_PAD_SIZE = Lingo.TILE_SIZE + PADDING * 2;

    public enum BoardState { BOARD_VICTORY, BOARD_INCORRECT, BOARD_INVALID_GUESS, BOARD_DEFEAT }

    private ArrayList<Tile> tiles;
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
        currentWord = "Kodas"; //temp!
    }

    public void setTargetWord(String word) {
        currentWord = word;
    }

    private Tile anyCorrectInColumn(int col) {
        for (int i = 0; i < currentRow; i++) {
            Tile t = getTile(i, col);
            if (t.getMark() == Tile.Mark.CORRECT)
                return t;
        }
        return null;
    }

    public void initializeRow() {
        for (int i = 0; i < WORD_LENGTH; i++) {
            currentColumn = i;
            Tile t = new Tile("", this);
            tiles.add(t);
            Tile test = anyCorrectInColumn(i);
            if (i == 0)
                t.setValue("" + currentWord.charAt(0));
            else if (test != null)
                t.setValue(test.getValue());
            else
                t.setValue(".");
        }

        currentColumn = 1;
        cursor.setPosition(currentRow, currentColumn);
    }

    public void addLetter(String letter) {
        if (currentColumn == WORD_LENGTH)
            return;

        Lingo.log("Adding letter " + letter);
        Tile t = getTile(currentRow, currentColumn);
        t.setValue(letter);
        currentColumn++;
        cursor.setPosition(currentRow, currentColumn);
    }

    public void removeLast() {
        if (currentColumn == 1)
            return;

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

    public BoardState submitGuess() {
        for (int i = 0; i < WORD_LENGTH; i++) {
            String letter = getTile(currentRow, i).getValue();
            if (letter.equals(".")) {
                return BoardState.BOARD_INVALID_GUESS;
            }
        }

        String submission = getWord();
        evaluateWord(submission);
        if (submission.equalsIgnoreCase(currentWord)) {
            return BoardState.BOARD_VICTORY;
        }

        advanceRow();
        if (currentRow == rows)
            return BoardState.BOARD_DEFEAT;

        initializeRow();
        return BoardState.BOARD_INCORRECT;
    }

    public void evaluateWord(String word) {
        word = word.substring(1).toLowerCase();
        String target = currentWord.substring(1).toLowerCase();

        //check for letters that are correct AND in the right spot
        int colPosition = 1;
        for (int i = 0; i < word.length(); i++) {
            char a = word.charAt(i);
            char b = target.charAt(i);
            Lingo.log("[Evaluate] Comparing '" + a + "' and '" + b + "'");
            if (a == b) {
                Tile t = getTile(currentRow, colPosition);
                t.setMark(Tile.Mark.CORRECT);
                //TODO: guard against index out of bounds for the i + 1
                word = word.substring(0, i) + word.substring(i + 1);
                target = target.substring(0, i) + target.substring(i + 1);
                i--;
            }

            colPosition++;
        }

        //check for letters that are correct but in the wrong spot
        HashMap<Character, Integer> charMap = new HashMap<Character, Integer>();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (charMap.get(c) == null) {
                charMap.put(c, 1);
            } else {
                charMap.put(c, charMap.get(c) + 1);
            }
        }

        for (int i = 1; i < WORD_LENGTH; i++) {
            Tile t = getTile(currentRow, i);
            if (t.getMark() != Tile.Mark.NONE)
                continue;

            //TODO: make Tile's value a char instead of a String
            char c = t.getValue().toLowerCase().charAt(0);
            if (charMap.get(c) == null)
                continue;

            int count = charMap.get(c);
            if (target.contains("" + c)) {
                t.setMark(Tile.Mark.WRONG);

                if (count == 1) {
                    charMap.remove(c);
                } else {
                    charMap.put(c, count - 1);
                }
            } else {
                charMap.remove(c);
            }
        }
    }

    public void render(SpriteBatch sb) {
        if (Lingo.DEBUG) {
            Lingo.debugSr.setColor(Color.BLACK);
            Lingo.debugSr.begin(ShapeRenderer.ShapeType.Line);

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
