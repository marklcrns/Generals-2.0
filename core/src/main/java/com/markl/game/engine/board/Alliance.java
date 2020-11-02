package com.markl.game.engine.board;

/**
 * Enum class that holds the two opposing sides of the board game.
 * This class is responsible of separating friendly and hostile pieces, their
 * respective territories, and the alliance of the Players.
 *
 * Author: Mark Lucernas
 * Date: Sep 24, 2020
 */
public enum Alliance {
  WHITE("WHITE"),
  BLACK("BLACK");
  final String value;

  Alliance(final String value) {
    this.value = value;
  }

  public String getValue() { return this.value; }
}
