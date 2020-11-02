package com.markl.game.control;

import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.markl.game.GameState;
import com.markl.game.engine.board.Board;
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

  private float PIECE_UI_ANIMATION_SPEED = 0.08f;

  private GameState gameState;
  private Board board;
  private GameScreen gameScreen;

  public MoveManager(GameState gameState, Board board, GameScreen gameScreen) {
    this.gameState = gameState;
    this.board = board;
    this.gameScreen = gameScreen;
  }

  public void makeMove(TileUI srcTileUI, TileUI tgtTileUI, boolean isUpdateServer) {
    final PieceUI srcPieceUI = srcTileUI.getPieceUI();
    final int srcTileUIId = srcTileUI.getTileId();
    final int tgtTileUIId = tgtTileUI.getTileId();
    final int moveType = board.makeMove(srcTileUIId, tgtTileUIId);

    if (moveType != -1) {
      if (moveType == 0) {
        gameScreen.removePieceUI(srcTileUIId);
        gameScreen.removePieceUI(tgtTileUIId);
      } else if (moveType == 1) {
        gameScreen.movePieceUI(srcTileUIId, tgtTileUIId);
        animatePieceUIMove(srcPieceUI, tgtTileUI.x, tgtTileUI.y, 1);
      } else if (moveType == 2) {
        gameScreen.removePieceUI(tgtTileUIId);
        gameScreen.movePieceUI(srcTileUIId, tgtTileUIId);
        animatePieceUIMove(srcPieceUI, tgtTileUI.x, tgtTileUI.y, 1);
      } else if (moveType == 3) {
        gameScreen.removePieceUI(srcTileUIId);
      }

      // TODO: Fix srcTileUI not changing reference. Make PieceUIListener store
      // field members in PieceUI.
      gameScreen.activeTileUI = null; // Remove old origin TileUI highlight
      if (isUpdateServer)
        gameScreen.serverSocket.updateMove(gameState.getCurrTurn(), srcTileUIId, tgtTileUIId);
    } else {
      animatePieceUIMove(srcPieceUI, srcTileUI.x, srcTileUI.y, 1);
    }
  }

  public void animatePieceUIMove(PieceUI pieceUI, float destX, float destY, float alpha) {
    MoveToAction mta = new MoveToAction();
    mta.setX(destX);
    mta.setY(destY);
    mta.setDuration(PIECE_UI_ANIMATION_SPEED);
    pieceUI.addAction(mta);
    pieceUI.setZIndex(999);   // Always on top of any pieces
    pieceUI.getColor().a = alpha; // Remove transparency
  }
}
