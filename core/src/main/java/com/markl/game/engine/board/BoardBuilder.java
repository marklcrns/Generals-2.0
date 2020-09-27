package com.markl.game.engine.board;

import java.util.ArrayList;
import java.util.HashMap;
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
    private final Map<Integer, Piece> boardConfig; // HashMap of board configuration that contains all designated pieces
    private int blackPiecesCount;                  // Black pieces counter
    private int whitePiecesCount;                  // White pieces counter
    private Player playerBlack;
    private Player playerWhite;

    /** No argument constructor that initializes all class fields. */
    public BoardBuilder(Board board) {
        this.board = board;
        this.boardConfig = new HashMap<>();
        this.blackPiecesCount = 0;
        this.whitePiecesCount = 0;
        this.playerBlack = board.getPlayer(Alliance.BLACK);
        this.playerWhite = board.getPlayer(Alliance.WHITE);
    }

    /**
     * Gets black pieces count added to boardConfig field.
     * @return int blackPiecesCount field.
     */
    public int getBlackPiecesCount() {
        return blackPiecesCount;
    }

    /**
     * Gets white pieces count added to boardConfig field.
     * @return int whitePiecesCount field.
     */
    public int getWhitePiecesCount() {
        return blackPiecesCount;
    }

    /**
     * Method that creates a sample demo board configuration.
     * @return this with pre-made board configuration.
     */
    public BoardBuilder createDemoBoardBuild() {
        // Start Tile row index.
        final int[] row = {0, 8, 17, 26};

        // Black territory
        int boardOffset = 0;

        // row 0
        setPiece(new GeneralTwo(playerBlack, Alliance.BLACK, boardOffset + row[0] + 9));
        setPiece(new Major(playerBlack, Alliance.BLACK, boardOffset + row[0] + 8));
        setPiece(new Private(playerBlack, Alliance.BLACK, boardOffset + row[0] + 7));
        setPiece(new Sergeant(playerBlack, Alliance.BLACK, boardOffset + row[0] + 6));
        setPiece(new LtOne(playerBlack, Alliance.BLACK, boardOffset + row[0] + 5));
        setPiece(new Private(playerBlack, Alliance.BLACK, boardOffset + row[0] + 4));
        setPiece(new Flag(playerBlack, Alliance.BLACK, boardOffset + row[0] + 3));
        setPiece(new LtTwo(playerBlack, Alliance.BLACK, boardOffset + row[0] + 2));
        setPiece(new Private(playerBlack, Alliance.BLACK, boardOffset + row[0] + 1));
        // row 1
        setPiece(new Spy(playerBlack, Alliance.BLACK, boardOffset + row[1] + 8));
        setPiece(new Private(playerBlack, Alliance.BLACK, boardOffset + row[1] + 7));
        setPiece(new Captain(playerBlack, Alliance.BLACK, boardOffset + row[1] + 5));
        setPiece(new Spy(playerBlack, Alliance.BLACK, boardOffset + row[1] + 4));
        setPiece(new Colonel(playerBlack, Alliance.BLACK, boardOffset + row[1] + 3));
        setPiece(new Private(playerBlack, Alliance.BLACK, boardOffset + row[1] + 2));
        setPiece(new LtCol(playerBlack, Alliance.BLACK, boardOffset + row[1] + 1));
        // row 2
        setPiece(new GeneralThree(playerBlack, Alliance.BLACK, boardOffset + row[2] + 9));
        setPiece(new Private(playerBlack, Alliance.BLACK, boardOffset + row[2] + 6));
        setPiece(new GeneralFour(playerBlack, Alliance.BLACK, boardOffset + row[2] + 5));
        // row 3
        setPiece(new GeneralOne(playerBlack, Alliance.BLACK, boardOffset + row[3] + 3));
        setPiece(new GeneralFive(playerBlack, Alliance.BLACK, boardOffset + row[3] + 2));
        setPiece(new GeneralFive(playerWhite, Alliance.WHITE, boardOffset + row[3] + 1));

        // White territory
        boardOffset = BoardUtils.TOTAL_BOARD_TILES / 2;

        // row 0
        setPiece(new GeneralFive(playerWhite, Alliance.BLACK, boardOffset + row[0] + 1));
        setPiece(new GeneralFive(playerWhite, Alliance.WHITE, boardOffset + row[0] + 2));
        setPiece(new GeneralOne(playerWhite, Alliance.WHITE, boardOffset + row[0] + 3));
        // row 1
        setPiece(new GeneralFour(playerWhite, Alliance.WHITE, boardOffset + row[1] + 5));
        setPiece(new Private(playerWhite, Alliance.WHITE, boardOffset + row[1] + 6));
        setPiece(new GeneralThree(playerWhite, Alliance.WHITE, boardOffset + row[1] + 9));
        // row 2
        setPiece(new LtCol(playerWhite, Alliance.WHITE, boardOffset + row[2] + 1));
        setPiece(new Private(playerWhite, Alliance.WHITE, boardOffset + row[2] + 2));
        setPiece(new Colonel(playerWhite, Alliance.WHITE, boardOffset + row[2] + 3));
        setPiece(new Spy(playerWhite, Alliance.WHITE, boardOffset + row[2] + 4));
        setPiece(new Captain(playerWhite, Alliance.WHITE, boardOffset + row[2] + 5));
        setPiece(new Private(playerWhite, Alliance.WHITE, boardOffset + row[2] + 7));
        setPiece(new Spy(playerWhite, Alliance.WHITE, boardOffset + row[2] + 8));
        // row 3
        setPiece(new Private(playerWhite, Alliance.WHITE, boardOffset + row[3] + 1));
        setPiece(new LtTwo(playerWhite, Alliance.WHITE, boardOffset + row[3] + 2));
        setPiece(new Flag(playerWhite, Alliance.WHITE, boardOffset + row[3] + 3));
        setPiece(new Private(playerWhite, Alliance.WHITE, boardOffset + row[3] + 4));
        setPiece(new LtOne(playerWhite, Alliance.WHITE, boardOffset + row[3] + 5));
        setPiece(new Sergeant(playerWhite, Alliance.WHITE, boardOffset + row[3] + 6));
        setPiece(new Private(playerWhite, Alliance.WHITE, boardOffset + row[3] + 7));
        setPiece(new Major(playerWhite, Alliance.WHITE, boardOffset + row[3] + 8));
        setPiece(new GeneralTwo(playerWhite, Alliance.WHITE, boardOffset + row[3] + 9));

        return this;
    }

    /**
     * Method thats creates random board configuration.
     * @return this with random board configuration.
     */
    public BoardBuilder createRandomBuild() {
        final int[] occupiedTiles = {};

        // Black pieces
        final int[] blackTerritoryBounds = {0, (BoardUtils.TOTAL_BOARD_TILES / 2) - 1};
        final List<Piece> unsetBlackPieces = new ArrayList<>();

        unsetBlackPieces.add(new GeneralFive(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new GeneralFour(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new GeneralThree(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new GeneralTwo(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new GeneralOne(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new Colonel(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new LtCol(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new Major(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new Captain(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new LtOne(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new LtTwo(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new Sergeant(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new Private(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new Flag(playerBlack, Alliance.BLACK));
        unsetBlackPieces.add(new Spy(playerBlack, Alliance.BLACK));

        // Sets black pieces randomly excluding already occupied tiles.
        for (final Piece unsetPiece : unsetBlackPieces) {
            setAllPieceInstanceRandomly(
                    this, unsetPiece, blackTerritoryBounds[0],
                    blackTerritoryBounds[1], occupiedTiles);
        }

        // White pieces
        final int[] whiteTerritoryBounds = {BoardUtils.TOTAL_BOARD_TILES / 2,
            (BoardUtils.TOTAL_BOARD_TILES) - 1};
        final List<Piece> unsetWhitePieces = new ArrayList<>();

        unsetWhitePieces.add(new GeneralFive(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new GeneralFour(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new GeneralThree(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new GeneralTwo(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new GeneralOne(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new Colonel(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new LtCol(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new Major(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new Captain(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new LtOne(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new LtTwo(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new Sergeant(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new Private(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new Flag(playerWhite, Alliance.WHITE));
        unsetWhitePieces.add(new Spy(playerWhite, Alliance.WHITE));

        // Sets white pieces randomly excluding already occupied tiles.
        for (final Piece unsetPiece : unsetWhitePieces) {
            setAllPieceInstanceRandomly(
                    this, unsetPiece, whiteTerritoryBounds[0],
                    whiteTerritoryBounds[1], occupiedTiles);
        }

        return this;
    }

    /**
     * Sets piece in designated Tile location.
     * @param piece Piece instance to insert into specific Tile.
     * @return boolean true if successful, else false.
     */
    public boolean setPiece(final Piece piece) {
        // checks if within bounds, correct territory, and piece legal count
        if (isPieceWithinBounds(piece) &&
                isPieceInCorrectTerritory(piece) &&
                isLegalPieceInstanceChecker(piece) &&
                isTileEmpty(piece.getPieceCoords())) {
            boardConfig.put(piece.getPieceCoords(), piece);

            if (piece.getPieceAlliance() == Alliance.BLACK)
                this.blackPiecesCount++;
            else
                this.whitePiecesCount++;

            return true;
                }
        return false;
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
    public void setAllPieceInstanceRandomly(final BoardBuilder builder, final Piece piece,
            final int from, final int to,
            final int[] occupiedTiles) {
        Piece pieceCopy = piece.clone();
        int pieceInstanceCounter = countPieceInstances(piece.getRank(),
                piece.getPieceAlliance());
        int randomEmptyTile;

        while (pieceInstanceCounter < piece.getLegalPieceInstanceCount()) {
            randomEmptyTile = Utils.getRandomWithExclusion(from, to, occupiedTiles);
            pieceCopy.setPieceCoords(randomEmptyTile);
            // TODO: Fix to check if randomEmptyTile is empty
            if (builder.setPiece(pieceCopy)) {
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
                pieceInstanceCounter++;
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
                piece.getPieceCoords() >= 0) {
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
                 piece.getPieceCoords() > BoardUtils.TOTAL_BOARD_TILES / 2)) {
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
        for (final Map.Entry<Integer, Piece> entry : this.boardConfig.entrySet()) {
            if (piece.getRank() == entry.getValue().getRank() &&
                    piece.getPieceAlliance() == entry.getValue().getPieceAlliance())
                pieceInstanceCounter++;
        }

        if (pieceInstanceCounter <= piece.getLegalPieceInstanceCount())
            return true;

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
     * @return Map<Integer, Piece> boardConfig field if not null, else null.
     */
    public Map<Integer, Piece> getBoardConfig() {
        try {
            return this.boardConfig;
        } catch(final NullPointerException e) {
            System.out.println("BuilderBoard Error: Board config does not exist");
            return null;
        }

    }

    @Override
    public String toString() {
        String builder = "BoardBuilder boardConfig=" + boardConfig.size() + "\n";

        for (final Map.Entry<Integer, Piece> entry : boardConfig.entrySet()) {
            builder += "tileId=" + entry.getKey() +
                ";piece=" + entry.getValue().getRank() +
                ";pieceAlliance=" + entry.getValue().getPieceAlliance() +
                "\n";
        }

        return builder;
    }

}
