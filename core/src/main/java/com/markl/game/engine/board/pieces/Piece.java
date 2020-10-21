package com.markl.game.engine.board.pieces;

import java.util.HashMap;
import java.util.Map;

import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.BoardUtils;
import com.markl.game.engine.board.Move;
import com.markl.game.engine.board.Player;
import com.markl.game.engine.board.Tile;

/**
 * Parent abstract class from where all the specific pieces will inherit from.
 *
 * Author: Mark Lucernas
 * Date: Sep 20, 2020
 */
public abstract class Piece {

  public String rank;                 // Rank of the piece
  public Board board;                 // Reference to game's Board
  public int powerLevel;              // Power level of the piece to compare ranks
  public int tileId;                  // Coordinates of this Piece instance
  public int legalPieceInstanceCount; // Allowed amount of piece instance owned by a Player in a single game
  public final Player owner;          // Player that owns this Piece
  public final Alliance alliance;     // Piece alliance of this Piece
  public Map<Integer, Move> moveSet;  // HashMap containing all currently available moves for this Piece instance

  /**
   * Constructor that takes in the owner Player, and Alliance of this piece.
   * Sets pieceTileId to -1 temporarily.
   */
  public Piece(final Board board, final Player owner, final Alliance alliance) {
    this.board = board;
    this.owner = owner;
    this.alliance = alliance;
    this.tileId = -1;
  }

  /**
   * Constructor that takes in the owner Player, Alliance and coordinates of this
   * Piece.
   */
  public Piece(final Board board, final Player owner,
      final Alliance alliance, final int tileId)
  {
    this.board = board;
    this.owner = owner;
    this.alliance = alliance;
    this.tileId = tileId;
  }

  /**
   * Gets the owner Player of this Piece.
   * @return Player pieceOwner field.
   */
  public Player getPieceOwner() {
    return this.owner;
  }

  /**
   * Gets this Piece current coordinates.
   * @return int pieceTileId field.
   */
  public int getPieceTileId() {
    return this.tileId;
  }

  /**
   * Sets this Piece current coordinates.
   * @param tileId new piece coordinates.
   */
  public void setPieceTileId(final int tileId) {
    this.tileId = tileId;
  }

  /**
   * Gets the current Tile containing this Piece.
   * @param board Board to get the Tile from.
   * @return Tile from the Board.
   */
  public Tile getTile() {
    return this.board.getTile(tileId);
  }

  /**
   * Evaluate this Piece current possible moves. Depends on
   * Move.evaluate() method.
   * Directions are clockwise. 0 for upward direction, 1 right, 2 down, 3 left.
   *
   * @return Map<String, Move> HashMap of possible moves.
   */
  public Map<Integer, Move> evaluateMoves() {
    moveSet = new HashMap<Integer, Move>();
    int direction;

    direction = 0;    // Up
    final int upAdjacentPieceTileId = this.tileId + -9;
    if (this.tileId >= BoardUtils.SECOND_ROW_INIT) {
      moveSet.put(direction, new Move(this.owner, this.board, this.tileId,
            upAdjacentPieceTileId));
      moveSet.get(direction).evaluate();
    }
    direction = 1;    // Right
    final int rightAdjacentPieceTileId = this.tileId + 1;
    if (rightAdjacentPieceTileId % 9 != 0) {
      moveSet.put(direction, new Move(this.owner, this.board, this.tileId,
            rightAdjacentPieceTileId));
      moveSet.get(direction).evaluate();
    }
    direction = 2;    // Down
    final int downAdjacentPieceTileId = this.tileId + 9;
    if (this.tileId < BoardUtils.LAST_ROW_INIT) {
      moveSet.put(direction, new Move(this.owner, this.board, this.tileId,
            downAdjacentPieceTileId));
      moveSet.get(direction).evaluate();
    }
    direction = 3;    // Left
    final int leftAdjacentPieceTileId = this.tileId + -1;
    if (this.tileId % 9 != 0) {
      moveSet.put(direction, new Move(this.owner, this.board, this.tileId,
            leftAdjacentPieceTileId));
      moveSet.get(direction).evaluate();
    }

    return moveSet;
  }

  //////////////////// Abstract methods to implement ////////////////////

  public abstract Piece clone();
  public abstract String getRank();
  public abstract Alliance getAlliance();
  public abstract int getPowerLevel();
  public abstract int getLegalPieceInstanceCount();
}
