 package com.markl.game.ui.board;

import com.badlogic.gdx.math.Rectangle;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/05/2020.
 */
public class TileUI extends Rectangle {

  public int id;
  public PieceUI pieceUI;

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
  public TileUI (int tileId, float x, float y, float width, float height,
      PieceUI pieceUI)
  {
    super(x, y, width, height);
    this.id = tileId;
    this.pieceUI = pieceUI;
  }

  public void setPieceUI(PieceUI pieceUI) {
    // Reference pieceUI to this tileUI if not null
    if (pieceUI != null)
      pieceUI.tileUI = this;

    this.pieceUI = pieceUI;
  }

  @Override
  public String toString() {
    return "id=" + id + ";x=" + this.x + ";y=" + this.y +
      ";width=" + this.width + ";height=" + this.height;
  }
}
