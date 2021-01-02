package com.markl.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.markl.game.ui.Application;
import com.markl.game.util.Constants;

public class DesktopLauncher {
  public static void main (String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

    config.width = Constants.VIEWPORT_WIDTH;
    config.height = Constants.VIEWPORT_HEIGHT;

    new LwjglApplication(new Application(), config);

    // TODO: DELETE BEFORE RELEASING //
    TexturePacker.Settings settings = new TexturePacker.Settings();
    settings.pot = true;
    settings.fast = true;
    settings.combineSubdirectories = true;
    settings.paddingX = 1;
    settings.paddingY = 1;
    settings.edgePadding = true;
    settings.grid = true;
    // settings.maxWidth = 2048;
    // settings.maxHeight = 2048;
    // TexturePacker.process(settings, "pieces/original", "./pieces", "piecesTex");
    TexturePacker.process(settings, "pieces/50", "./pieces", "piecesTex");
  }
}
