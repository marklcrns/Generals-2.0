package com.markl.game.ai.minimax;

import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.Move;

/**
 * Abstract class for AI algorithms
 *
 * @author Mark Lucernas
 * Created on 11/26/2020.
 */
public abstract class AI {

  protected Board board;
  protected Alliance aiAlliance;

  public AI() {}

  public void setBoard(Board board)              { this.board = board; }
  public void setAIAlliance(Alliance aiAlliance) { this.aiAlliance = aiAlliance; }
  public Alliance getAIAlliance()                { return this.aiAlliance; }

  public abstract Move generateMove();
}
