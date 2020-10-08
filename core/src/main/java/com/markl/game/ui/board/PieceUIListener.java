package com.markl.game.ui.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.markl.game.ui.screen.GameScreen;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/07/2020.
 */
public class PieceUIListener extends ClickListener {

  private Camera camera;
  private PieceUI piece;

  public PieceUIListener(Camera camera, PieceUI piece) {
    this.camera = camera;
    this.piece = piece;
  }

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
    // Set click/touch position relative to world coordinates
    Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    camera.unproject(mousePos);

    // Prevent from leaving the board
    if (mousePos.x + piece.getWidth() / 2 > GameScreen.BOARD_WIDTH) {
      mousePos.x = GameScreen.BOARD_WIDTH - piece.getWidth() / 2;
    } else if (mousePos.x - piece.getWidth() / 2 < 0) {
      mousePos.x = piece.getWidth() / 2;
    }

    if (mousePos.y + piece.getHeight() / 2 > GameScreen.BOARD_HEIGHT) {
      mousePos.y = GameScreen.BOARD_HEIGHT - piece.getHeight() / 2;
    } else if (mousePos.y - piece.getHeight() / 2 < 0) {
      mousePos.y = piece.getHeight() / 2;
    }

    // add MoveToAction to piece actor
    MoveToAction mta = new MoveToAction();
    mta.setX(mousePos.x - piece.getWidth() / 2);
    mta.setY(mousePos.y - piece.getHeight() / 2);
    mta.setDuration(0.1f);
    piece.addAction(mta);
    piece.setZIndex(999); // Always on top of any pieces

    return super.touchDown(event, x, y, pointer, button);
  }

  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    super.touchUp(event, x, y, pointer, button);
  }

  @Override
  public void touchDragged(InputEvent event, float x, float y, int pointer) {
    super.touchDragged(event, x, y, pointer);

    // Set click/touch position relative to world coordinates
    Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    camera.unproject(mousePos); // mousePos is now in world coordinates

    // Prevent from leaving the board
    if (mousePos.x + piece.getWidth() / 2 > GameScreen.BOARD_WIDTH) {
      mousePos.x = GameScreen.BOARD_WIDTH - piece.getWidth() / 2;
    } else if (mousePos.x - piece.getWidth() / 2 < 0) {
      mousePos.x = piece.getWidth() / 2;
    }

    if (mousePos.y + piece.getHeight() / 2 > GameScreen.BOARD_HEIGHT) {
      mousePos.y = GameScreen.BOARD_HEIGHT - piece.getHeight() / 2;
    } else if (mousePos.y - piece.getHeight() / 2 < 0) {
      mousePos.y = piece.getHeight() / 2;
    }

    // add MoveToAction to piece actor
    MoveToAction mta = new MoveToAction();
    mta.setX(mousePos.x - piece.getWidth() / 2);
    mta.setY(mousePos.y - piece.getHeight() / 2);
    mta.setDuration(0.08f);
    piece.addAction(mta);
    piece.setZIndex(999); // Always on top of any pieces
  }
}
