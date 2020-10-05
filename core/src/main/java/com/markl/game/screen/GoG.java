package com.markl.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GoG extends Game {

  public static final String TITLE = "Game of The Generals";
  public static final float VERSION = 0.1f;
  public static final int V_WIDTH = 480;
  public static final int V_HEIGHT = 420;

  protected SpriteBatch batch;
  protected BitmapFont font;
  protected OrthographicCamera camera;
  protected AssetManager assets;

  protected LoadingScreen loadingScreen;
  protected SplashScreen splashScreen;
  protected MainMenuScreen mainMenuScreen;
  protected GameScreen gameScreen;

  @Override
  public void create() {
    assets = new AssetManager();
    batch = new SpriteBatch();
    camera = new OrthographicCamera();
    camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
    font = new BitmapFont();
    font.setColor(Color.YELLOW);

    loadingScreen = new LoadingScreen(this);
    splashScreen = new SplashScreen(this);
    mainMenuScreen = new MainMenuScreen(this);
    gameScreen = new GameScreen(this);

    this.setScreen(mainMenuScreen);
  }

  @Override
  public void render() {
    super.render();

    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
      Gdx.app.exit();
  }

  @Override
  public void dispose() {
    // dispose of all the native resources
    assets.dispose();
    batch.dispose();
    font.dispose();
    loadingScreen.dispose();
    splashScreen.dispose();
    mainMenuScreen.dispose();
  }

  // public static void main(String[] args) {
  //     Game game = new Game();
  //     Board board = new Board(game);
  //     BoardBuilder builder = new BoardBuilder(board);
  //     builder.createDemoBoardBuild();
  //     builder.build();
  //     game.start();
  //     System.out.println(builder.toString());
  //     System.out.println(board.toString());
}
