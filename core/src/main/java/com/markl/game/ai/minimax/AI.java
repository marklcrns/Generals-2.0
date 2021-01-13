package com.markl.game.ai.minimax;

import com.markl.game.Gog;
import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Move;

/**
 * Abstract class for AI algorithms
 *
 * @author Mark Lucernas
 * Created on 11/26/2020.
 */
public abstract class AI {

  protected Gog gog;
  protected Alliance aiAlliance;

  public AI() {}

  public void setGog(Gog gog)                    { this.gog = gog; }
  public void setAIAlliance(Alliance aiAlliance) { this.aiAlliance = aiAlliance; }
  public Alliance getAIAlliance()                { return this.aiAlliance; }

  public abstract Move generateMove();
}
