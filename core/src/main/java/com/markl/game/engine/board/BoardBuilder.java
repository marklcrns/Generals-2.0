package com.markl.game.engine.board;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.markl.game.engine.board.pieces.Captain;
import com.markl.game.engine.board.pieces.Colonel;
import com.markl.game.engine.board.pieces.Flag;
import com.markl.game.engine.board.pieces.GeneralFive;
import com.markl.game.engine.board.pieces.GeneralFour;
import com.markl.game.engine.board.pieces.GeneralOne;
import com.markl.game.engine.board.pieces.GeneralThree;
import com.markl.game.engine.board.pieces.GeneralTwo;
import com.markl.game.engine.board.pieces.LtCol;
import com.markl.game.engine.board.pieces.LtOne;
import com.markl.game.engine.board.pieces.LtTwo;
import com.markl.game.engine.board.pieces.Major;
import com.markl.game.engine.board.pieces.Piece;
import com.markl.game.engine.board.pieces.Private;
import com.markl.game.engine.board.pieces.Sergeant;
import com.markl.game.engine.board.pieces.Spy;
import com.markl.game.utils.Utils;

/**
 * Builds board for the {@link Board} class.
 *
 * @author Mark Lucernas
 * Date: Sep 19, 2020
 */
public class BoardBuilder {

    private Board board;
    private Player playerBlack;
    private Player playerWhite;
    private int blackPiecesCount;                  // Black pieces counter
    private int whitePiecesCount;                  // White pieces counter
    private final LinkedHashMap<Integer, Piece> boardConfig; // HashMap of board configuration that contains all designated pieces

    /** No argument constructor that initializes all class fields. */
    public BoardBuilder(Board board) {
        this.board = board;
        this.boardConfig = new LinkedHashMap<Integer, Piece>();
        this.blackPiecesCount = 0;
        this.whitePiecesCount = 0;
        this.playerBlack = board.getPlayer(Alliance.BLACK);
        this.playerWhite = board.getPlayer(Alliance.WHITE);
    }

