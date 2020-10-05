package com.markl.game.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/04/2020.
 */
public class SplashScreen implements Screen {

  private final GoG game;

  private Stage stage;
  private Image pieceImg;

  public SplashScreen(final GoG game) {
    this.game = game;
    stage = new Stage(new StretchViewport(GoG.V_WIDTH, GoG.V_HEIGHT, game.camera));
  }

  @Override
  public void show() {
    System.out.println("SplashScreen Show");
    Gdx.input.setInputProcessor(stage);

    Texture pieceTex = game.assets.get("pieces/original/white/WHITE_Flag.png", Texture.class);
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

    game.batch.begin();
    game.font.draw(game.batch, "Splashscreen", 50, 50);
    game.batch.end();
  }

  public void update(float delta) {
    stage.act(delta);
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
