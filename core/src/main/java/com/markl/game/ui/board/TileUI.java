package com.markl.game.ui.board;

import com.badlogic.gdx.math.Rectangle;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/05/2020.
 */
public class TileUI extends Rectangle {

  private int id;
  private PieceUI pieceUI;

  /**
   * No-argument constructor
   */
  public TileUI() {}

  /**
   * Constructor function that takes in tileId and Rectangle params.
   *
   * @param tileId
   */
  public TileUI (int tileId, float x, float y, float width, float height) {
    super(x, y, width, height);
    this.id = tileId;
  }

  /**
   * Constructor function that takes in tileId, Rectangle params and pieceUI.
   *
   * @param tileId
   */
  public TileUI (int tileId, float x, float y,
                 float width, float height, PieceUI pieceUI)
  {
    super(x, y, width, height);
    this.id = tileId;
    this.pieceUI = pieceUI;
  }

  public int getTileId() {
    return this.id;
  }

  public void setPieceUI(PieceUI pieceUI) {
    pieceUI.tileUI = this;
    this.pieceUI = pieceUI;
  }

  public void clearPieceUI() {
    if (pieceUI != null) {

      if (pieceUI.tileUI == this)
        pieceUI.tileUI = null;

      this.pieceUI = null;
    }
  }

  public PieceUI getPieceUI() {
    return this.pieceUI;
  }

  public boolean isTileUIOccupied() {
    if (pieceUI != null)
      return true;
    return false;
  }

  public boolean isTileUIEmpty() {
    if (pieceUI == null)
      return true;
    return false;
  }

  @Override
  public String toString() {
    return "id=" + id + ";x=" + this.x + ";y=" + this.y +
      ";width=" + this.width + ";height=" + this.height;
  }
}
