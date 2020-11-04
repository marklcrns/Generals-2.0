package com.markl.game.engine.board;

import com.markl.game.engine.board.pieces.Piece;

/**
 * @author Mark Lucernas
 * Date: Sep 19, 2020
 */
public class Player {

  private Alliance alliance;

  public Player() {}

  public Player(Alliance alliance) {
    this.alliance = alliance;
  }

  public Alliance getAlliance() {
    return this.alliance;
  }

  public boolean isCurrentTurnMaker() {
    return false;
  }

  /**
   * Return check if {@link Piece} is owned by Player
   *
   * @param piece Piece to check for owner
   *
   * @return True if piece is owned or has the same alliance as the Player
   */
  public boolean isMyPiece(Piece piece) {
    if (piece.getAlliance() == alliance)
      return true;
    return false;
  }
}
