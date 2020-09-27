package com.markl.game.engine.board.pieces;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.BoardUtils;
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
    public int powerLevel;              // Power level of the piece to compare ranks
    public int coords;                  // Coordinates of this Piece instance
    public int legalPieceInstanceCount; // Allowed amount of piece instance owned by a Player in a single game
    public final Player owner;          // Player that owns this Piece
    public final Alliance alliance;     // Piece alliance of this Piece
    private Map<String, Move> moveSet;  // HashMap containing all currently available moves for this Piece instance

    /**
     * HashMap that contains the Piece mobility according to the Board coordinates
     * system.
     */
    public final Map<String, Integer> mobility = Collections.unmodifiableMap(
            new HashMap<String, Integer>() {
                { put("u", -9); put("d", 9); put("l", -1); put("r", 1); }
            });

    /**
     * Constructor that takes in the owner Player, and Alliance of this piece.
     * Sets pieceCoords to -1 temporarily.
     */
    public Piece(final Player owner, final Alliance alliance) {
        this.owner = owner;
        this.alliance = alliance;
        this.coords = -1;
    }

    /**
     * Constructor that takes in the owner Player, Alliance and coordinates of this
     * Piece.
     */
    public Piece(final Player owner, final Alliance alliance,
            final int coords) {
        this.owner = owner;
        this.alliance = alliance;
        this.coords = coords;
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
     * @return int pieceCoords field.
     */
    public int getPieceCoords() {
        return this.coords;
    }

    /**
     * Sets this Piece current coordinates.
     * @param coords new piece coordinates.
     */
    public void setPieceCoords(final int coords) {
        this.coords = coords;
    }

    /**
     * Gets the current Tile containing this Piece.
     * @param board Board to get the Tile from.
     * @return Tile from the Board.
     */
    // TODO: Improve to a no argument method.
    public Tile getTile(final Board board) {
        return board.getTile(coords);
    }

    /**
     * Evaluate this Piece current possible moves.
     * Depends on Move.evaluateMove() method.
     * @param board Board to evaluate the move from.
     * @return Map<String, Move> HashMap of possible moves.
     */
    // TODO: Improve to a no argument method.
    public Map<String, Move> evaluateMoves(final Board board) {
        moveSet = new HashMap<String, Move>();

        final int upAdjacentPieceCoords = this.coords + mobility.get("u");
        if (coords >= BoardUtils.SECOND_ROW_INIT) {
            moveSet.put("up", new Move(owner, board, coords, upAdjacentPieceCoords));
            moveSet.get("up").evaluateMove();
        }
        final int downAdjacentPieceCoords = this.coords + mobility.get("d");
        if (coords < BoardUtils.LAST_ROW_INIT) {
            moveSet.put("down", new Move(owner, board, coords, downAdjacentPieceCoords));
            moveSet.get("down").evaluateMove();
        }
        final int leftAdjacentPieceCoords = this.coords + mobility.get("l");
        if (this.coords % 9 != 0) {
            moveSet.put("left", new Move(owner, board, coords, leftAdjacentPieceCoords));
            moveSet.get("left").evaluateMove();
        }
        final int rightAdjacentPieceCoords = this.coords + mobility.get("r");
        if (rightAdjacentPieceCoords % 9 != 0) {
            moveSet.put("right", new Move(owner, board, coords, rightAdjacentPieceCoords));
            moveSet.get("right").evaluateMove();
        }

        return moveSet;
    }

    //////////////////// Abstract methods to implement ////////////////////

    public abstract Piece clone();
    public abstract String getRank();
    public abstract Alliance getPieceAlliance();
    public abstract int getPowerLevel();
    public abstract int getLegalPieceInstanceCount();
}
