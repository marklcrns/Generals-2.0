package com.markl.game.ai.minimax;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.markl.game.Gog;
import com.markl.game.engine.board.Move;
import com.markl.game.engine.board.Tile;
import com.markl.game.engine.board.pieces.Piece;
import com.markl.game.ui.screen.GameScreen;

/**
 * Single player AI using minimax algorithm
 *
 * @author Mark Lucernas
 * Created on 11/25/2020.
 */
public class AIMinimax extends AI {

  private int nodeCount = 0;
  private int depth;
  private GameScreen gameScreen;

  public AIMinimax(int depth, GameScreen gameScreen) {
    this.depth = depth;
    this.gameScreen = gameScreen;
  }

  public int evaluateBoard() {
    int score = 0;
    Iterator<Tile> iter = gog.getBoard().getAllTiles().iterator();

    // Evaluate ally pieces rank
    while (iter.hasNext()) {
      Tile tile = iter.next();
      if (tile.isTileOccupied()) {
        Piece piece = tile.getPiece();
        if (piece.getAlliance() == gog.getCurrTurnMaker())
          score += piece.getPowerLevel();
      }
    }

    // Evaluate enemy pieces count
    return score;
  }

  public Move minimaxRoot(Gog gog, GameScreen gs, int depth, boolean isMaximizing) {
    this.nodeCount = 0;
    int bestScore = -9999;
    Move bestMove = null;

    List<Move> legalMoves = gog.getBoard().getLegalMoves();
    Gdx.app.log("AIMinimax", "legalMoves: " + legalMoves.size());

    for (int i = 0; i < legalMoves.size(); i++) {
      this.nodeCount++;
      Move nextMove = legalMoves.get(i);
      Gdx.app.log("AiMinimax", nextMove.toString());
      // Gdx.app.log("AiMinimax", gog.getBoard().ascii());
      gs.moveManager.makeMove(nextMove.getSrcTileId(), nextMove.getTgtTileId(), false, false);
      if (gog.isRunning()) {
        int value = minimax(gog, gs, depth - 1, isMaximizing);
        if (value >= bestScore) {
          bestScore = value;
          bestMove = nextMove;
        }
      }
      gs.moveManager.undoLastMove();
    }

    // TODO: If null, return random legal move //
    return bestMove;
  }

  public int minimax(Gog gog, GameScreen gs, int depth, boolean isMaximizing) {
    float delay = 0.1f;
    Timer.schedule(new Task() {
      @Override
      public void run() {}
    }, delay);

    Gdx.app.log("AIMinimax", "Depth: " + depth);

    if (depth == 0)
      return -evaluateBoard();

    List<Move> legalMoves = gog.getBoard().getLegalMoves();

    if (isMaximizing) {
      int max = -9999;
      for (int i = 0; i < legalMoves.size(); i++) {
        this.nodeCount++;
        Move nextMove = legalMoves.get(i);
        Gdx.app.log("AiMinimax", nextMove.toString());
        gs.moveManager.makeMove(nextMove.getSrcTileId(), nextMove.getTgtTileId(), false, false);
        if (gog.isRunning()) {
          max = Math.max(max, minimax(gog, gs, depth - 1, !isMaximizing));
        }
        gs.moveManager.undoLastMove();
      }
      return max;
    } else {
      int min = 9999;
      for (int i = 0; i < legalMoves.size(); i++) {
        this.nodeCount++;
        Move nextMove = legalMoves.get(i);
        Gdx.app.log("AiMinimax", nextMove.toString());
        gs.moveManager.makeMove(nextMove.getSrcTileId(), nextMove.getTgtTileId(), false, false);
        if (gog.isRunning()) {
          min = Math.min(min, minimax(gog, gs, depth - 1, isMaximizing));
        }
        gs.moveManager.undoLastMove();
      }
      return min;
    }
  }

  @Override
  public Move generateMove() {
    Gdx.app.log("AiMinimax", "Node Count: " + this.nodeCount);
    return minimaxRoot(gog, gameScreen, this.depth, true);
  }
}
