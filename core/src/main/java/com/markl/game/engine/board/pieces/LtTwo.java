package com.markl.game.engine.board.pieces;

import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.BoardUtils;
import com.markl.game.engine.board.Player;

/**
 * Second Lieutenant piece class that inherits from abstract Piece class.
 *
 * Author: Mark Lucernas
 * Date: Sep 20, 2020
 */
public class LtTwo extends Piece {

    private final String rank = BoardUtils.LT_TWO_RANK;                  // Rank of the piece
    private final int powerLevel = 4;                                    // Power level of the piece to compare ranks
    private final int legalPieceInstanceCount = BoardUtils.LT_TWO_COUNT; // Allowed amount of piece instance owned by a Player in a single game

    /**
     * Constructor that takes in the owner Player, and Alliance of this piece.
     * Sets coords to -1 temporarily.
     */
    public LtTwo(final Player owner, final Alliance alliance) {
        super(owner, alliance);
    }

    /**
     * Constructor that takes in the owner Player, Alliance and coordinates of
     * this Piece.
     */
    public LtTwo(final Player owner, final Alliance alliance,
            final int coords) {
        super(owner, alliance, coords);
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
    public final Alliance getPieceAlliance() {
        return this.alliance;
    }

    /**
     * Create deep copy of this specific Piece instance.
     * @return Piece deep copy of this Piece instance.
     */
    @Override
    public final Piece clone() {
        final LtTwo copy = new LtTwo(
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
