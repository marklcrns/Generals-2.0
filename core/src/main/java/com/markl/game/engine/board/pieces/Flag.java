package com.markl.game.engine.board.pieces;

import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.BoardUtils;
import com.markl.game.engine.board.Player;

/**
 * Flag piece class that inherits from abstract Piece class.
 *
 * Author: Mark Lucernas
 * Date: Sep 20, 2020
 */
public class Flag extends Piece {

  private final String rank = BoardUtils.FLAG_RANK;                  // Rank of the piece
  private final int powerLevel = BoardUtils.FLAG_POW;                // Power level of the piece to compare ranks
  private final int legalPieceInstanceCount = BoardUtils.FLAG_COUNT; // Allowed amount of piece instance owned by a Player in a single game

  /**
   * Constructor that takes in the owner Player, and Alliance of this piece.
   */
  public Flag(int pieceId, final Board board, final Player owner, final Alliance alliance) {
    super(pieceId, board, owner, alliance);
  }

  /**
   * Constructor that takes in the owner Player, Alliance and coordinates of
   * this Piece.
   */
  public Flag(int pieceId, final Board board, final Player owner,
      final Alliance alliance, final int tileId) {
    super(pieceId, board, owner, alliance, tileId);
  }

  /**
   * Get piece rank.
   * @return String rank field.
   */
  @Override
  public final String getRank() {
    return this.rank;
  }

  /**
   * Get piece power level.
   * @return piece powerLevel.
   */
  @Override
  public final int getPowerLevel() {
    return this.powerLevel;
  }

  /**
   * Get piece legal instance count.
   * @return int getLegalPieceInstanceCount field.
   */
  @Override
  public final int getLegalPieceInstanceCount() {
    return this.legalPieceInstanceCount;
  }

  /**
   * Gets piece {@link Alliance}.
   * @return piece alliance.
   */
  @Override
  public final Alliance getAlliance() {
    return this.alliance;
  }

  /**
   * Create deep copy of this specific Piece instance.
   * @return Piece deep copy of this Piece instance.
   */
  @Override
  public final Piece clone() {
    final Flag copy = new Flag(
        this.pieceId, this.board, this.owner, this.alliance, this.tileId);
    return copy;
  }

  @Override
  public String toString() {
    return "rank=" + rank +
      ";tileId=" + tileId +
      ";owner=" + owner.getAlliance() +
      ";alliance=" + alliance;
  }
}
