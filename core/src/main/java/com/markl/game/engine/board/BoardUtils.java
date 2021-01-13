package com.markl.game.engine.board;

import com.markl.game.Gog;
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

/**
 * Utility class for engine and gui convenience.
 *
 * @author Mark Lucernas
 * Date: Sep 20, 2020
 */
public class BoardUtils {

  /** Board tiles measurements */
  public static final int BOARD_TILES_COL_COUNT = 9;
  public static final int BOARD_TILES_ROW_COUNT = 8;
  public static final int TOTAL_BOARD_TILES = BOARD_TILES_COL_COUNT * BOARD_TILES_ROW_COUNT;

  /** Board anchors */
  public static final int FIRST_ROW_INIT = 0;                                                  // First tile index of the first board row
  public static final int SECOND_ROW_INIT = FIRST_ROW_INIT + BOARD_TILES_COL_COUNT;            // First tile index of the second board row
  public static final int LAST_ROW_INIT = (BOARD_TILES_ROW_COUNT - 1) * BOARD_TILES_COL_COUNT; // First tile index of the last board row
  public static final int SECOND_TO_LAST_ROW_INIT = LAST_ROW_INIT - BOARD_TILES_COL_COUNT;     // First tile index of the second to las board row

  /** Pieces ranks string name */
  public static final String SPY_RANK           = "Spy";
  public static final String GENERAL_FIVE_RANK  = "GeneralFive";
  public static final String GENERAL_FOUR_RANK  = "GeneralFour";
  public static final String GENERAL_THREE_RANK = "GeneralThree";
  public static final String GENERAL_TWO_RANK   = "GeneralTwo";
  public static final String GENERAL_ONE_RANK   = "GeneralOne";
  public static final String COLONEL_RANK       = "Colonel";
  public static final String LT_COLONEL_RANK    = "LtCol";
  public static final String MAJOR_RANK         = "Major";
  public static final String CAPTAIN_RANK       = "Captain";
  public static final String LT_ONE_RANK        = "LtOne";
  public static final String LT_TWO_RANK        = "LtTwo";
  public static final String SERGEANT_RANK      = "Sergeant";
  public static final String PRIVATE_RANK       = "Private";
  public static final String FLAG_RANK          = "Flag";

  /** Pieces powerlevel */
  public static final int SPY_POW           = 999;
  public static final int GENERAL_FIVE_POW  = 14;
  public static final int GENERAL_FOUR_POW  = 13;
  public static final int GENERAL_THREE_POW = 12;
  public static final int GENERAL_TWO_POW   = 11;
  public static final int GENERAL_ONE_POW   = 10;
  public static final int COLONEL_POW       = 9;
  public static final int LT_COLONEL_POW    = 8;
  public static final int MAJOR_POW         = 7;
  public static final int CAPTAIN_POW       = 6;
  public static final int LT_ONE_POW        = 5;
  public static final int LT_TWO_POW        = 4;
  public static final int SERGEANT_POW      = 3;
  public static final int PRIVATE_POW       = 2;
  public static final int FLAG_POW          = 1;

  /** Individual pieces count */
  public static final int SPY_COUNT           = 2;
  public static final int GENERAL_FIVE_COUNT  = 1;
  public static final int GENERAL_FOUR_COUNT  = 1;
  public static final int GENERAL_THREE_COUNT = 1;
  public static final int GENERAL_TWO_COUNT   = 1;
  public static final int GENERAL_ONE_COUNT   = 1;
  public static final int COLONEL_COUNT       = 1;
  public static final int LT_COLONEL_COUNT    = 1;
  public static final int MAJOR_COUNT         = 1;
  public static final int CAPTAIN_COUNT       = 1;
  public static final int LT_ONE_COUNT        = 1;
  public static final int LT_TWO_COUNT        = 1;
  public static final int SERGEANT_COUNT      = 1;
  public static final int PRIVATE_COUNT       = 6;
  public static final int FLAG_COUNT          = 1;

  /**
   * Creates Piece instance of the passed in piece rank and alliance.
   * @param pieceRankName   name or rank of the piece to be created.
   * @param owner           Player reference who will own the piece.
   * @param alliance        Alliance of the piece.
   * @return the Piece created. Null unsuccessful.
   */
  public static Piece pieceInstanceCreator(final Gog gog, final String pieceRankName,
      final Player owner, final Alliance alliance)
  {
    Piece piece = null;

    if (pieceRankName.contains(GENERAL_FIVE_RANK))
      piece = new GeneralFive(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(GENERAL_FOUR_RANK))
      piece = new GeneralFour(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(GENERAL_THREE_RANK))
      piece = new GeneralThree(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(GENERAL_TWO_RANK))
      piece = new GeneralTwo(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(GENERAL_ONE_RANK))
      piece = new GeneralOne(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(COLONEL_RANK))
      piece = new Colonel(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(LT_COLONEL_RANK))
      piece = new LtCol(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(MAJOR_RANK))
      piece = new Major(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(CAPTAIN_RANK))
      piece = new Captain(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(LT_ONE_RANK))
      piece = new LtOne(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(LT_TWO_RANK))
      piece = new LtTwo(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(SERGEANT_RANK))
      piece = new Sergeant(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(PRIVATE_RANK))
      piece = new Private(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(FLAG_RANK))
      piece = new Flag(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);
    else if (pieceRankName.contains(SPY_RANK))
      piece = new Spy(gog.incTotalPiecesCount(alliance), gog.getBoard(), owner, alliance);

    return piece;
  }

  /**
   *
   * @return tile column number on a 2D board
   */
  public static int getTileColNum(int id) {
    if (id <= TOTAL_BOARD_TILES - 1)
      return id % BOARD_TILES_COL_COUNT;

    return -1;
  }

  /**
   *
   * @return tile row number on a 2D board
   */
  public static int getTileRowNum(int id) {
    if (id <= TOTAL_BOARD_TILES - 1)
      return id / BOARD_TILES_COL_COUNT;

    return - 1;
  }

  /**
   * Compares two {@link Piece} equality.
   *
   * @return boolean true if both pieces are equal, else false.
   */
  public static boolean areTwoPiecesEqual(Piece pieceOne, Piece pieceTwo) {
    if (pieceOne.getRank().equals(pieceTwo.getRank()) &&
        pieceOne.getAlliance() == pieceTwo.getAlliance() &&
        pieceOne.getPowerLevel() == pieceTwo.getPowerLevel() &&
        pieceOne.getPieceOwner() == pieceTwo.getPieceOwner())
      return true;

    return false;
  }

  public static String getPieceImagePath(String alliance, String pieceRank) {
    return "pieces/original/" + alliance + "/" + pieceRank + ".png";
  }

  /**
   * Constructor method that ensures this {@link BoardUtils} class cannot be
   * instantiated.
   */
  private BoardUtils() {
    throw new RuntimeException("You cannot instantiate BoardUtils class");
  }
}
