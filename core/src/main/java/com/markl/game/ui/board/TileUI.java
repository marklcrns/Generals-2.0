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
  public boolean isOccupied = false;

  /**
   * No-argument constructor
   */
  public TileUI() {
  }

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
   * Constructor function that takes in tileId, Rectangle, and isOccupied
   * params.
   *
   * @param tileId
   * @param isOccupied
   */
  public TileUI (int tileId, float x, float y, float width, float height, boolean isOccupied) {
    super(x, y, width, height);
    this.id = tileId;
    this.isOccupied = isOccupied;
  }

  @Override
  public String toString() {
    return "id=" + id + ";x=" + this.x + ";y=" + this.y +
      ";width=" + this.width + ";height=" + this.height;
  }
}
