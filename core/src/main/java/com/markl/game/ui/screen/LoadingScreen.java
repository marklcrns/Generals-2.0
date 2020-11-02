package com.markl.game.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.markl.game.ui.Application;

import static com.markl.game.engine.board.BoardUtils.getPieceImagePath;

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
    // Load black pieces
    app.assets.load(getPieceImagePath("black", "GeneralFive"), Texture.class);
    app.assets.load(getPieceImagePath("black", "GeneralFour"), Texture.class);
    app.assets.load(getPieceImagePath("black", "GeneralThree"), Texture.class);
    app.assets.load(getPieceImagePath("black", "GeneralTwo"), Texture.class);
    app.assets.load(getPieceImagePath("black", "GeneralOne"), Texture.class);
    app.assets.load(getPieceImagePath("black", "Colonel"), Texture.class);
    app.assets.load(getPieceImagePath("black", "LtCol"), Texture.class);
    app.assets.load(getPieceImagePath("black", "Major"), Texture.class);
    app.assets.load(getPieceImagePath("black", "Captain"), Texture.class);
    app.assets.load(getPieceImagePath("black", "LtOne"), Texture.class);
    app.assets.load(getPieceImagePath("black", "LtTwo"), Texture.class);
    app.assets.load(getPieceImagePath("black", "Sergeant"), Texture.class);
    app.assets.load(getPieceImagePath("black", "Private"), Texture.class);
    app.assets.load(getPieceImagePath("black", "Spy"), Texture.class);
    app.assets.load(getPieceImagePath("black", "Flag"), Texture.class);
    app.assets.load(getPieceImagePath("black", "Hidden"), Texture.class);

    // Load white pieces
    app.assets.load(getPieceImagePath("white", "GeneralFive"), Texture.class);
    app.assets.load(getPieceImagePath("white", "GeneralFour"), Texture.class);
    app.assets.load(getPieceImagePath("white", "GeneralThree"), Texture.class);
    app.assets.load(getPieceImagePath("white", "GeneralTwo"), Texture.class);
    app.assets.load(getPieceImagePath("white", "GeneralOne"), Texture.class);
    app.assets.load(getPieceImagePath("white", "Colonel"), Texture.class);
    app.assets.load(getPieceImagePath("white", "LtCol"), Texture.class);
    app.assets.load(getPieceImagePath("white", "Major"), Texture.class);
    app.assets.load(getPieceImagePath("white", "Captain"), Texture.class);
    app.assets.load(getPieceImagePath("white", "LtOne"), Texture.class);
    app.assets.load(getPieceImagePath("white", "LtTwo"), Texture.class);
    app.assets.load(getPieceImagePath("white", "Sergeant"), Texture.class);
    app.assets.load(getPieceImagePath("white", "Private"), Texture.class);
    app.assets.load(getPieceImagePath("white", "Spy"), Texture.class);
    app.assets.load(getPieceImagePath("white", "Flag"), Texture.class);
    app.assets.load(getPieceImagePath("white", "Hidden"), Texture.class);
    app.assets.finishLoading();
  }

  @Override
  public void show() {
    System.out.println("LoadingScreen Show");
    shapeRend.setProjectionMatrix(app.camera.combined);
    this.progress = 0f;
    queueAssets();
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
