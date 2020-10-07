package com.markl.game.ui.board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/07/2020.
 */
public class PieceUI extends Actor {

  private Texture pieceTex;

  /**
   * No-args constructor
   */
  public PieceUI(Texture pieceTex) {
    this.pieceTex = pieceTex;
  }

  public void draw(Batch batch) {
    batch.draw(pieceTex, getX(), getY(), getWidth(), getHeight());
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Color color = getColor();
    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
    batch.draw(pieceTex, getX(), getY(), getWidth(), getHeight());
  }
}
