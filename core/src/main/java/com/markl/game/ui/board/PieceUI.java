package com.markl.game.ui.board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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
  private AtlasRegion pieceTexVisible;
  private AtlasRegion pieceTexHidden;
  public boolean isVisible = false;

  /**
   * No-args constructor
   */
  public PieceUI(TileUI tile, String pieceRank, Alliance alliance) {
    this.tileUI           = tile;
    this.pieceRank        = pieceRank;
    this.alliance         = alliance;
  }

  public void show() { if (!isVisible) { this.isVisible = true; } }
  public void hide() { if (isVisible) { this.isVisible = false; } }
  public void setPieceTexVisible(AtlasRegion tex) { this.pieceTexVisible = tex; }
  public void setPieceTexHidden(AtlasRegion tex)  { this.pieceTexHidden = tex; }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Color color = getColor();
    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
    if (isVisible)
      batch.draw(pieceTexVisible, getX(), getY(), getWidth(), getHeight());
    else
      batch.draw(pieceTexHidden, getX(), getY(), getWidth(), getHeight());
  }
}
