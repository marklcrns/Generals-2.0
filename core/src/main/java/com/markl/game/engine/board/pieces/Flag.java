package com.markl.game.engine.board.pieces;

import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.BoardUtils;
import com.markl.game.engine.board.Player;

/**
 * Flag piece class that inherits from abstract Piece class.
 *
 * Author: Mark Lucernas
 * Date: Sep 20, 2020
 */
public class Flag extends Piece {

  /** Rank of the piece */
  private final String rank = BoardUtils.FLAG_RANK;

  /** Power level of the piece to compare ranks */
  private final int powerLevel = 1;

  /** Allowed amount of piece instance owned by a Player in a single game */
  private final int legalPieceInstanceCount = 1;

  /**
   * Constructor that takes in the owner Player, and Alliance of this piece.
   * Sets coords to -1 temporarily.
   */
  public Flag(final Player owner, final Alliance alliance) {
    super(owner, alliance);
  }

  /**
   * Constructor that takes in the owner Player, Alliance and coordinates of
   * this Piece.
   */
  public Flag(final Player owner, final Alliance alliance,
              final int coords) {
    super(owner, alliance, coords);
  }

  /**
   * Gets the current rank of this specific Piece instance.
   * @return String rank field.
   */
  @Override
  public final String getRank() {
    return this.rank;
  }

  /**
   * Gets this Pieces instance power level.
   * @return int powerLevel field.
   */
  @Override
  public final int getPowerLevel() {
    return this.powerLevel;
  }

  /**
   * Create deep copy of this specific Piece instance.
   * @return Piece deep copy if this Piece instance.
   */
  @Override
  public final Piece clone() {
    final Flag copy = new Flag(
        this.owner, this.alliance, this.coords);
    return copy;
  }

  @Override
  public String toString() {
    return "piece=" + rank + ";powerLevel=" + powerLevel +
      ";coords=" + coords +
      ";legalPieceInstanceCount=" + legalPieceInstanceCount +
      ";owner=" + owner.getAlliance() +
      ";alliance=" + alliance;
  }
}
