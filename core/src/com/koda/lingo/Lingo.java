package com.koda.lingo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.koda.lingo.internal.GameState;
import com.koda.lingo.internal.StateManager;
import com.koda.lingo.states.HighScoreState;
import com.koda.lingo.states.MenuState;
import com.koda.lingo.states.PlayState;

import java.util.ArrayList;

import javax.swing.plaf.nimbus.State;

public class Lingo extends ApplicationAdapter {

	SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();

        StateManager.addState(StateManager.MENU_STATE, new MenuState());
        StateManager.addState(StateManager.PLAY_STATE, new PlayState());
        StateManager.addState(StateManager.HIGH_SCORE_STATE, new HighScoreState());
        StateManager.setState(StateManager.PLAY_STATE);
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
    }
}
