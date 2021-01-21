package com.markl.game.ui.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.markl.game.engine.board.BoardUtils.getPieceImagePath;
import static com.markl.game.util.Constants.VIEWPORT_HEIGHT;
import static com.markl.game.util.Constants.VIEWPORT_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.markl.game.ui.Application;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/04/2020.
 */
public class SplashScreen implements Screen {

	private final Application app;

	private Stage stage;
	private Image pieceImg;

	public SplashScreen(final Application app) {
		this.app = app;
		stage = new Stage(new StretchViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, app.camera));
	}

	@Override
	public void show() {
		System.out.println("SplashScreen Show");
		Gdx.input.setInputProcessor(stage);

		Texture pieceTex = app.assets.get(getPieceImagePath("white", "GeneralFive"), Texture.class);
		pieceImg = new Image(pieceTex);
		pieceImg.setWidth(64f);
		pieceImg.setHeight(64f);
		pieceImg.setOrigin(pieceImg.getWidth() / 2, pieceImg.getHeight() / 2);
		pieceImg.setPosition(stage.getWidth() / 2 - 32, stage.getHeight() / 2 + 32);
		pieceImg.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
					parallel(fadeIn(2f, Interpolation.pow2),
						scaleTo(2f, 2f, 2.5f, Interpolation.pow5),
						moveTo(stage.getWidth() / 2 - 32, stage.getHeight() / 2 - 32, 2f, Interpolation.swing)),
					delay(1.5f), fadeOut(1.25f)));

		stage.addActor(pieceImg);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen

		update(delta);
		stage.draw();
	}

	public void update(float delta) {
		stage.act(delta);

		if (pieceImg.getActions().size == 0)
			app.setScreen(app.mainMenuScreen);
	}

	@Override
	public void resize(int width, int height) {
		System.out.println("GameScreen Resize");
		stage.getViewport().update(width, height, false);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
