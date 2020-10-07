package com.markl.game.ui.board;

import com.badlogic.gdx.math.Rectangle;
import com.markl.game.engine.board.Tile;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/05/2020.
 */
public class TileUI extends Rectangle {

  public int id;
  public Tile tile;
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

  @Override
  public String toString() {
    return "id=" + id + ";x=" + this.x + ";y=" + this.y +
      ";width=" + this.width + ";height=" + this.height;
  }

  public void setTileId(int id)               { this.id = id; }
  public void setTile(Tile tile)              { this.tile = tile; }

  public int getTileId()                      { return this.id; }
  public Tile getTile()                       { return this.tile; }
}
