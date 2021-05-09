package com.markl.game.engine.board;

import com.markl.game.engine.board.pieces.Piece;

/**
 * @author Mark Lucernas
 * Date: Sep 19, 2020
 */
public class Tile {

  private int id;             // Tile unique index or ID number. From 0 to 71
  private boolean occupied;   // Is tile occupied by piece
  private Alliance territory; // Tile territorial Alliance. 0 - 35 black and 36 - 71 white territory.
  private Piece piece;        // Containing piece. Null if empty or remains uninitialized.

  /**
   * Constructor that takes in the tileId and territorial Alliance, and sets
   * the tile as empty.
   * @param tileId    Unique id of {@link Tile}
   * @param territory {@link Tile} territorial {@link Alliance}
   */
  public Tile(final int tileId, final Alliance territory) {
    this.id = tileId;
    this.territory = territory;
    this.occupied = false;
  }

  /**
   * Checks if this Tile is empty of Piece instance.
   * @return boolean true if this Tile is empty, else false.
   */
  public boolean isTileEmpty() {
    if (!this.occupied) {
      return true;
    }
    return false;
  }

  /**
   * Checks if this {@link Tile} is occupied by Piece instance.
   * @return boolean true if this Tile is occupied, else false.
   */
  public boolean isTileOccupied() {
    if (this.occupied)
      return true;

    return false;
  }

  /**
   * Returns the Tile index or ID.
   * @return int tileId field.
   */
  public int getTileId() {
    return this.id;
  }

  /**
   * Returns the Tile index or ID.
   * @return int tileId field.
   */
  public void setTileId(int id) {
    this.id = id;
  }

  /**
   * Gets the Tile territorial Alliance.
   * @return Alliance territory field.
   */
  public Alliance getTerritory() {
    return this.territory;
  }

  /**
   * Gets the occupying Piece of the Tile.
   * @return the Piece occupying the Tile, else null.
   */
  public Piece getPiece() {
    return this.piece;
  }

  /**
   * Inserts the Piece into this Tile.
   * @param piece the Piece instance to insert.
   * @return boolean true if successful, else false if already occupied.
   */
  public boolean insertPiece(final Piece piece) {
    if (!this.occupied) {
      this.piece = piece;
      this.occupied = true;
      return true;
    }

    return false;
  }

  /**
   * Replaces the occupying Piece with another Piece instance.
   * @param piece the Piece to replace the existing with.
   * @return boolean true if successful, else false if is Tile is empty.
   */
  public boolean replacePiece(final Piece piece) {
    if (this.occupied) {
      this.piece = piece;
      return true;
    }

    return false;
  }

  /**
   * Empties the occupying piece of this Tile.
   * @return boolean true if successful, else false if tile is empty.
   */
  public boolean removePiece() {
    if (isTileOccupied()) {
      this.piece = null;
      this.occupied = false;
      return true;
    }

    return false;
  }

  /**
   * Creates deep copy of this Tile instance.
   * @return Tile deep copy of this Tile instance.
   */
  @Override
  public Tile clone() {
    final Tile tileCopy = new Tile(this.id, this.territory);

    if (isTileOccupied())
      tileCopy.insertPiece(getPiece().clone());

    return tileCopy;
  }

  @Override
  public String toString() {
    if (this.occupied)
      return "Tile " + this.id + " contains " +
        this.piece.getAlliance() + " " + this.piece.getRank();
    else
      return "Tile " + this.id + " is empty";
  }
}
