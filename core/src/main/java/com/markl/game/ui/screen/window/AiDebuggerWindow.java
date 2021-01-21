package com.markl.game.ui.screen.window;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.markl.game.ui.screen.GameScreen;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 01/04/2021.
 */
public class AiDebuggerWindow extends Window {

	private GameScreen gameScreen;

	public AiDebuggerWindow(String title, Skin skin, GameScreen gameScreen) {
		super(title, skin);
		this.gameScreen = gameScreen;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// gameScreen.drawBoard();
		// gameScreen.drawTileHighlights();
		super.draw(batch, parentAlpha);
	}

}
