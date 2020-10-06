package com.markl.game.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.markl.game.ui.GoG;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/04/2020.
 */
public class LoadingScreen implements Screen {

  private final GoG game;

  private ShapeRenderer shapeRend;
  private float progress;

  public LoadingScreen(final GoG game) {
    this.game = game;
    this.shapeRend = new ShapeRenderer();
  }

  private void queueAssets() {
    game.assets.load("pieces/original/white/WHITE_Flag.png", Texture.class);
  }

  @Override
  public void show() {
    System.out.println("LoadingScreen Show");
    shapeRend.setProjectionMatrix(game.camera.combined);
    this.progress = 0f;
    queueAssets();
  }

  public void update(float delta) {
    // Linear interpolation a + (b - 1) * lerp
    progress = MathUtils.lerp(progress, game.assets.getProgress(), .1f);
    if (game.assets.update() && progress >= game.assets.getProgress() - 0.001f) {
      game.setScreen(game.splashScreen);
    }
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen

    update(delta);

    shapeRend.begin(ShapeType.Filled);
    shapeRend.setColor(Color.BLACK);
    shapeRend.rect(32, game.camera.viewportHeight / 2 - 8, game.camera.viewportWidth - 64, 16);

    shapeRend.setColor(Color.BLUE);
    shapeRend.rect(32, game.camera.viewportHeight / 2 - 8, progress * (game.camera.viewportWidth - 64), 16);
    shapeRend.end();

    game.batch.begin();
    game.font.draw(game.batch, "Screen: Loading", 20, 20);
    game.batch.end();
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
    shapeRend.dispose();
  }
}
