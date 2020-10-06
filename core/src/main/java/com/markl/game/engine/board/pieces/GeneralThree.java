package com.markl.game.engine.board.pieces;

import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.BoardUtils;
import com.markl.game.engine.board.Player;

/**
 * Three star general piece class that inherits from abstract Piece class.
 *
 * Author: Mark Lucernas
 * Date: Sep 20, 2020
 */
public class GeneralThree extends Piece {

  private final String rank = BoardUtils.GENERAL_THREE_RANK;                  // Rank of the piece
  private final int powerLevel = 12;                                          // Power level of the piece to compare ranks
  private final int legalPieceInstanceCount = BoardUtils.GENERAL_THREE_COUNT; // Allowed amount of piece instance owned by a Player in a single game

  /**
   * Constructor that takes in the owner Player, and Alliance of this piece.
   * Sets coords to -1 temporarily.
   */
  public GeneralThree(final Board board, final Player owner,
      final Alliance alliance)
  {
    super(board, owner, alliance);
  }

  /**
   * Constructor that takes in the owner Player, Alliance and coordinates of
   * this Piece.
   */
  public GeneralThree(final Board board, final Player owner,
      final Alliance alliance, final int coords) {
    super(board, owner, alliance, coords);
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
    final GeneralThree copy = new GeneralThree(
        this.board, this.owner,this.alliance, this.coords);
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
