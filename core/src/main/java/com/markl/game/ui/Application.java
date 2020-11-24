package com.markl.game.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.markl.game.ui.screen.GameScreen;
import com.markl.game.ui.screen.LoadingScreen;
import com.markl.game.ui.screen.MainMenuScreen;
import com.markl.game.ui.screen.SplashScreen;

import static com.markl.game.util.Constants.VIEWPORT_WIDTH;
import static com.markl.game.util.Constants.VIEWPORT_HEIGHT;

public class Application extends Game {

  public static final String TITLE = "Game of The Generals";

  public SpriteBatch batch;
  public BitmapFont font;
  public OrthographicCamera camera;
  public AssetManager assets;

  // Screens
  public LoadingScreen loadingScreen;
  public SplashScreen splashScreen;
  public MainMenuScreen mainMenuScreen;
  public GameScreen gameScreen;

  @Override
  public void create() {
    assets = new AssetManager();
    batch = new SpriteBatch();
    camera = new OrthographicCamera();
    camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    font = new BitmapFont();
    font.setColor(Color.YELLOW);

    loadingScreen = new LoadingScreen(this);
    splashScreen = new SplashScreen(this);
    mainMenuScreen = new MainMenuScreen(this);
    gameScreen = new GameScreen(this);

    this.setScreen(loadingScreen);
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
    gameScreen.dispose();
  }
}
