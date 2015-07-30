package com.koda.lingo.internal;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.koda.lingo.states.PlayState;

public class MyInputProcessor extends InputAdapter {

    PlayState playState;

    public MyInputProcessor(PlayState playState) {
        this.playState = playState;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            StateManager.setState(StateManager.MENU_STATE);
            return true;
        }

        playState.typeKey(keycode);
        return true;
    }
}
