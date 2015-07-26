package com.koda.lingo.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.koda.lingo.Lingo;
import com.koda.lingo.internal.GameState;
import com.koda.lingo.internal.MyInputProcessor;
import com.koda.lingo.internal.Timer;
import com.koda.lingo.logic.Board;

import java.io.InputStream;
import java.util.ArrayList;

public class PlayState extends GameState {

    public static final long VICTORY_DELAY = 1000;
    public static final long DEFEAT_DELAY = 3000;

    Board board;
    String[] words;
    boolean victoryMode = false;

    public PlayState() {
        Gdx.input.setInputProcessor(new MyInputProcessor(this));
        Gdx.input.setOnscreenKeyboardVisible(true);

        long now = System.currentTimeMillis();
        FileHandle file = Gdx.files.internal("wordlist.txt");
        words = file.readString().replaceAll("\n", " ").split(" ");
        long elapsed = System.currentTimeMillis() - now;
        Lingo.log("[PlayState] Took " + elapsed / 1000.0 + " seconds to read the wordlist");

        board = new Board(5, Board.PADDING, 235);
        board.setTargetWord(words[Lingo.rand(words.length)]);
        board.initializeRow();
    }

    public void typeKey(int key) {
        if (Timer.running("victory") || Timer.running("defeat"))
            return;

        if (key >= Input.Keys.A && key <= Input.Keys.Z && board.getColumn() < Board.WORD_LENGTH)
            board.addLetter("" + (char) (key + 36));
        else if (key == Input.Keys.BACKSPACE && board.getColumn() > 0)
            board.removeLast();
        else if (key == Input.Keys.ENTER) {
            Board.BoardState boardState = board.submitGuess();
            if (boardState == Board.BoardState.BOARD_VICTORY) {
                Timer.start("victory", VICTORY_DELAY);
                victoryMode = true;
            } else if (boardState == Board.BoardState.BOARD_DEFEAT) {
                Timer.start("defeat", DEFEAT_DELAY);
            }
        }
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.justTouched()) {
            Lingo.log("Touched at: " + Lingo.getTouchCoords());
            Gdx.input.setOnscreenKeyboardVisible(true);
        }

        board.update(dt);

        if (Timer.justFinished("victory") || Timer.justFinished("defeat")) {
            victoryMode = false;
            board.reset();
            board.setTargetWord(words[Lingo.rand(words.length)]);
            board.initializeRow();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        board.render(sb);

        if (Timer.running("defeat")) {
            sb.begin();
            Lingo.getFont().setColor(Color.RED);
            Lingo.getFont().draw(sb, board.getTargetWord(), 10, 575);
            Lingo.getFont().setColor(Color.WHITE);
            sb.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        Lingo.log("Play state paused");
    }

    @Override
    public void resume() {
        Lingo.log("Play state resumed");
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
