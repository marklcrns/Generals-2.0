package com.markl.game.control;

import com.badlogic.gdx.Gdx;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.Move;
import com.markl.game.engine.board.pieces.Piece;
import com.markl.game.ui.board.PieceUI;
import com.markl.game.ui.board.TileUI;
import com.markl.game.ui.screen.GameScreen;

/**
 * Controls movement of {@link Piece} in {@link Board} and Updates
 * {@link PieceUI}s of GameScreen.
 *
 * @author Mark Lucernas
 * Created on 10/28/2020.
 */
public class MoveManager {

  private GameScreen gameScreen;
  private boolean isOnline;

  public MoveManager(GameScreen gameScreen, boolean isOnline) {
    this.gameScreen     = gameScreen;
    this.isOnline       = isOnline;
  }

  public void makeMove(TileUI srcTileUI, TileUI tgtTileUI, boolean isUpdateServer) {
    final int srcTileUIId = srcTileUI.getTileId();
    final int tgtTileUIId = tgtTileUI.getTileId();
    final PieceUI srcPieceUI = srcTileUI.getPieceUI();
    final Move newMove = new Move(gameScreen.gameState.getCurrentTurnMaker(), gameScreen.board, srcTileUIId, tgtTileUIId);
    final int moveType = gameScreen.board.move(newMove);

    if (moveType != -1) {
      if (moveType == 0) {
        gameScreen.pieceUIManager.removePieceUI(srcTileUIId);
        gameScreen.pieceUIManager.removePieceUI(tgtTileUIId);
      } else if (moveType == 1) {
        gameScreen.pieceUIManager.movePieceUI(srcTileUIId, tgtTileUIId);
      } else if (moveType == 2) {
        gameScreen.pieceUIManager.removePieceUI(tgtTileUIId);
        gameScreen.pieceUIManager.movePieceUI(srcTileUIId, tgtTileUIId);
      } else if (moveType == 3) {
        // TODO animate aggressive lose on the other client
        gameScreen.pieceUIManager.removePieceUI(srcTileUIId);
      }

      gameScreen.activeTileUI = null; // Remove old origin TileUI highlight

      if (isOnline && isUpdateServer) {
        gameScreen.serverSocket.updateMove(newMove);
      }
      Gdx.app.log("Move", "Update move by Player: " + newMove.getPlayer().getId());
    } else {
      // Move piece back to original position
      gameScreen.pieceUIManager.animatePieceUIMove(srcPieceUI, srcTileUI.getX(), srcTileUI.getY(), 1);
    }
  }
}
