package com.koda.lingo.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.koda.lingo.Lingo;
import com.koda.lingo.states.PlayState;

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

    public void update(float dt) {
        if (Gdx.input.justTouched()) {
            Vector2 coords = Lingo.getTouchCoords();
            coords.x -= renderX;
            coords.y -= renderY;
            int row = rows - (int) (coords.y / TILE_PAD_SIZE) - 1;
            int col = (int) (coords.x / TILE_PAD_SIZE);
            if (row == currentRow && col > 0 && col < WORD_LENGTH)
                cursor.setPosition(row, col);
        }
    }

    public void reset() {
        tiles.clear();
        currentColumn = 0;
        currentRow = 0;
    }

    public void setTargetWord(String word) {
        word = word.toUpperCase();
        currentWord = word;
        Lingo.log("[Board] Word was set to: " + word);
    }

    public String getTargetWord() {
        return currentWord;
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
        int startingColumn = 1;
        boolean startColumnSet = false;
        for (int i = 0; i < WORD_LENGTH; i++) {
            currentColumn = i;
            Tile t = new Tile("", this);
            tiles.add(t);
            Tile test = anyCorrectInColumn(i);
            if (i == 0)
                t.setValue("" + currentWord.charAt(0));
            else if (test != null)
                t.setValue(test.getValue());
            else {
                t.setValue(".");
                if (!startColumnSet) {
                    startingColumn = i;
                    startColumnSet = true;
                }
            }
        }

        currentColumn = startingColumn;
        cursor.setPosition(currentRow, currentColumn);
    }

    public void addLetter(String letter) {
        if (cursor.getCol() == WORD_LENGTH)
            return;

        Tile t = getTile(cursor.getRow(), cursor.getCol());
        t.setValue(letter);
        cursor.advance();
    }

    public void removeLast() {
        if (cursor.getCol() > 1)
            cursor.backspace();

        Tile t = getTile(cursor.getRow(), cursor.getCol());
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

    private boolean isInDictionary(String word) {
        for (int i = 0; i < PlayState.dictionary.length; i++) {
            if (PlayState.dictionary[i].equalsIgnoreCase(word))
                return true;
        }
        return false;
    }

    public void evaluateWord(String word) {
        if (!isInDictionary(word)) {
            for (int i = 0; i < WORD_LENGTH; i++) {
                getTile(currentRow, i).setMark(Tile.Mark.INVALID);
            }
            return;
        }

        word = word.substring(1).toLowerCase();
        String target = currentWord.substring(1).toLowerCase();

        //check for letters that are correct AND in the right spot
        int colPosition = 1;
        for (int i = 0; i < word.length(); i++) {
            char a = word.charAt(i);
            char b = target.charAt(i);
            if (a == b) {
                Tile t = getTile(currentRow, colPosition);
                t.setMark(Tile.Mark.CORRECT);

                word = word.substring(0, i) + word.substring(i + 1);
                target = target.substring(0, i) + target.substring(i + 1);
                i--;
            }

            colPosition++;
        }

        //check for letters that are correct but in the wrong spot
        /*gets the number of occurrences of each letter
        *ex: a: 2, s: 1...
        */
        HashMap<Character, Integer> charMap = new HashMap<Character, Integer>();
        HashMap<Character, Integer> targetCharMap = new HashMap<Character, Integer>();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (charMap.get(c) == null) {
                charMap.put(c, 1);
            } else {
                charMap.put(c, charMap.get(c) + 1);
            }

            c = target.charAt(i);
            if (targetCharMap.get(c) == null) {
                targetCharMap.put(c, 1);
            } else {
                targetCharMap.put(c, targetCharMap.get(c) + 1);
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
            if (targetCharMap.get(c) != null) {
                t.setMark(Tile.Mark.WRONG);
                int remaining = targetCharMap.get(c);
                if (remaining == 1)
                    targetCharMap.remove(c);
                else
                    targetCharMap.put(c, remaining - 1);

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
