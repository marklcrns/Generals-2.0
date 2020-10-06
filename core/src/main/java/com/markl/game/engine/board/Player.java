package com.markl.game.engine.board;

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

}