    /**
     * Method that creates a sample demo board configuration.
     * @return this with pre-made board configuration.
     */
    public void createDemoBoardBuild() {
        final Alliance black = Alliance.BLACK;
        final Alliance white = Alliance.WHITE;
        final int[] row = {0, 8, 17, 26}; // Start Tile row index.
        int boardOffset = 0;              // Black territory

        // row 0
        setPiece(new Private(playerBlack, black, boardOffset + row[0] + 0));
        setPiece(new LtTwo(playerBlack, black, boardOffset + row[0] + 1));
        setPiece(new Flag(playerBlack, black, boardOffset + row[0] + 2));
        setPiece(new Private(playerBlack, black, boardOffset + row[0] + 3));
        setPiece(new LtOne(playerBlack, black, boardOffset + row[0] + 4));
        setPiece(new Sergeant(playerBlack, black, boardOffset + row[0] + 5));
        setPiece(new Private(playerBlack, black, boardOffset + row[0] + 6));
        setPiece(new Major(playerBlack, black, boardOffset + row[0] + 7));
        setPiece(new GeneralTwo(playerBlack, black, boardOffset + row[0] + 8));

        // row 1
        setPiece(new LtCol(playerBlack, black, boardOffset + row[1] + 1));
        setPiece(new Private(playerBlack, black, boardOffset + row[1] + 2));
        setPiece(new Colonel(playerBlack, black, boardOffset + row[1] + 3));
        setPiece(new Spy(playerBlack, black, boardOffset + row[1] + 4));
        setPiece(new Captain(playerBlack, black, boardOffset + row[1] + 5));
        setPiece(new Private(playerBlack, black, boardOffset + row[1] + 7));
        setPiece(new Spy(playerBlack, black, boardOffset + row[1] + 8));

        // row 2
        setPiece(new GeneralThree(playerBlack, black, boardOffset + row[2] + 9));
        setPiece(new Private(playerBlack, black, boardOffset + row[2] + 6));
        setPiece(new GeneralFour(playerBlack, black, boardOffset + row[2] + 5));
        // row 3
        setPiece(new GeneralOne(playerBlack, black, boardOffset + row[3] + 3));
        setPiece(new GeneralFive(playerBlack, black, boardOffset + row[3] + 2));

        // White territory
        boardOffset = BoardUtils.TOTAL_BOARD_TILES / 2;

        // row 0
        setPiece(new GeneralFive(playerWhite, white, boardOffset + row[0] + 1));
        setPiece(new Colonel(playerWhite, white, boardOffset + row[0] + 2));
        setPiece(new GeneralOne(playerWhite, white, boardOffset + row[0] + 3));
        // row 1
        setPiece(new GeneralFour(playerWhite, white, boardOffset + row[1] + 5));
        setPiece(new Private(playerWhite, white, boardOffset + row[1] + 6));
        setPiece(new GeneralThree(playerWhite, white, boardOffset + row[1] + 9));
        // row 2
        setPiece(new LtCol(playerWhite, white, boardOffset + row[2] + 1));
        setPiece(new Private(playerWhite, white, boardOffset + row[2] + 2));
        setPiece(new Spy(playerWhite, white, boardOffset + row[2] + 4));
        setPiece(new Captain(playerWhite, white, boardOffset + row[2] + 5));
        setPiece(new Private(playerWhite, white, boardOffset + row[2] + 7));
        setPiece(new Spy(playerWhite, white, boardOffset + row[2] + 8));
        // row 3
        setPiece(new Private(playerWhite, white, boardOffset + row[3] + 1));
        setPiece(new LtTwo(playerWhite, white, boardOffset + row[3] + 2));
        setPiece(new Flag(playerWhite, white, boardOffset + row[3] + 3));
        setPiece(new Private(playerWhite, white, boardOffset + row[3] + 4));
        setPiece(new LtOne(playerWhite, white, boardOffset + row[3] + 5));
        setPiece(new Sergeant(playerWhite, white, boardOffset + row[3] + 6));
        setPiece(new Private(playerWhite, white, boardOffset + row[3] + 7));
        setPiece(new Major(playerWhite, white, boardOffset + row[3] + 8));
        setPiece(new GeneralTwo(playerWhite, white, boardOffset + row[3] + 9));
    }

