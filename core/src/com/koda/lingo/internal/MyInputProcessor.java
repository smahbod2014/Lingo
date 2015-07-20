package com.koda.lingo.internal;

import com.badlogic.gdx.InputAdapter;
import com.koda.lingo.states.PlayState;

public class MyInputProcessor extends InputAdapter {

    PlayState playState;

    public MyInputProcessor(PlayState playState) {
        this.playState = playState;
    }

    @Override
    public boolean keyDown(int keycode) {
        playState.typeKey(keycode);
        return true;
    }
}
