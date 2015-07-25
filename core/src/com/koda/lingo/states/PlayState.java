package com.koda.lingo.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.koda.lingo.Lingo;
import com.koda.lingo.internal.GameState;
import com.koda.lingo.internal.MyInputProcessor;
import com.koda.lingo.logic.Board;

public class PlayState extends GameState {

    Board board;

    public PlayState() {
        board = new Board(5, Board.PADDING, 235);
        board.setTargetWord("Class");
        board.initializeRow();

        Gdx.input.setInputProcessor(new MyInputProcessor(this));
        Gdx.input.setOnscreenKeyboardVisible(true);
    }

    public void typeKey(int key) {
        if (key >= Input.Keys.A && key <= Input.Keys.Z && board.getColumn() < Board.WORD_LENGTH)
            board.addLetter("" + (char) (key + 36));
        else if (key == Input.Keys.BACKSPACE && board.getColumn() > 0)
            board.removeLast();
        else if (key == Input.Keys.ENTER) {
            Board.BoardState boardState = board.submitGuess();
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            System.out.println("SPACE");
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        board.render(sb);
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
