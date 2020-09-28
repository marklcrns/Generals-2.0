package com.markl.game.engine.board;

import java.util.LinkedList;

import com.markl.game.engine.board.pieces.Piece;

/**
 * @author Mark Lucernas
 * Date: Sep 19, 2020
 */
public class Board {

    private LinkedList<Tile> tiles;  // List of all Tiles containing data of each piece
    private Player playerBlack;      // Player instance that all contains all infos on black pieces
    private Player playerWhite;      // Player instance that all contains all infos on white pieces
    private int blackPiecesLeft = 0; // Black pieces counter
    private int whitePiecesLeft = 0; // White pieces counter

    /**
     * No argument constructor
     */
    public Board() {
        this.init();
    }

    /**
     * Initializes Board instance
     */
    private void init() {
        this.tiles = new LinkedList<Tile>();
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
     * @param sourcePieceCoords source piece coordinates.
     * @param piece Piece instance to insert.
     * @return boolean true if successful, else false.
     */
    protected boolean insertPiece(final int sourcePieceCoords, final Piece piece) {
        if (this.getTile(sourcePieceCoords).isTileEmpty()) {
            piece.setPieceCoords(sourcePieceCoords);
            this.getAllTiles().get(sourcePieceCoords).insertPiece(piece);
            this.getTile(sourcePieceCoords).insertPiece(piece);
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
    protected boolean replacePiece(final int targetCoords, final Piece sourcePiece) {
        if (this.getTile(targetCoords).isTileOccupied()) {
            // TODO: improve piece manipulation efficiency
            sourcePiece.setPieceCoords(targetCoords);
            this.getAllTiles().get(targetCoords).replacePiece(sourcePiece);
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
    protected boolean movePiece(final int sourcePieceCoords, final int targetPieceCoords) {
        // insert copy of source piece into target tile
        if (this.getTile(sourcePieceCoords).isTileOccupied() &&
            this.getTile(targetPieceCoords).isTileEmpty())
        {
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
     * @param sourcePieceCoords source piece coordinates.
     * @param targetPieceCoords target piece coordinates.
     * @return boolean true if successful, else false.
     */
    protected boolean swapPiece(final int sourcePieceCoords, final int targetPieceCoords) {
        if (this.getTile(sourcePieceCoords).isTileOccupied() &&
                this.getTile(targetPieceCoords).isTileOccupied())
        {
            final Piece tmpSourcePiece = this.getTile(sourcePieceCoords).getPiece().clone();
            final Piece tmpTargetPiece = this.getTile(targetPieceCoords).getPiece().clone();

            tmpSourcePiece.setPieceCoords(targetPieceCoords);
            tmpTargetPiece.setPieceCoords(sourcePieceCoords);

            this.getAllTiles().get(sourcePieceCoords).replacePiece(tmpTargetPiece);
            this.getAllTiles().get(targetPieceCoords).replacePiece(tmpSourcePiece);

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
