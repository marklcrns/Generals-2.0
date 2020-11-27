package com.markl.game.control;

import com.badlogic.gdx.Gdx;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.Move;
import com.markl.game.engine.board.pieces.Piece;
import com.markl.game.ui.board.PieceUI;
import com.markl.game.ui.board.TileUI;
import com.markl.game.ui.screen.GameScreen;
import com.markl.game.ui.screen.GameScreen.GameMode;

/**
 * Controls movement of {@link Piece} in {@link Board} and Updates
 * {@link PieceUI}s of GameScreen.
 *
 * @author Mark Lucernas
 * Created on 10/28/2020.
 */
public class MoveManager {

  private GameScreen gameScreen;

  public MoveManager(GameScreen gameScreen) {
    this.gameScreen     = gameScreen;
  }

  public void makeMove(int srcTileId, int tgtTileId, boolean isUpdateServer) {
    final TileUI srcTileUI = gameScreen.tilesUI.get(srcTileId);

    final PieceUI srcPieceUI = srcTileUI.getPieceUI();
    final Move newMove = new Move(gameScreen.gameState.getCurrentTurnMaker(), gameScreen.board, srcTileId, tgtTileId);
    final int moveType = gameScreen.board.move(newMove);

    if (moveType != -1) {
      if (moveType == 0) {
        gameScreen.pieceUIManager.removePieceUI(srcTileId);
        gameScreen.pieceUIManager.removePieceUI(tgtTileId);
      } else if (moveType == 1) {
        gameScreen.pieceUIManager.movePieceUI(srcTileId, tgtTileId);
      } else if (moveType == 2) {
        gameScreen.pieceUIManager.removePieceUI(tgtTileId);
        gameScreen.pieceUIManager.movePieceUI(srcTileId, tgtTileId);
      } else if (moveType == 3) {
        // TODO animate aggressive lose on the other client
        gameScreen.pieceUIManager.removePieceUI(srcTileId);
      }

      gameScreen.activeTileUI = null; // Remove old origin TileUI highlight
      gameScreen.prevMove = newMove;

      Gdx.app.log("Move", "Update move by Player: " + newMove.getPlayer().getId());
      Gdx.app.log("Move", newMove.toString());

      if (gameScreen.gameMode == GameMode.ONLINE) {
        if (isUpdateServer)
          gameScreen.serverSocket.updateMove(newMove);
      } else {
        // gameScreen.pieceUIManager.flipPieceUISetDisplay();

        // Make AI Move
        if (gameScreen.gameState.isRunning()) {
          if (gameScreen.gameState.getCurrentTurnMaker().getAlliance() ==
              gameScreen.board.getAI().getAIAlliance())
          {
            Move aiMove = gameScreen.board.getAI().generateMove();
            makeMove(aiMove.getSrcTileId(), aiMove.getTgtTileId(), false);
          }
        }
      }
    } else {
      // Move piece back to original position
      gameScreen.pieceUIManager.animatePieceUIMove(srcPieceUI, srcTileUI.getX(), srcTileUI.getY(), 1);
    }

    if (!gameScreen.gameState.isRunning()) {
      gameScreen.pieceUIManager.showAllPieceUI();
    }
  }
}
