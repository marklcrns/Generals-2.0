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

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Application extends Game {

  public static final String TITLE = "Game of The Generals";
  public static final float VERSION = 0.1f;
  public static final int V_WIDTH = 800;
  public static final int V_HEIGHT = 600;

  public SpriteBatch batch;
  public BitmapFont font;
  public OrthographicCamera camera;
  public AssetManager assets;

  // Screens
  public LoadingScreen loadingScreen;
  public SplashScreen splashScreen;
  public MainMenuScreen mainMenuScreen;
  public GameScreen gameScreen;

  private Socket socket;

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

    this.setScreen(loadingScreen);

    connectSocket();
    configSocketEvents();
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

  public void connectSocket() {
    try {
      socket = IO.socket("http://localhost:8080");
      socket.connect();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void configSocketEvents() {
    socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        // TODO Auto-generated method stub
        Gdx.app.log("SocketIO", "Connected");
      }
    }).on("socketID", new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
        try {
          String id = data.getString("id");
          Gdx.app.log("SocketIO", "My ID: " + id);
        } catch (JSONException e) {
          Gdx.app.log("SocketIO", "Error getting ID");
        }
      }
    }).on("newPlayer", new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
        try {
          String id = data.getString("id");
          Gdx.app.log("SocketIO", "New Player Connect: " + id);
        } catch (JSONException e) {
          Gdx.app.log("SocketIO", "Error getting New Player ID");
        }
      }
    });
  }
}
