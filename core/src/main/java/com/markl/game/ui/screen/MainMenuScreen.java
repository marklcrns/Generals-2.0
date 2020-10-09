package com.markl.game.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.markl.game.ui.Application;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/03/2020.
 */
public class MainMenuScreen implements Screen {

  final Application app;

  public MainMenuScreen(final Application app) {
    this.app = app;
  }

  @Override
  public void show() {
    // TODO
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen

    this.app.camera.update();
    this.app.batch.setProjectionMatrix(this.app.camera.combined);

    this.app.batch.begin();
    this.app.font.draw(app.batch, "Welcome to Game of Generals!!!", 100, 150);
    this.app.font.draw(app.batch, "Tap anywhere to begin!", 100, 100);
    this.app.batch.end();

    if (Gdx.input.isTouched()) {
      this.app.setScreen(new GameScreen(app));
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