    /**
     * Method thats creates random board configuration.
     * @return this with random board configuration.
     */
    public void createRandomBuild() {
        final int[] occupiedTiles = {};

        // Black pieces
        final int[] blackTerritoryBounds = {0, (BoardUtils.TOTAL_BOARD_TILES / 2) - 1};
        final List<Piece> tmpBlackPiecesList = new ArrayList<>();

        tmpBlackPiecesList.add(new GeneralFive(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new GeneralFour(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new GeneralThree(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new GeneralTwo(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new GeneralOne(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new Colonel(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new LtCol(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new Major(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new Captain(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new LtOne(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new LtTwo(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new Sergeant(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new Private(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new Flag(playerBlack, Alliance.BLACK));
        tmpBlackPiecesList.add(new Spy(playerBlack, Alliance.BLACK));

        // Sets black pieces randomly excluding already occupied tiles.
        for (final Piece piece : tmpBlackPiecesList) {
            setAllPieceInstanceRandomly(piece, blackTerritoryBounds[0],
                    blackTerritoryBounds[1], occupiedTiles);
        }

        // White pieces
        final int[] whiteTerritoryBounds = {BoardUtils.TOTAL_BOARD_TILES / 2,
            (BoardUtils.TOTAL_BOARD_TILES) - 1};
        final List<Piece> tmpWhitePiecesList = new ArrayList<>();

        tmpWhitePiecesList.add(new GeneralFive(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new GeneralFour(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new GeneralThree(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new GeneralTwo(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new GeneralOne(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new Colonel(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new LtCol(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new Major(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new Captain(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new LtOne(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new LtTwo(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new Sergeant(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new Private(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new Flag(playerWhite, Alliance.WHITE));
        tmpWhitePiecesList.add(new Spy(playerWhite, Alliance.WHITE));

        // Sets white pieces randomly excluding already occupied tiles.
        for (final Piece piece : tmpWhitePiecesList) {
            setAllPieceInstanceRandomly(piece, whiteTerritoryBounds[0],
                    whiteTerritoryBounds[1], occupiedTiles);
        }
    }

    /**
     * Method that builds board pieces initial arrangement.
     * Depends on BoardBuilder inner class.
     */
    public void build() {
        if (this.boardConfig.size() < 42) {
            System.out.println("build() FAILED: Missing piece(s).");
            return;
        }

        this.board.discardPieces();
        List<Tile> tiles = this.board.getAllTiles();

        // Insert pieces to Board Tiles based on build config.
        for (final Map.Entry<Integer, Piece> entry : this.boardConfig.entrySet()) {
            // insert piece to Tile if empty
            if (tiles.get(entry.getKey()).isTileEmpty()) {
                tiles.get(entry.getKey()).insertPiece(entry.getValue());
            }
        };
        this.board.setBlackPiecesLeft(this.blackPiecesCount);
        this.board.setWhitePiecesLeft(this.whitePiecesCount);
    }

    /**
     * Sets piece in designated Tile id.
     * @param piece Piece instance to insert into specific Tile.
     * @return boolean true if successful, else false.
     */
    public boolean setPiece(final Piece piece) {
        if (!isPieceWithinBounds(piece)) {
            System.out.println("setPiece() FAILED: " + piece.getPieceAlliance() +
                               " " + piece.getRank() +
                               " at " + piece.getPieceCoords() +
                               ". Piece out of bounds.");
            return false;
        } else if (!isPieceInCorrectTerritory(piece)) {
            System.out.println("setPiece() FAILED: " + piece.getPieceAlliance() +
                               " " + piece.getRank() +
                               " at " + piece.getPieceCoords() +
                               ". Piece not in correct territory.");
            return false;
        } else if (!isLegalPieceInstanceChecker(piece)) {
            System.out.println("setPiece() FAILED: " + piece.getPieceAlliance() +
                               " " + piece.getRank() +
                               " at " + piece.getPieceCoords() +
                               ". Piece instance over limit " +
                               piece.getLegalPieceInstanceCount() + ".");
            return false;
        } else if (!isTileEmpty(piece.getPieceCoords())) {
            System.out.println("setPiece() FAILED: " + piece.getPieceAlliance() +
                               " " + piece.getRank() +
                               " at " + piece.getPieceCoords() +
                               ". Tile not empty.");
            return false;
        }

        boardConfig.put(piece.getPieceCoords(), piece);

        if (piece.getPieceAlliance() == Alliance.BLACK)
            this.blackPiecesCount++;
        else
            this.whitePiecesCount++;

        return true;
    }

    /**
     * Method thats sets all available amount of a single pieces in random
     * locations within its respective Alliance territory.
     * @param builder BoardBuilder to set the Piece into
     * @param piece Piece to set all legal amount of instance randomly.
     * @param from start index bounds to set the piece/pieces within.
     * @param to end index bounds to set the piece/pieces within.
     * @param occupiedTiles int array that contains all Tile exclusions to stop
     * inserting piece in.
     */
    public void setAllPieceInstanceRandomly(final Piece piece,
            final int from, final int to,
            final int[] occupiedTiles)
    {
        Piece pieceCopy = piece.clone();
        int pieceInstanceCounter = countPieceInstances(
                piece.getRank(), piece.getPieceAlliance());
        int randomEmptyTile;

        while (pieceInstanceCounter < piece.getLegalPieceInstanceCount()) {
            randomEmptyTile = Utils.getRandomWithExclusion(from, to, occupiedTiles);
            pieceCopy.setPieceCoords(randomEmptyTile);
            // TODO: Fix to check if randomEmptyTile is empty
            if (setPiece(pieceCopy)) {
                pieceCopy = piece.clone();
                Utils.appendToIntArray(occupiedTiles, randomEmptyTile);
                pieceInstanceCounter++;
            }
        }
    }

    /**
     * Method that counts all piece instances that has been set into boardConfig
     * field.
     * @param rank Piece rank of the piece to be counted.
     * @param alliance Alliance of the piece to be counted.
     * @return int the count of the specified piece.
     */
    public int countPieceInstances(final String rank, final Alliance alliance) {
        int pieceInstanceCounter = 0;

        // Count all pieces from boardConfig HashMap field.
        for (final Map.Entry<Integer, Piece> entry : boardConfig.entrySet()) {
            if (entry.getValue().getRank() == rank &&
                entry.getValue().getPieceAlliance() == alliance)
            {
                pieceInstanceCounter++;
            }
        }

        return pieceInstanceCounter;
    }

    /**
     * Checks if a Piece to be inserted is within bounds of the Board.
     * @param piece the Piece instance to be checked.
     * @return boolean true if piece is within bounds, else false.
     */
    public boolean isPieceWithinBounds(final Piece piece) {
        if (piece.getPieceCoords() < BoardUtils.TOTAL_BOARD_TILES &&
                piece.getPieceCoords() >= 0)
        {
            return true;
        }

        return false;
    }

    /**
     * Checks if a Piece to be inserted is within its respective territory
     * Alliance.
     * @param piece the Piece instance to be checked.
     * @return boolean true if piece is within its respective territory, else
     * false.
     */
    public boolean isPieceInCorrectTerritory(final Piece piece) {
        if ((piece.getPieceAlliance() == Alliance.BLACK &&
             piece.getPieceCoords() < BoardUtils.TOTAL_BOARD_TILES / 2) ||
            (piece.getPieceAlliance() == Alliance.WHITE &&
             piece.getPieceCoords() > BoardUtils.TOTAL_BOARD_TILES / 2))
        {
            return true;
        }
        return false;
    }

    /**
     * Checks if a Piece to be inserted exceeds the amount of allowed instance
     * in a single game.
     * @param piece the Piece instance to be checked.
     * @return boolean true if the piece is still less than or equal the amount
     * of allowed instance of the specific piece.
     */
    public boolean isLegalPieceInstanceChecker(final Piece piece) {
        int pieceInstanceCounter = 0;
        // Increment counter if instance detected within the same alliance
        for (final Map.Entry<Integer, Piece> entry : this.boardConfig.entrySet()) {
            if (piece.getRank().equals(entry.getValue().getRank()) &&
                piece.getPieceAlliance() == entry.getValue().getPieceAlliance())
            {
                pieceInstanceCounter++;
            }
        }

        if (pieceInstanceCounter < piece.getLegalPieceInstanceCount()) {
            return true;
        }
        return false;
    }

    /**
     * Checks if Tile is empty or does not contain a Piece instance.
     * @return boolean true if Tile is empty, else false.
     */
    public boolean isTileEmpty(final int coords) {
        if (!boardConfig.containsKey(coords))
            return true;
        return false;
    }

    /**
     * Gets the boardConfig field.
     * @return LinkedHashMap<Integer, Piece> boardConfig field if not null, else null.
     */
    public LinkedHashMap<Integer, Piece> getBoardConfig() {
        try {
            return this.boardConfig;
        } catch(final NullPointerException e) {
            System.out.println("BuilderBoard Error: Board config does not exist");
            return null;
        }

    }

    /**
     * @return String representation of boardConfig for debugging
     */
    @Override
    public String toString() {
        String boardConfigStatus = "BoardBuilder pieces=" + boardConfig.size() + "\n";

        for (final Map.Entry<Integer, Piece> entry : boardConfig.entrySet()) {
            boardConfigStatus += "tileId=" + entry.getKey() +
                ";piece=" + entry.getValue().getRank() +
                ";pieceAlliance=" + entry.getValue().getPieceAlliance() + "\n";
        }

        return boardConfigStatus;
    }
}
