package com.markl.game.ui.board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.markl.game.engine.board.Alliance;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/07/2020.
 */
public class PieceUI extends Actor {

  public TileUI tileUI;
  public String pieceRank;
  public Alliance alliance;
  private Texture pieceTexCurrent;
  private Texture pieceTexShow;
  private Texture pieceTexHidden;
  public boolean isHidden;

  /**
   * No-args constructor
   */
  public PieceUI(TileUI tile, String pieceRank, Alliance alliance,
                 Texture pieceTexShow, Texture pieceTexHidden) {
    this.tileUI           = tile;
    this.pieceRank        = pieceRank;
    this.alliance         = alliance;
    this.pieceTexShow     = pieceTexShow;
    this.pieceTexHidden   = pieceTexHidden;
    // Set current Texture displayed
    this.pieceTexCurrent = pieceTexShow;
    this.isHidden         = false;
  }

  public void showPieceDisplay() {
    if (isHidden) {
      pieceTexCurrent = pieceTexShow;
      isHidden = false;
    }
  }

  public void hidePieceDisplay() {
    if (!isHidden) {
      pieceTexCurrent = pieceTexHidden;
      isHidden = true;
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Color color = getColor();
    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
    batch.draw(pieceTexCurrent, getX(), getY(), getWidth(), getHeight());
  }
}
