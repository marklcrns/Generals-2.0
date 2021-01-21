package com.markl.game.ui;

import static com.markl.game.util.Constants.VIEWPORT_HEIGHT;
import static com.markl.game.util.Constants.VIEWPORT_WIDTH;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.markl.game.Gog;
import com.markl.game.ui.screen.GameScreen;
import com.markl.game.ui.screen.GameScreen.GameMode;
import com.markl.game.ui.screen.LoadingScreen;
import com.markl.game.ui.screen.MainMenuScreen;
import com.markl.game.ui.screen.SplashScreen;

public class Application extends Game {

	public SpriteBatch batch;
	public BitmapFont font;
	public OrthographicCamera camera;
	public AssetManager assets;
	public Skin uiskin;

	// Game
	public Gog gog;

	// Screens
	public LoadingScreen loadingScreen;
	public SplashScreen splashScreen;
	public MainMenuScreen mainMenuScreen;
	public GameScreen singleGameScreen;
	public GameScreen localGameScreen;
	public GameScreen onlineGameScreen;

	@Override
	public void create() {
		assets = new AssetManager();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		font = new BitmapFont();
		font.setColor(Color.YELLOW);

		this.gog = new Gog();

		loadingScreen = new LoadingScreen(this);
		splashScreen = new SplashScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		singleGameScreen = new GameScreen(this, GameMode.SINGLE);
		localGameScreen = new GameScreen(this, GameMode.LOCAL);
		onlineGameScreen = new GameScreen(this, GameMode.ONLINE);

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
		singleGameScreen.dispose();
		localGameScreen.dispose();
		onlineGameScreen.dispose();
	}
}
