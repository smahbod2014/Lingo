package com.koda.lingo.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.koda.lingo.internal.Resources;

public class MarkerTile {

    private Texture texture;
    private float renderX;
    private float renderY;

    public MarkerTile(String icon, float x, float y) {
        this.texture = Resources.getTexture(icon);
        this.renderX = x;
        this.renderY = y;
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(texture, renderX, renderY, Board.TILE_PAD_SIZE, Board.TILE_PAD_SIZE);
        sb.end();
    }
}
