package com.markl.game.ui.screen;

import static com.markl.game.util.Constants.VIEWPORT_HEIGHT;
import static com.markl.game.util.Constants.VIEWPORT_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.markl.game.ui.Application;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/03/2020.
 */
public class MainMenuScreen implements Screen {

  final Application app;

  private Stage stage;
  private InputMultiplexer inputMultiplexer;

  public MainMenuScreen(final Application app) {
    this.app = app;
  }

  public Table createControls() {
    Table table = new Table();
    LabelStyle labelStyle = new LabelStyle(app.font, Color.WHITE);

    Label singlePlayer = new Label("Single Player", labelStyle);
    Label online = new Label("Online", labelStyle);
    Label quit = new Label("Quit", labelStyle);

    table.row();
    table.add(singlePlayer).padBottom(25);
    table.row();
    table.add(online).padBottom(25);
    table.row();
    table.add(quit).padBottom(25);

    singlePlayer.addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        singlePlayer.setColor(Color.YELLOW);
        super.enter(event, x, y, pointer, fromActor);
      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        singlePlayer.setColor(Color.WHITE);
        super.exit(event, x, y, pointer, toActor);
      }

      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        app.setScreen(app.singleGameScreen);
        super.touchUp(event, x, y, pointer, button);
      }
    });

    online.addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        online.setColor(Color.YELLOW);
        super.enter(event, x, y, pointer, fromActor);
      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        online.setColor(Color.WHITE);
        super.exit(event, x, y, pointer, toActor);
      }

      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        app.setScreen(app.onlineGameScreen);
        super.touchUp(event, x, y, pointer, button);
      }
    });

    quit.addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        quit.setColor(Color.YELLOW);
        super.enter(event, x, y, pointer, fromActor);
      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        quit.setColor(Color.WHITE);
        super.exit(event, x, y, pointer, toActor);
      }

      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        Gdx.app.exit();
        super.touchUp(event, x, y, pointer, button);
      }
    });

    return table;
  }

  public void rebuildStage() {
    Table controls = createControls();

    stage.clear();
    Stack stack = new Stack();
    stage.addActor(stack);
    stack.setSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    stack.add(controls);
  }

  @Override
  public void show() {
      stage = new Stage(new StretchViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT));
      inputMultiplexer = new InputMultiplexer();
      inputMultiplexer.addProcessor(stage);
      Gdx.input.setInputProcessor(inputMultiplexer);
      rebuildStage();
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen

    stage.act(delta);
    stage.draw();

    // this.app.camera.update();
    // this.app.batch.setProjectionMatrix(this.app.camera.combined);

    // this.app.batch.begin();
    // this.app.font.draw(app.batch, "Welcome to Game of Generals!!!", 100, 150);
    // this.app.font.draw(app.batch, "Tap anywhere to begin!", 100, 100);
    // this.app.batch.end();

    // if (Gdx.input.isTouched()) {
    //   this.app.setScreen(new GameScreen(app));
    //   dispose();
    // }
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

