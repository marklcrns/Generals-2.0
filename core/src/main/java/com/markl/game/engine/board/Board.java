package com.markl.game.engine.board;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.markl.game.GameState;
import com.markl.game.engine.board.pieces.Piece;

/**
 * @author Mark Lucernas
 * Date: Sep 19, 2020
 */
public class Board {

  private GameState game;                 // Game instance reference
  private LinkedList<Tile> tiles;         // List of all Tiles containing data of each piece
  private Map<Integer, Move> moveHistory; // Move history
  private Player playerBlack;             // Player instance that all contains all infos on black pieces
  private Player playerWhite;             // Player instance that all contains all infos on white pieces
  private int blackPiecesLeft = 0;        // Black pieces counter
  private int whitePiecesLeft = 0;        // White pieces counter

  /**
   * No argument constructor
   */
  public Board() {
    this.initBoard();
  }

  /**
   * Constructor function that takes in Game as a parameter
   * @param game {@link GameState} instance.
   */
  public Board(GameState game) {
    this.game = game;
    this.initBoard();
  }

  /**
   * Initializes Board instance
   */
  private void initBoard() {
    this.tiles = new LinkedList<Tile>();
    this.moveHistory = new HashMap<Integer, Move>();
    clearBoard();
  }

  public void initGame() {
    this.game.setBoard(this);
  }

  public int makeMove(int srcPieceTileId, int tgtPieceTileId) {
    if (getTile(srcPieceTileId).getPiece().getAlliance() ==
        game.getCurrentTurnMaker().getAlliance())
    {
      Move newMove = new Move(game.getCurrentTurnMaker(), this, srcPieceTileId, tgtPieceTileId);
      newMove.evaluate();
      newMove.execute();

      // Record if valid move
      if (newMove.getMoveType().getValue() != -1)
        moveHistory.put(newMove.getTurnId(), newMove);

      return newMove.getMoveType().getValue();
    }
    // Return invalid if piece does not owned by current turn maker player
    return -1;
  }

  /**
   * Method that empties board Tiles pieces.
   */
  public void clearBoard() {
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
   * @param srcPieceTileId source piece coordinates.
   * @param piece Piece instance to insert.
   * @return boolean true if successful, else false.
   */
  public boolean insertPiece(final int srcPieceTileId, final Piece piece) {
    if (this.getTile(srcPieceTileId).isTileEmpty()) {
      piece.setPieceTileId(srcPieceTileId);
      this.getAllTiles().get(srcPieceTileId).insertPiece(piece);
      this.getTile(srcPieceTileId).insertPiece(piece);
      return true;
    }
    return false;
  }

  /**
   * Replaces Tile piece.
   * @param tgtTileId target occupied tile to replace.
   * @param srcTileId new Piece instance to replace with.
   * @return boolean true if successful, else false.
   */
  public boolean replacePiece(final int tgtTileId, final Piece srcTileId) {
    if (this.getTile(tgtTileId).isTileOccupied()) {
      // TODO: improve piece manipulation efficiency
      srcTileId.setPieceTileId(tgtTileId);
      this.getAllTiles().get(tgtTileId).replacePiece(srcTileId);
      this.getTile(tgtTileId).replacePiece(srcTileId);

      return true;
    }
    return false;
  }

  /**
   * Moves piece from one Tile to another.
   * @param srcPieceTileId source piece Tile id.
   * @param tgtPieceTileId targetPiece Tile id.
   * @return boolean true if successful, else false.
   */
  public boolean movePiece(final int srcPieceTileId, final int tgtPieceTileId) {
    // insert copy of source piece into target tile
    if (this.getTile(srcPieceTileId).isTileOccupied() &&
        this.getTile(tgtPieceTileId).isTileEmpty())
    {
      final Piece srcPieceCopy = this.getTile(srcPieceTileId).getPiece().clone();
      srcPieceCopy.setPieceTileId(tgtPieceTileId);
      this.getTile(tgtPieceTileId).insertPiece(srcPieceCopy);
      // delete source piece
      this.getTile(srcPieceTileId).removePiece();

      return true;
    }
    return false;
  }

  /**
   * Deletes occupied tile.
   * @param pieceTileId piece coordinates.
   * @return boolean true if successful, else false.
   */
  public boolean deletePiece(final int pieceTileId) {
    if (this.getTile(pieceTileId).isTileOccupied()) {
      this.getTile(pieceTileId).removePiece();

      return true;
    }
    return false;
  }

  /**
   * Swaps two pieces and update piece coordinates.
   * @param srcPieceTileId source piece coordinates.
   * @param tgtPieceTileId target piece coordinates.
   * @return boolean true if successful, else false.
   */
  public boolean swapPiece(final int srcPieceTileId, final int tgtPieceTileId) {
    if (this.getTile(srcPieceTileId).isTileOccupied() &&
        this.getTile(tgtPieceTileId).isTileOccupied())
    {
      final Piece tmpSrcPiece = this.getTile(srcPieceTileId).getPiece().clone();
      final Piece tmpTgtPiece = this.getTile(tgtPieceTileId).getPiece().clone();

      tmpSrcPiece.setPieceTileId(tgtPieceTileId);
      tmpTgtPiece.setPieceTileId(srcPieceTileId);

      this.getAllTiles().get(srcPieceTileId).replacePiece(tmpTgtPiece);
      this.getAllTiles().get(tgtPieceTileId).replacePiece(tmpSrcPiece);

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
   * @param id tile number.
   * @return Tile from gameBoard field List.
   */
  public Tile getTile(int id) {
    return this.tiles.get(id);
  }

  /**
   * Gets the occupying piece in specified Tile
   * @param tileId  tile id
   * @return        Piece if Tile occupied, else null
   */
  public Piece getPiece(int tileId) {
    if (this.tiles.get(tileId).isTileOccupied())
      return this.tiles.get(tileId).getPiece();

    return null;
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
    return this.playerWhite;
  }

  public Map<Integer, Move> getMoveHistory() {
    if (moveHistory != null)
      return this.moveHistory;
    return null;
  }

  /**
   * Sets the required black Player instance.
   * @param player black Player instance.
   */
  public void setPlayerBlack(final Player player) {
    this.playerBlack = player;
  }

  /**
   * Sets the required white Player instance.
   * @param player white Player instance.
   */
  public void setPlayerWhite(final Player player) {
    this.playerWhite = player;
  }

  public void setBlackPiecesLeft(int blackPiecesLeft) {
    this.blackPiecesLeft = blackPiecesLeft;
  }

  public void setWhitePiecesLeft(int whitePiecesLeft) {
    this.whitePiecesLeft = whitePiecesLeft;
  }

  public GameState getGame() { return this.game; }

  /**
   * @return String representation of all current Tiles for debugging
   */
  @Override
  public String toString() {
    String debugBoard = "Board Debug Board\n";
    debugBoard += "    0 1 2 3 4 5 6 7 8\n";
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
