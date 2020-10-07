package com.markl.game.ui.screen;

import static com.markl.game.engine.board.BoardUtils.getPieceImagePath;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.markl.game.ui.GoG;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/03/2020.
 */
public class MainMenuScreen implements Screen {

  final GoG gameUI;

  public MainMenuScreen(final GoG game) {
    this.gameUI = game;
  }

  @Override
  public void show() {
    // TODO
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen

    this.gameUI.camera.update();
    this.gameUI.batch.setProjectionMatrix(this.gameUI.camera.combined);


    this.gameUI.batch.begin();
    this.gameUI.font.draw(gameUI.batch, "Welcome to Game of Generals!!!", 100, 150);
    this.gameUI.font.draw(gameUI.batch, "Tap anywhere to begin!", 100, 100);
    this.gameUI.batch.end();

    if (Gdx.input.isTouched()) {
      this.gameUI.setScreen(new GameScreen(gameUI));
      dispose();
    }
  }

  @Override
  public void resize(int width, int height) {
    // TODO Auto-generated method stub

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

