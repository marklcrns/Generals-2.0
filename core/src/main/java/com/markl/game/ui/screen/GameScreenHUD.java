package com.markl.game.ui.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.markl.game.util.Constants;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 11/27/2020.
 */
public class GameScreenHUD {

  private Label undoLbl;
  private Label redoLbl;
  private Label aiDebugLbl;

  private GameScreen gameScreen;

  public GameScreenHUD(GameScreen gameScreen) {
    this.gameScreen = gameScreen;
  }

  public Table createGameHUD() {
    Table table = new Table();
    LabelStyle labelStyle = new LabelStyle(gameScreen.app.font, Color.WHITE);
    undoLbl = new Label("UNDO", labelStyle);
    redoLbl = new Label("REDO", labelStyle);
    aiDebugLbl = new Label("AI DEBUG", labelStyle);

    undoLbl.setAlignment(Align.center);
    redoLbl.setAlignment(Align.center);

    undoLbl.addListener(new ClickListener() {

      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        undoLbl.setColor(Color.YELLOW);
        super.enter(event, x, y, pointer, fromActor);
      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        undoLbl.setColor(Color.WHITE);
        super.exit(event, x, y, pointer, toActor);
      }

      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        gameScreen.moveManager.undoLastMove(true);
        super.touchUp(event, x, y, pointer, button);
      }
    });

    redoLbl.addListener(new ClickListener() {

      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        redoLbl.setColor(Color.YELLOW);
        super.enter(event, x, y, pointer, fromActor);
      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        redoLbl.setColor(Color.WHITE);
        super.exit(event, x, y, pointer, toActor);
      }

      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        gameScreen.moveManager.redoNextMove(true);
        super.touchUp(event, x, y, pointer, button);
      }
    });

    aiDebugLbl.addListener(new ClickListener() {

      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        aiDebugLbl.setColor(Color.YELLOW);
        super.enter(event, x, y, pointer, fromActor);
      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        aiDebugLbl.setColor(Color.WHITE);
        super.exit(event, x, y, pointer, toActor);
      }

      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        Stage stage = gameScreen.stage;
        if (!stage.getActors().contains(gameScreen.aiDebuggerWin, true))
          gameScreen.stage.addActor(gameScreen.aiDebuggerWin);
        else
          gameScreen.aiDebuggerWin.addAction(Actions.removeActor());
        super.touchUp(event, x, y, pointer, button);
      }
    });

    table.row();
    table.add(undoLbl).padRight(10).fillX();
    table.add(redoLbl).padRight(10).fillX();
    table.add(aiDebugLbl).padRight(10).fillX();

    return table;
  }

  public void rebuildGameHUD() {
    Table hud = createGameHUD();
    Container<Table> tableContainer = new Container<Table>();

    float screenWidth = Constants.VIEWPORT_WIDTH;
    float screenHeight = Constants.VIEWPORT_HEIGHT;

    float containerWidth = screenWidth * 0.6f;
    float containerHeight = screenHeight * 0.1f;

    tableContainer.setSize(containerWidth, containerHeight);
    tableContainer.setPosition((screenWidth - containerWidth) / 2.0f, (screenHeight - containerHeight));
    tableContainer.fillX();

    tableContainer.setActor(hud);
    gameScreen.stage.addActor(tableContainer);
  }

}
