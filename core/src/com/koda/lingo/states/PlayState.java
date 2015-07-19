package com.koda.lingo.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.koda.lingo.internal.GameState;

import java.util.ArrayList;

public class PlayState extends GameState {

    ArrayList<Texture> tiles = new ArrayList<Texture>();

    public PlayState() {
        for (int i = 0; i < 5; i++) {
            Texture tex = new Texture("tiles_48/Tile_48_" + (char) ('A' + i) + ".png");
            tiles.add(tex);
        }
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        float size = tiles.get(0).getWidth();
        float padding = size / 10f;

        sb.begin();
        float position = 8f;
        for (int i = 0; i < 5; i++) {
            sb.draw(tiles.get(i), position, 400, size, size);
            position += size + padding;
        }
        sb.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
