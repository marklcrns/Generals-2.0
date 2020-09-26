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

    public String rank;                  // Rank of the piece
    public int powerLevel;               // Power level of the piece to compare ranks
    public int pieceCoords;              // Coordinates of this Piece instance
    public int legalPieceInstanceCount;  // Allowed amount of piece instance owned by a Player in a single game
    public final Player pieceOwner;      // Player that owns this Piece
    public final Alliance pieceAlliance; // Piece alliance of this Piece
    private Map<String, Move> moveSet;   // HashMap containing all currently available moves for this Piece instance

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
        this.pieceOwner = owner;
        this.pieceAlliance = alliance;
        this.pieceCoords = -1;
    }

    /**
     * Constructor that takes in the owner Player, Alliance and coordinates of this
     * Piece.
     */
    public Piece(final Player owner, final Alliance alliance,
            final int coords) {
        this.pieceOwner = owner;
        this.pieceAlliance = alliance;
        this.pieceCoords = coords;
    }

    /**
     * Gets the owner Player of this Piece.
     * @return Player pieceOwner field.
     */
    public Player getPieceOwner() {
        return this.pieceOwner;
    }

    /**
     * Gets this Piece current coordinates.
     * @return int pieceCoords field.
     */
    public int getPieceCoords() {
        return this.pieceCoords;
    }

    /**
     * Sets this Piece current coordinates.
     * @param coords new piece coordinates.
     */
    public void setPieceCoords(final int coords) {
        this.pieceCoords = coords;
    }

    /**
     * Gets the current Tile containing this Piece.
     * @param board Board to get the Tile from.
     * @return Tile from the Board.
     */
    // TODO: Improve to a no argument method.
    public Tile getTile(final Board board) {
        return board.getTile(pieceCoords);
    }

    /**
     * Gets this Piece Alliance.
     * @return Alliance of this Piece.
     */
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
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

        final int upAdjacentPieceCoords = this.pieceCoords + mobility.get("u");
        if (pieceCoords >= BoardUtils.SECOND_ROW_INIT) {
            moveSet.put("up", new Move(pieceOwner, board, pieceCoords, upAdjacentPieceCoords));
            moveSet.get("up").evaluateMove();
        }
        final int downAdjacentPieceCoords = this.pieceCoords + mobility.get("d");
        if (pieceCoords < BoardUtils.LAST_ROW_INIT) {
            moveSet.put("down", new Move(pieceOwner, board, pieceCoords, downAdjacentPieceCoords));
            moveSet.get("down").evaluateMove();
        }
        final int leftAdjacentPieceCoords = this.pieceCoords + mobility.get("l");
        if (this.pieceCoords % 9 != 0) {
            moveSet.put("left", new Move(pieceOwner, board, pieceCoords, leftAdjacentPieceCoords));
            moveSet.get("left").evaluateMove();
        }
        final int rightAdjacentPieceCoords = this.pieceCoords + mobility.get("r");
        if (rightAdjacentPieceCoords % 9 != 0) {
            moveSet.put("right", new Move(pieceOwner, board, pieceCoords, rightAdjacentPieceCoords));
            moveSet.get("right").evaluateMove();
        }

        return moveSet;
    }

    //////////////////// Abstract methods to implement ////////////////////

    public abstract String getRank();
    public abstract int getLegalPieceInstanceCount();
    public abstract int getPowerLevel();
    public abstract Piece clone();
}
