package com.markl.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * @author Mark Lucernas
 * Date: Oct 03, 2020
 */
public class GameScreen implements Screen {

  private final GoG game;

  private Stage stage;
  private Texture pieceImg;
  private Rectangle piece;

  public GameScreen(final GoG game) {
    this.game = game;
    this.stage = new Stage(new FitViewport(GoG.V_WIDTH, GoG.V_HEIGHT, game.camera));
    Gdx.input.setInputProcessor(stage);

    // Load image as Texture
    pieceImg = new Texture(Gdx.files.internal("pieces/original/black/BLACK_Captain.png"));

    piece = new Rectangle();
    piece.x = GoG.V_WIDTH / 2 - 64 / 2; // center the piece horizontally
    piece.y = 20; // bottom left corner
    piece.width = 64;
    piece.height = 64;
  }

  @Override
  public void show() {
    // TODO Auto-generated method stub

  }

  public void update(float delta) {
    stage.act(delta);

  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen

    update(delta);
    stage.draw();
    game.camera.update();
    game.batch.setProjectionMatrix(game.camera.combined);

    game.batch.begin();
    game.font.draw(game.batch, "Generals", 0, GoG.V_HEIGHT);
    game.batch.draw(pieceImg, piece.x, piece.y, piece.width, piece.height);
    game.batch.end();

    // process user input
    if(Gdx.input.isTouched()) {
      Vector3 touchPos = new Vector3();
      touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      game.camera.unproject(touchPos);
      piece.x = touchPos.x - 64 / 2;
      piece.y = touchPos.y - 64 / 2;
    }
    if(Gdx.input.isKeyPressed(Keys.LEFT)) piece.x -= 200 * Gdx.graphics.getDeltaTime();
    if(Gdx.input.isKeyPressed(Keys.RIGHT)) piece.x += 200 * Gdx.graphics.getDeltaTime();

    // make sure the bucket stays within the screen bounds
    if(piece.x < 0) piece.x = 0;
    if(piece.x > GoG.V_WIDTH - 64) piece.x = GoG.V_WIDTH - 64;

    if(piece.y < 0) piece.y = 0;
    if(piece.y > GoG.V_HEIGHT - 64) piece.y = GoG.V_HEIGHT - 64;
  }

  @Override
  public void resize(int width, int height) {
    System.out.println("GameScreen Resizing");
    stage.getViewport().update(width, height, false);
  }

  @Override
  public void pause() {
    System.out.println("GameScreen Pausing");
  }

  @Override
  public void resume() {
    System.out.println("GameScreen Resuming");
  }

  @Override
  public void hide() {
    System.out.println("GameScreen Hidding");
  }

  @Override
  public void dispose() {
    System.out.println("GameScreen Disposing");
    pieceImg.dispose();
    stage.dispose();
  }
}

