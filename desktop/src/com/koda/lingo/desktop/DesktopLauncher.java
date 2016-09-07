package com.koda.lingo.desktop;

import com.badlogic.gdx.Files;
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
        config.addIcon("Tile_32_L.png", Files.FileType.Internal);
		new LwjglApplication(new Lingo(), config);
	}
}
