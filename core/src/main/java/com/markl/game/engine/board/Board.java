package com.markl.game.engine.board;

import java.util.ArrayList;
import java.util.List;

import com.markl.game.engine.board.pieces.Piece;

/**
 * @author Mark Lucernas
 * Date: Sep 19, 2020
 */
public class Board {

    private List<Tile> tiles;       // List of all Tiles containing data of each piece
    private Player playerBlack;     // Player instance that all contains all infos on black pieces
    private Player playerWhite;     // Player instance that all contains all infos on white pieces
    private String playerBlackName; // Black player's name assigned when game initialized
    private String playerWhiteName; // White player's name assigned when game initialized

    public Board() {
        this.init();
    }

    private void init() {
        this.tiles = new ArrayList<Tile>();
    }


    /**
     * Gets current board state.
     * @return List<Tile> gameBoard field.
     */
    public List<Tile> getBoard() {
        return this.tiles;
    }

    /**
     * Swaps two pieces and update piece coordinates.
     * @param sourcePieceCoords source piece coordinates.
     * @param targetPieceCoords target piece coordinates.
     * @return boolean true if successful, else false.
     */
    public boolean swapPiece(final int sourcePieceCoords, final int targetPieceCoords) {
        if (this.getTile(sourcePieceCoords).isTileOccupied() &&
                this.getTile(targetPieceCoords).isTileOccupied()) {
            final Piece sourcePiece = this.getTile(sourcePieceCoords).getPiece().clone();
            final Piece targetPiece = this.getTile(targetPieceCoords).getPiece().clone();
            sourcePiece.setPieceCoords(targetPieceCoords);
            targetPiece.setPieceCoords(sourcePieceCoords);
            this.getBoard().get(sourcePieceCoords).replacePiece(targetPiece);
            this.getBoard().get(targetPieceCoords).replacePiece(sourcePiece);

            return true;
                }
        return false;
    }

    /**
     * Replaces Tile piece.
     * @param targetCoords target occupied tile to replace.
     * @param sourcePiece new Piece instance to replace with.
     * @return boolean true if successful, else false.
     */
    public boolean replacePiece(final int targetCoords, final Piece sourcePiece) {
        if (this.getTile(targetCoords).isTileOccupied()) {
            // TODO: improve piece manipulation efficiency
            sourcePiece.setPieceCoords(targetCoords);
            this.getBoard().get(targetCoords).replacePiece(sourcePiece);
            this.getTile(targetCoords).replacePiece(sourcePiece);

            return true;
        }
        return false;
    }

    /**
     * Moves piece from one Tile to another.
     * @param sourcePieceCoords source piece coordinates.
     * @param targetPieceCoords targetPiece coordinates.
     * @return boolean true if successful, else false.
     */
    public boolean movePiece(final int sourcePieceCoords, final int targetPieceCoords) {
        // insert copy of source piece into target tile
        if (this.getTile(targetPieceCoords).isTileEmpty()) {
            final Piece sourcePieceCopy = this.getTile(sourcePieceCoords).getPiece().clone();
            sourcePieceCopy.setPieceCoords(targetPieceCoords);
            this.getTile(targetPieceCoords).insertPiece(sourcePieceCopy);
            // delete source piece
            this.getTile(sourcePieceCoords).removePiece();

            return true;
        }
        return false;
    }

    /**
     * Inserts piece into an empty tile.
     * @param sourcePieceCoords source piece coordinates.
     * @param piece Piece instance to insert.
     * @return boolean true if successful, else false.
     */
    public boolean insertPiece(final int sourcePieceCoords, final Piece piece) {
        if (this.getTile(sourcePieceCoords).isTileEmpty()) {
            piece.setPieceCoords(sourcePieceCoords);
            this.getBoard().get(sourcePieceCoords).insertPiece(piece);
            this.getTile(sourcePieceCoords).insertPiece(piece);
            return true;
        }
        return false;
    }

    /**
     * Deletes occupied tile.
     * @param pieceCoords piece coordinates.
     * @return boolean true if successful, else false.
     */
    public boolean deletePiece(final int pieceCoords) {
        if (this.getTile(pieceCoords).isTileOccupied()) {
            this.getTile(pieceCoords).removePiece();

            return true;
        }
        return false;
    }

    /**
     * Method that adds Tile into gameBoard field.
     * @param tileId    tile id.
     * @param territory tile territory Alliance.
     * @param occupied  is tile occupied by a piece.
     */
    public final void addTile(final int tileId, final Alliance territory) {
        this.tiles.add(new Tile(tileId, territory));
    }

    /**
     * Gets specific tile from gameBoard field.
     * @param tileId tile number.
     * @return Tile from gameBoard field List.
     */
    public Tile getTile(int tileNum) {
        return this.tiles.get(tileNum);
    }

    /**
     * Method that empties board Tiles pieces.
     */
    private void emptyBoard() {
        this.tiles = new ArrayList<Tile>();
        // Add new empty Tiles in board
        for (int i = 0; i < BoardUtils.TOTAL_BOARD_TILES; i++) {
            // Set Tile territory
            if (i < BoardUtils.TOTAL_BOARD_TILES / 2)
                this.addTile(i, Alliance.BLACK);
            else
                this.addTile(i, Alliance.WHITE);
        }
    }

    /**
     * Gets specific Player currently registered in this Board based on the
     * alliance.
     * @param alliance Alliance of the Player.
     * @return Player based on the alliance param.
     */
    public Player getPlayer(final Alliance alliance) {
        if (alliance == Alliance.BLACK)
            return this.playerBlack;
        else
            return this.playerWhite;
    }

}
