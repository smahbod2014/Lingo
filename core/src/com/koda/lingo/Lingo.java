package com.koda.lingo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Lingo extends ApplicationAdapter {

	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("BaseTile.png");
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float size = 64f;
        float padding = size / 10f;

		batch.begin();
        float position = 32f;
        for (int i = 0; i < 5; i++) {
		    batch.draw(img, position, 400, size, size);
            position += size + padding;
        }
		batch.end();
	}
}
