package com.koda.lingo.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.koda.lingo.Lingo;
import com.koda.lingo.internal.GameState;
import com.koda.lingo.internal.MyInputProcessor;
import com.koda.lingo.internal.StateManager;
import com.koda.lingo.internal.Timer;
import com.koda.lingo.logic.Board;

public class PlayState extends GameState {

    public enum PlayMode { REGULAR, MARATHON, HARD, HARD_MARATHON;
        public boolean isMarathon() {
            return this == MARATHON || this == HARD_MARATHON;
        }

        public boolean isHardMode() {
            return this == HARD || this == HARD_MARATHON;
        }
    }

    public static final long VICTORY_DELAY = 1000;
    public static final long DEFEAT_DELAY = 3000;
    public static final int BONUS_LETTERS = 3;
    public static final int MARATHON_TIME = 180;
    public static String[] wordBank;
    public static String[] dictionary;
    {

    }

    Board board;
    boolean victoryMode = false;
    PlayMode mode;

    //marathon modes
    private int bonusLetters;
    private int time;
    private int marathonScore;

    private int numLetters;

    public PlayState(PlayMode mode, @Deprecated MenuState.Obscurity obscurity, int numLetters) {
        Lingo.log("PlayState entered. " + mode.toString() + " mode selected. Obscurity is " + obscurity.toString());

        long now = System.currentTimeMillis();
        FileHandle file = Gdx.files.internal("dictionary.txt");
        dictionary = file.readString().split("\\s+");
        file = Gdx.files.internal("wordbank-" + numLetters + ".txt");
        wordBank = file.readString().split("\\s+");
        long elapsed = System.currentTimeMillis() - now;
        Lingo.log("[PlayState] Took " + elapsed / 1000.0 + " seconds to read the word bank and dictionary");

        board = new Board(5, 5, 245, mode, numLetters);
        board.setTargetWord(wordBank[Lingo.rand(wordBank.length)]);
        this.mode = mode;
        this.numLetters = numLetters;

        if (mode.isMarathon()) {
            bonusLetters = BONUS_LETTERS;
            time = MARATHON_TIME;
            Timer.start("marathon_timer", 1000);
        }
    }

    public void typeKey(int key) {
        if (Timer.running("victory") || Timer.running("defeat"))
            return;

        if (key >= Input.Keys.A && key <= Input.Keys.Z && board.getColumn() < numLetters)
            board.addLetter("" + (char) (key + 36));
        else if (key == Input.Keys.BACKSPACE && board.getColumn() > 0)
            board.removeLast();
        else if (key == Input.Keys.ENTER) {
            Board.BoardState boardState = board.submitGuess();
            if (boardState == Board.BoardState.BOARD_VICTORY) {
                Timer.start("victory", VICTORY_DELAY);
                victoryMode = true;
                if (mode.isMarathon())
                    marathonScore++;
            } else if (boardState == Board.BoardState.BOARD_DEFEAT) {
                Timer.start("defeat", DEFEAT_DELAY);
            }
        }
        else if (mode.isMarathon() && key == Input.Keys.COMMA && bonusLetters > 0) {
            if (board.doBonusLetter())
                bonusLetters--;
        } else if (key == Input.Keys.PERIOD) {
            board.resetRow();
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new MyInputProcessor(this));
        Gdx.input.setOnscreenKeyboardVisible(true);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.justTouched()) {
            //Lingo.log("Touched at: " + Lingo.getTouchCoords());
            Gdx.input.setOnscreenKeyboardVisible(true);
        }

        board.update(dt);

        if (Timer.justFinished("victory") || Timer.justFinished("defeat")) {
            victoryMode = false;
            board.reset();
            //TODO: Add bias against words farther down the list
            board.setTargetWord(wordBank[Lingo.rand(wordBank.length)]);
            //board.initializeRow();
        }

        if (mode.isMarathon()) {
            if (Timer.justFinished("marathon_timer")) {
                Timer.start("marathon_timer", 1000);
                time--;
                if (time < 0) {
                    MenuState menuState = (MenuState) StateManager.getGameState(StateManager.MENU_STATE);
                    menuState.setEndGameStats(board.getTargetWord(), marathonScore);
                    menuState.setup(MenuState.MenuMode.GAME_OVER);
                    StateManager.setState(menuState);
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            StateManager.setState(new MenuState());
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        board.render(sb);

        if (Timer.running("defeat")) {
            sb.begin();
            Lingo.getBonusFont().setColor(Color.RED);
            Lingo.getBonusFont().draw(sb, board.getTargetWord(), 10, 590);
            Lingo.getBonusFont().setColor(Color.WHITE);
            sb.end();
        }

        if (Timer.running("victory")) {
            sb.begin();
            Lingo.getBonusFont().setColor(Color.RED);
            Lingo.getBonusFont().draw(sb, "Nice!", 10, 590);
            Lingo.getBonusFont().setColor(Color.WHITE);
            sb.end();
        }

        if (mode.isMarathon()) {
            String timeText = convertTime();
            sb.begin();
            BitmapFont f = Lingo.getTimerFont();
            if (time <= 30 && time % 2 == 0)
                f.setColor(Color.RED);
            else
                f.setColor(Color.WHITE);
            f.draw(sb, timeText, 250, 575);

            f = Lingo.getBonusFont();
            String bonusText = "Bonus\nLetters: ";
            f.draw(sb, bonusText, 125, 585);
            f.draw(sb, "Score: " + marathonScore, 10, 550);

            f = Lingo.getTimerFont();
            f.setColor(Color.WHITE);
            f.draw(sb, Integer.toString(bonusLetters), 210, 570);
            sb.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        //Lingo.log("Play state paused");
    }

    @Override
    public void resume() {
        //Lingo.log("Play state resumed");
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private String convertTime() {
        int minutes = time / 60;
        int seconds = time % 60;
        String s = "" + minutes +":";
        if (seconds < 10)
            s += "0";
        s += seconds;
        return s;
    }
}
