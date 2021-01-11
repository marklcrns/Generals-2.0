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
import com.markl.game.util.Utils;

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

  public BoardBuilder(Board board) {
    this.board = board;
    this.boardConfig = new LinkedHashMap<Integer, Piece>();
    this.blackPiecesCount = 0;
    this.whitePiecesCount = 0;
    this.playerBlack = board.getGog().getPlayer(Alliance.BLACK);
    this.playerWhite = board.getGog().getPlayer(Alliance.WHITE);
  }

  // TODO: For debugging ONLY. Removed later  <07-10-20, yourname> //
  public void createTestBuild() {
    final Alliance black = Alliance.BLACK;
    final Alliance white = Alliance.WHITE;
    final int[] row = {0, 8, 17, 26}; // Start Tile row index.

    // Black territory
    int boardOffset = 0;
    setPiece(new GeneralFour(this.board, playerBlack, black, boardOffset + row[3] + 4), false);

    // White territory
    boardOffset = BoardUtils.TOTAL_BOARD_TILES / 2;
    setPiece(new Flag(this.board, playerWhite, white, boardOffset + row[0] + 4), false);
  }

  /**
   * Method that creates a sample demo board configuration.
   * @return this with pre-made board configuration.
   */
  public void createBoardDemoBuild() {
    final Alliance black = Alliance.BLACK;
    final Alliance white = Alliance.WHITE;
    final int[] row = {0, 8, 17, 26}; // Start Tile row index.

    // Black territory
    int boardOffset = 0;
    // row 0
    setPiece(new Private(this.board, playerBlack, black, boardOffset + row[0] + 0), false);
    setPiece(new LtTwo(this.board, playerBlack, black, boardOffset + row[0] + 1), false);
    setPiece(new Private(this.board, playerBlack, black, boardOffset + row[0] + 3), false);
    setPiece(new LtOne(this.board, playerBlack, black, boardOffset + row[0] + 4), false);
    setPiece(new Sergeant(this.board, playerBlack, black, boardOffset + row[0] + 5), false);
    setPiece(new Private(this.board, playerBlack, black, boardOffset + row[0] + 6), false);
    setPiece(new Major(this.board, playerBlack, black, boardOffset + row[0] + 7), false);
    setPiece(new GeneralTwo(this.board, playerBlack, black, boardOffset + row[0] + 8), false);
    // row 1
    setPiece(new LtCol(this.board, playerBlack, black, boardOffset + row[1] + 1), false);
    setPiece(new Private(this.board, playerBlack, black, boardOffset + row[1] + 2), false);
    setPiece(new Colonel(this.board, playerBlack, black, boardOffset + row[1] + 3), false);
    setPiece(new Spy(this.board, playerBlack, black, boardOffset + row[1] + 4), false);
    setPiece(new Captain(this.board, playerBlack, black, boardOffset + row[1] + 5), false);
    setPiece(new Private(this.board, playerBlack, black, boardOffset + row[1] + 7), false);
    setPiece(new Spy(this.board, playerBlack, black, boardOffset + row[1] + 8), false);
    // row 2
    setPiece(new GeneralThree(this.board, playerBlack, black, boardOffset + row[2] + 9), false);
    // row 3
    setPiece(new GeneralFive(this.board, playerBlack, black, boardOffset + row[3] + 2), false);
    setPiece(new GeneralOne(this.board, playerBlack, black, boardOffset + row[3] + 3), false);
    setPiece(new GeneralFour(this.board, playerBlack, black, boardOffset + row[3] + 4), false);
    setPiece(new Private(this.board, playerBlack, black, boardOffset + row[3] + 5), false);

    // White territory
    boardOffset = BoardUtils.TOTAL_BOARD_TILES / 2;
    // row 0
    setPiece(new GeneralFive(this.board, playerWhite, white, boardOffset + row[0] + 1), false);
    setPiece(new Colonel(this.board, playerWhite, white, boardOffset + row[0] + 2), false);
    setPiece(new GeneralOne(this.board, playerWhite, white, boardOffset + row[0] + 3), false);
    setPiece(new Flag(this.board, playerWhite, white, boardOffset + row[0] + 4), false);
    setPiece(new GeneralTwo(this.board, playerWhite, white, boardOffset + row[0] + 7), false);
    // row 1
    setPiece(new GeneralFour(this.board, playerWhite, white, boardOffset + row[1] + 5), false);
    setPiece(new Private(this.board, playerWhite, white, boardOffset + row[1] + 6), false);
    setPiece(new Spy(this.board, playerWhite, white, boardOffset + row[1] + 7), false);
    setPiece(new GeneralThree(this.board, playerWhite, white, boardOffset + row[1] + 8), false);
    // row 2
    setPiece(new LtCol(this.board, playerWhite, white, boardOffset + row[2] + 1), false);
    setPiece(new Private(this.board, playerWhite, white, boardOffset + row[2] + 2), false);
    setPiece(new Spy(this.board, playerWhite, white, boardOffset + row[2] + 4), false);
    setPiece(new Captain(this.board, playerWhite, white, boardOffset + row[2] + 5), false);
    setPiece(new Private(this.board, playerWhite, white, boardOffset + row[2] + 7), false);
    setPiece(new Flag(this.board, playerBlack, black, boardOffset + row[2] + 9), true);
    // row 3
    setPiece(new Private(this.board, playerWhite, white, boardOffset + row[3] + 1), false);
    setPiece(new LtTwo(this.board, playerWhite, white, boardOffset + row[3] + 2), false);
    setPiece(new Private(this.board, playerWhite, white, boardOffset + row[3] + 4), false);
    setPiece(new LtOne(this.board, playerWhite, white, boardOffset + row[3] + 5), false);
    setPiece(new Sergeant(this.board, playerWhite, white, boardOffset + row[3] + 6), false);
    setPiece(new Private(this.board, playerWhite, white, boardOffset + row[3] + 7), false);
    setPiece(new Major(this.board, playerWhite, white, boardOffset + row[3] + 8), false);
  }

  /**
   * Method thats creates random board configuration.
   * @return this with random board configuration.
   */
  public void createBoardRandomBuild() {
    final int[] occupiedTiles = {};

    // Black pieces
    final int[] blackTerritoryBounds = {0, (BoardUtils.TOTAL_BOARD_TILES / 2) - 1};
    final List<Piece> tmpBlackPiecesList = new ArrayList<>();

    tmpBlackPiecesList.add(new GeneralFive(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new GeneralFour(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new GeneralThree(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new GeneralTwo(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new GeneralOne(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new Colonel(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new LtCol(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new Major(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new Captain(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new LtOne(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new LtTwo(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new Sergeant(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new Private(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new Flag(this.board, playerBlack, Alliance.BLACK));
    tmpBlackPiecesList.add(new Spy(this.board, playerBlack, Alliance.BLACK));

    // Sets black pieces randomly excluding already occupied tiles.
    for (final Piece piece : tmpBlackPiecesList) {
      setAllPieceInstanceRandomly(piece, blackTerritoryBounds[0],
          blackTerritoryBounds[1], occupiedTiles);
    }

    // White pieces
    final int[] whiteTerritoryBounds = {BoardUtils.TOTAL_BOARD_TILES / 2,
      (BoardUtils.TOTAL_BOARD_TILES) - 1};
    final List<Piece> tmpWhitePiecesList = new ArrayList<>();

    tmpWhitePiecesList.add(new GeneralFive(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new GeneralFour(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new GeneralThree(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new GeneralTwo(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new GeneralOne(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new Colonel(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new LtCol(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new Major(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new Captain(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new LtOne(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new LtTwo(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new Sergeant(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new Private(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new Flag(this.board, playerWhite, Alliance.WHITE));
    tmpWhitePiecesList.add(new Spy(this.board, playerWhite, Alliance.WHITE));

    // Sets white pieces randomly excluding already occupied tiles.
    for (final Piece piece : tmpWhitePiecesList) {
      setAllPieceInstanceRandomly(piece, whiteTerritoryBounds[0],
          whiteTerritoryBounds[1], occupiedTiles);
    }
  }

  /**
   * Creates random build on specified territory
   * @param alliance    Alliance to build territory randomly
   */
  public void createTerritoryRandomBuild(Alliance alliance) {
    final int[] occupiedTiles = {};

    if (alliance == Alliance.BLACK) {
      // Black pieces
      final int[] blackTerritoryBounds = {0, (BoardUtils.TOTAL_BOARD_TILES / 2) - 1};
      final List<Piece> tmpBlackPiecesList = new ArrayList<>();

      tmpBlackPiecesList.add(new GeneralFive(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new GeneralFour(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new GeneralThree(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new GeneralTwo(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new GeneralOne(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new Colonel(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new LtCol(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new Major(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new Captain(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new LtOne(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new LtTwo(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new Sergeant(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new Private(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new Flag(this.board, playerBlack, Alliance.BLACK));
      tmpBlackPiecesList.add(new Spy(this.board, playerBlack, Alliance.BLACK));

      // Sets black pieces randomly excluding already occupied tiles.
      for (final Piece piece : tmpBlackPiecesList) {
        setAllPieceInstanceRandomly(piece, blackTerritoryBounds[0],
            blackTerritoryBounds[1], occupiedTiles);
      }
    } else {
      // White pieces
      final int[] whiteTerritoryBounds = {BoardUtils.TOTAL_BOARD_TILES / 2,
        (BoardUtils.TOTAL_BOARD_TILES) - 1};
      final List<Piece> tmpWhitePiecesList = new ArrayList<>();

      tmpWhitePiecesList.add(new GeneralFive(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new GeneralFour(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new GeneralThree(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new GeneralTwo(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new GeneralOne(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new Colonel(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new LtCol(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new Major(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new Captain(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new LtOne(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new LtTwo(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new Sergeant(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new Private(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new Flag(this.board, playerWhite, Alliance.WHITE));
      tmpWhitePiecesList.add(new Spy(this.board, playerWhite, Alliance.WHITE));

      // Sets white pieces randomly excluding already occupied tiles.
      for (final Piece piece : tmpWhitePiecesList) {
        setAllPieceInstanceRandomly(piece, whiteTerritoryBounds[0],
            whiteTerritoryBounds[1], occupiedTiles);
      }
    }
  }

  /**
   * Builds board pieces initial arrangement.
   * Debug mode skips board configuration size check.
   *
   * @param isDebug toggles debug mode
   */
  public void build(boolean isDebugMode) {
    // Checks if all pieces has been set
    if ((this.boardConfig.size() == 42 || this.boardConfig.size() == 21) &&
        !isDebugMode) {
      System.out.println("build() FAILED: Missing piece(s).");
      return;
    }

    this.board.clearBoard();
    List<Tile> tiles = this.board.getAllTiles();

    // Insert pieces to Board Tiles based on build config.
    for (final Map.Entry<Integer, Piece> entry : this.boardConfig.entrySet()) {
      // insert piece to Tile if empty
      if (tiles.get(entry.getKey()).isTileEmpty()) {
        tiles.get(entry.getKey()).insertPiece(entry.getValue());
      }
    };
    this.board.getGog().setBlackPiecesLeft(this.blackPiecesCount);
    this.board.getGog().setWhitePiecesLeft(this.whitePiecesCount); }

  /**
   * Sets piece in designated Tile id.
   * @param piece Piece instance to insert into specific Tile.
   * @return boolean true if successful, else false.
   */
  public boolean setPiece(final Piece piece, boolean isSkipTerritoryCheck) {
    if (!isPieceWithinBounds(piece)) {
      System.out.println("setPiece() FAILED: " + piece.getAlliance() +
          " " + piece.getRank() +
          " at " + piece.getPieceTileId() +
          ". Piece out of bounds.");
      return false;
    } else if (!isPieceInCorrectTerritory(piece) && !isSkipTerritoryCheck) {
      System.out.println("setPiece() FAILED: " + piece.getAlliance() +
          " " + piece.getRank() +
          " at " + piece.getPieceTileId() +
          ". Piece not in correct territory.");
      return false;
    } else if (!isLegalPieceInstanceChecker(piece)) {
      System.out.println("setPiece() FAILED: " + piece.getAlliance() +
          " " + piece.getRank() +
          " at " + piece.getPieceTileId() +
          ". Piece instance over limit " +
          piece.getLegalPieceInstanceCount() + ".");
      return false;
    } else if (!isTileEmpty(piece.getPieceTileId())) {
      System.out.println("setPiece() FAILED: " + piece.getAlliance() +
          " " + piece.getRank() +
          " at " + piece.getPieceTileId() +
          ". Tile not empty.");
      return false;
    }

    boardConfig.put(piece.getPieceTileId(), piece);

    if (piece.getAlliance() == Alliance.BLACK)
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
        piece.getRank(), piece.getAlliance());
    int randomEmptyTile;

    while (pieceInstanceCounter < piece.getLegalPieceInstanceCount()) {
      randomEmptyTile = Utils.getRandomIntWithExclusion(from, to, occupiedTiles);
      pieceCopy.setPieceTileId(randomEmptyTile);
      // TODO: Fix to check if randomEmptyTile is empty
      if (setPiece(pieceCopy, false)) {
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
          entry.getValue().getAlliance() == alliance)
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
    if (piece.getPieceTileId() < BoardUtils.TOTAL_BOARD_TILES &&
        piece.getPieceTileId() >= 0)
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
    if ((piece.getAlliance() == Alliance.BLACK &&
         piece.getPieceTileId() < BoardUtils.TOTAL_BOARD_TILES / 2) ||
        (piece.getAlliance() == Alliance.WHITE &&
         piece.getPieceTileId() > BoardUtils.TOTAL_BOARD_TILES / 2))
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
          piece.getAlliance() == entry.getValue().getAlliance())
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
  public boolean isTileEmpty(final int tileId) {
    if (!boardConfig.containsKey(tileId))
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
        ";pieceAlliance=" + entry.getValue().getAlliance() + "\n";
    }

    return boardConfigStatus;
  }
}
