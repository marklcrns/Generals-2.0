package com.markl.game.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.markl.game.ui.Application;
import com.markl.game.util.Constants;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/04/2020.
 */
public class LoadingScreen implements Screen {

  private final Application app;

  private ShapeRenderer shapeRend;
  private float progress;

  public LoadingScreen(final Application app) {
    this.app = app;
    this.shapeRend = new ShapeRenderer();
  }

  private void queueAssets() {
    app.assets.load(Constants.UI_SKIN_JSON_PATH, Skin.class);
    app.assets.load(Constants.PIECE_ATLAS_PATH, TextureAtlas.class);
    app.assets.finishLoading();

  }

  private void getAssets() {
    // TODO: Replace. For testing ONLY
    app.uiskin = app.assets.get(Constants.UI_SKIN_JSON_PATH, Skin.class);
  }

  @Override
  public void show() {
    System.out.println("LoadingScreen Show");
    shapeRend.setProjectionMatrix(app.camera.combined);
    this.progress = 0f;
    queueAssets();
    getAssets();
  }

  public void update(float delta) {
    // Linear interpolation a + (b - 1) * lerp
    progress = MathUtils.lerp(progress, app.assets.getProgress(), .1f);
    if (app.assets.update() && progress >= app.assets.getProgress() - 0.001f) {
      app.setScreen(app.mainMenuScreen);
    }
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen

    update(delta);

    shapeRend.begin(ShapeType.Filled);
    shapeRend.setColor(Color.BLACK);
    shapeRend.rect(32, app.camera.viewportHeight / 2 - 8, app.camera.viewportWidth - 64, 16);

    shapeRend.setColor(Color.BLUE);
    shapeRend.rect(32, app.camera.viewportHeight / 2 - 8, progress * (app.camera.viewportWidth - 64), 16);
    shapeRend.end();
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
