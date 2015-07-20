package com.koda.lingo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.koda.lingo.Lingo;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Lingo.SCREEN_WIDTH;
        config.height = Lingo.SCREEN_HEIGHT;
        config.title = "Lingo";
        config.resizable = false;
        //this is a fake comment
		new LwjglApplication(new Lingo(), config);
	}
}
