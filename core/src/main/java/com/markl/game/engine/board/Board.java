package com.markl.game.engine.board;

import java.util.LinkedList;

import com.markl.game.Game;
import com.markl.game.engine.board.pieces.Piece;

/**
 * @author Mark Lucernas
 * Date: Sep 19, 2020
 */
public class Board {

    private Game game;               // Game instance reference
    private LinkedList<Tile> tiles;  // List of all Tiles containing data of each piece
    private Player playerBlack;      // Player instance that all contains all infos on black pieces
    private Player playerWhite;      // Player instance that all contains all infos on white pieces
    private int blackPiecesLeft = 0; // Black pieces counter
    private int whitePiecesLeft = 0; // White pieces counter

    /**
     * No argument constructor
     */
    public Board() {
        this.initBoard();
    }

    /**
     * Constructor function that takes in Game as a parameter
     * @param game {@link Game} instance.
     */
    public Board(Game game) {
        this.game = game;
        this.initBoard();
    }

    /**
     * Initializes Board instance
     */
    private void initBoard() {
        this.tiles = new LinkedList<Tile>();
    }

    public void initGame() {
        this.game.setBoard(this);
    }

    /**
     * Method that empties board Tiles pieces.
     */
    protected void discardPieces() {
        this.tiles = new LinkedList<Tile>();
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
     * Inserts piece into an empty tile.
     * @param srcPieceCoords source piece coordinates.
     * @param piece Piece instance to insert.
     * @return boolean true if successful, else false.
     */
    protected boolean insertPiece(final int srcPieceCoords, final Piece piece) {
        if (this.getTile(srcPieceCoords).isTileEmpty()) {
            piece.setPieceCoords(srcPieceCoords);
            this.getAllTiles().get(srcPieceCoords).insertPiece(piece);
            this.getTile(srcPieceCoords).insertPiece(piece);
            return true;
        }
        return false;
    }

    /**
     * Replaces Tile piece.
     * @param tgtCoords target occupied tile to replace.
     * @param srcPiece new Piece instance to replace with.
     * @return boolean true if successful, else false.
     */
    protected boolean replacePiece(final int tgtCoords, final Piece srcPiece) {
        if (this.getTile(tgtCoords).isTileOccupied()) {
            // TODO: improve piece manipulation efficiency
            srcPiece.setPieceCoords(tgtCoords);
            this.getAllTiles().get(tgtCoords).replacePiece(srcPiece);
            this.getTile(tgtCoords).replacePiece(srcPiece);

            return true;
        }
        return false;
    }

    /**
     * Moves piece from one Tile to another.
     * @param srcPieceCoords source piece coordinates.
     * @param tgtPieceCoords targetPiece coordinates.
     * @return boolean true if successful, else false.
     */
    protected boolean movePiece(final int srcPieceCoords, final int tgtPieceCoords) {
        // insert copy of source piece into target tile
        if (this.getTile(srcPieceCoords).isTileOccupied() &&
            this.getTile(tgtPieceCoords).isTileEmpty())
        {
            final Piece srcPieceCopy = this.getTile(srcPieceCoords).getPiece().clone();
            srcPieceCopy.setPieceCoords(tgtPieceCoords);
            this.getTile(tgtPieceCoords).insertPiece(srcPieceCopy);
            // delete source piece
            this.getTile(srcPieceCoords).removePiece();

            return true;
        }
        return false;
    }

    /**
     * Deletes occupied tile.
     * @param pieceCoords piece coordinates.
     * @return boolean true if successful, else false.
     */
    protected boolean deletePiece(final int pieceCoords) {
        if (this.getTile(pieceCoords).isTileOccupied()) {
            this.getTile(pieceCoords).removePiece();

            return true;
        }
        return false;
    }

    /**
     * Swaps two pieces and update piece coordinates.
     * @param srcPieceCoords source piece coordinates.
     * @param tgtPieceCoords target piece coordinates.
     * @return boolean true if successful, else false.
     */
    protected boolean swapPiece(final int srcPieceCoords, final int tgtPieceCoords) {
        if (this.getTile(srcPieceCoords).isTileOccupied() &&
                this.getTile(tgtPieceCoords).isTileOccupied())
        {
            final Piece tmpSrcPiece = this.getTile(srcPieceCoords).getPiece().clone();
            final Piece tmpTgtPiece = this.getTile(tgtPieceCoords).getPiece().clone();

            tmpSrcPiece.setPieceCoords(tgtPieceCoords);
            tmpTgtPiece.setPieceCoords(srcPieceCoords);

            this.getAllTiles().get(srcPieceCoords).replacePiece(tmpTgtPiece);
            this.getAllTiles().get(tgtPieceCoords).replacePiece(tmpSrcPiece);

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
    protected final void addTile(final int tileId, final Alliance territory) {
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
     * Gets current board state.
     * @return List<Tile> gameBoard field.
     */
    public LinkedList<Tile> getAllTiles() {
        return this.tiles;
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

    public void setBlackPiecesLeft(int blackPiecesLeft) {
        this.blackPiecesLeft = blackPiecesLeft;
    }

    public void setWhitePiecesLeft(int whitePiecesLeft) {
        this.whitePiecesLeft = whitePiecesLeft;
    }

    public Game getGame() { return this.game; }

    /**
     * @return String representation of all current Tiles for debugging
     */
    @Override
    public String toString() {
        String debugBoard = "    0 1 2 3 4 5 6 7 8\n";
        debugBoard += "    _________________\n";
        for (int i = 0; i < BoardUtils.TOTAL_BOARD_TILES / 2; i += 9) {
            if (i < 10)
                debugBoard += " " + i + " |";
            else
                debugBoard += i + " |";
            for (int j = i; j < i + 9; j++) {
                if (this.getTile(j).isTileEmpty()) {
                    debugBoard += "-";
                } else {
                    final String rank = this.getTile(j).getPiece().getRank();
                    if (rank == "GeneralOne")
                        debugBoard += "1";
                    else if (rank == "GeneralTwo")
                        debugBoard += "2";
                    else if (rank == "GeneralThree")
                        debugBoard += "3";
                    else if (rank == "GeneralFour")
                        debugBoard += "4";
                    else if (rank == "GeneralFive")
                        debugBoard += "5";
                    else
                        debugBoard += rank.substring(0, 1);
                }
                debugBoard += " ";
            }
            debugBoard += "\n";
        }

        debugBoard += "   |-----------------\n";

        for (int i = BoardUtils.TOTAL_BOARD_TILES / 2; i < BoardUtils.TOTAL_BOARD_TILES; i += 9) {
            if (i < 10)
                debugBoard += " " + i + " |";
            else
                debugBoard += i + " |";
            for (int j = i; j < i + 9; j++) {
                if (this.getTile(j).isTileEmpty()) {
                    debugBoard += "-";
                } else {
                    final String rank = this.getTile(j).getPiece().getRank();
                    if (rank == "GeneralOne")
                        debugBoard += "1";
                    else if (rank == "GeneralTwo")
                        debugBoard += "2";
                    else if (rank == "GeneralThree")
                        debugBoard += "3";
                    else if (rank == "GeneralFour")
                        debugBoard += "4";
                    else if (rank == "GeneralFive")
                        debugBoard += "5";
                    else
                        debugBoard += rank.substring(0, 1);
                }
                debugBoard += " ";
            }
            debugBoard += "\n";
        }

        return debugBoard;
    }

}
