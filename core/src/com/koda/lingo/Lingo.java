package com.koda.lingo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.koda.lingo.internal.GameState;
import com.koda.lingo.internal.StateManager;
import com.koda.lingo.states.HighScoreState;
import com.koda.lingo.states.MenuState;
import com.koda.lingo.states.PlayState;

import java.util.ArrayList;

import javax.swing.plaf.nimbus.State;

public class Lingo extends ApplicationAdapter {

    public static final int SCREEN_WIDTH = 337;
    public static final int SCREEN_HEIGHT = 600;
    public static final int TILE_SIZE = 48;
    public static final boolean DEBUG = true;

	SpriteBatch batch;
    public static ShapeRenderer debugSr;
    static OrthographicCamera camera;
    static BitmapFont font;
    static Texture blankTile;

	@Override
	public void create() {
		batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("calibri.ttf"));
        font = generator.generateFont(46);
        generator.dispose();

        StateManager.addState(StateManager.MENU_STATE, new MenuState());
        StateManager.addState(StateManager.PLAY_STATE, new PlayState());
        StateManager.addState(StateManager.HIGH_SCORE_STATE, new HighScoreState());
        StateManager.setState(StateManager.PLAY_STATE);

        debugSr = new ShapeRenderer();
        debugSr.setProjectionMatrix(camera.combined);

        blankTile = new Texture("tiles_" + TILE_SIZE + "/BlankTile_" + TILE_SIZE + ".png");
    }

	@Override
	public void render() {
		Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        StateManager.update(Gdx.graphics.getDeltaTime());
        StateManager.render(batch);
    }

    @Override
    public void dispose() {
        StateManager.disposeAll();
        batch.dispose();
        font.dispose();
        debugSr.dispose();
    }

    public static void log(String message) {
        Gdx.app.log("Lingo", message);
    }

    public static void unproject(Vector3 coords) {
        camera.unproject(coords);
    }

    public static Vector2 getTouchCoords() {
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
        Lingo.unproject(mouse);
        return new Vector2(mouse.x, mouse.y);
    }

    public static BitmapFont getFont() {
        return font;
    }

    public static Texture getBlankTile() {
        return blankTile;
    }
}
