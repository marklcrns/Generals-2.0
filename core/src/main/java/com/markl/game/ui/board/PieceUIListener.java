package com.markl.game.ui.board;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/07/2020.
 */
public class PieceUIListener extends ClickListener {

  private PieceUI piece;

  public PieceUIListener(PieceUI piece) {
    this.piece = piece;
  }

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
    System.out.println("Touched");
    return super.touchDown(event, x, y, pointer, button);
  }

  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    super.touchUp(event, x, y, pointer, button);
    piece.setPosition(x, y);
    System.out.println("x: " + piece.getX() + ", y: " + piece.getY());
  }

  @Override
  public void touchDragged(InputEvent event, float x, float y, int pointer) {
    super.touchDragged(event, x, y, pointer);
    // piece.setPosition(x, y);
    // System.out.println("x: " + piece.getX() + ", y: " + piece.getY());
  }
}
