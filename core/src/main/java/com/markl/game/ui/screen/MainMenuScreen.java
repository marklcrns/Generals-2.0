package com.markl.game.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.markl.game.ui.GoG;

import static com.markl.game.engine.board.BoardUtils.getPieceImagePath;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/03/2020.
 */
public class MainMenuScreen implements Screen {

  final GoG game;

  public MainMenuScreen(final GoG game) {
    this.game = game;
  }

  public void loadPieceImages() {
    game.assets.load(getPieceImagePath("black", "GeneralFive"), Texture.class);
    game.assets.load(getPieceImagePath("black", "GeneralFour"), Texture.class);
    game.assets.load(getPieceImagePath("black", "GeneralThree"), Texture.class);
    game.assets.load(getPieceImagePath("black", "GeneralTwo"), Texture.class);
    game.assets.load(getPieceImagePath("black", "GeneralOne"), Texture.class);
    game.assets.load(getPieceImagePath("black", "Colonel"), Texture.class);
    game.assets.load(getPieceImagePath("black", "LtCol"), Texture.class);
    game.assets.load(getPieceImagePath("black", "Major"), Texture.class);
    game.assets.load(getPieceImagePath("black", "Captain"), Texture.class);
    game.assets.load(getPieceImagePath("black", "LtOne"), Texture.class);
    game.assets.load(getPieceImagePath("black", "LtTwo"), Texture.class);
    game.assets.load(getPieceImagePath("black", "Sergeant"), Texture.class);
    game.assets.load(getPieceImagePath("black", "Private"), Texture.class);
    game.assets.load(getPieceImagePath("black", "Spy"), Texture.class);
    game.assets.load(getPieceImagePath("black", "Flag"), Texture.class);
    game.assets.load(getPieceImagePath("white", "GeneralFive"), Texture.class);
    game.assets.load(getPieceImagePath("white", "GeneralFour"), Texture.class);
    game.assets.load(getPieceImagePath("white", "GeneralThree"), Texture.class);
    game.assets.load(getPieceImagePath("white", "GeneralTwo"), Texture.class);
    game.assets.load(getPieceImagePath("white", "GeneralOne"), Texture.class);
    game.assets.load(getPieceImagePath("white", "Colonel"), Texture.class);
    game.assets.load(getPieceImagePath("white", "LtCol"), Texture.class);
    game.assets.load(getPieceImagePath("white", "Major"), Texture.class);
    game.assets.load(getPieceImagePath("white", "Captain"), Texture.class);
    game.assets.load(getPieceImagePath("white", "LtOne"), Texture.class);
    game.assets.load(getPieceImagePath("white", "LtTwo"), Texture.class);
    game.assets.load(getPieceImagePath("white", "Sergeant"), Texture.class);
    game.assets.load(getPieceImagePath("white", "Private"), Texture.class);
    game.assets.load(getPieceImagePath("white", "Spy"), Texture.class);
    game.assets.load(getPieceImagePath("white", "Flag"), Texture.class);
    game.assets.finishLoading();
  }

  @Override
  public void show() {
    loadPieceImages();
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen

    this.game.camera.update();
    this.game.batch.setProjectionMatrix(this.game.camera.combined);


    this.game.batch.begin();
    this.game.font.draw(game.batch, "Welcome to Game of Generals!!!", 100, 150);
    this.game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
    this.game.batch.end();

    if (Gdx.input.isTouched()) {
      this.game.setScreen(new GameScreen(game));
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

