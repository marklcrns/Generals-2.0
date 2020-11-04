package com.markl.game.control;

import com.markl.game.GameState;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.Move;
import com.markl.game.engine.board.pieces.Piece;
import com.markl.game.network.ServerSocket;
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
  private Board board;
  private GameState gameState;
  private ServerSocket serverSocket;
  private PieceUIManager pieceUIManager;

  public MoveManager(GameScreen gameScreen) {
    this.gameScreen     = gameScreen;
    this.board          = gameScreen.board;
    this.gameState      = gameScreen.gameState;
    this.serverSocket   = gameScreen.serverSocket;
    this.pieceUIManager = gameScreen.pieceUIManager;
  }

  public void makeMove(TileUI srcTileUI, TileUI tgtTileUI, boolean isUpdateServer) {
    final int srcTileUIId = srcTileUI.getTileId();
    final int tgtTileUIId = tgtTileUI.getTileId();
    final PieceUI srcPieceUI = srcTileUI.getPieceUI();
    final Move newMove = new Move(gameState.getCurrentTurnMaker(), board, srcTileUIId, tgtTileUIId);
    final int moveType = board.makeMove(newMove);

    if (moveType != -1) {
      if (moveType == 0) {
        pieceUIManager.removePieceUI(srcTileUIId);
        pieceUIManager.removePieceUI(tgtTileUIId);
      } else if (moveType == 1) {
        pieceUIManager.movePieceUI(srcTileUIId, tgtTileUIId);
      } else if (moveType == 2) {
        pieceUIManager.removePieceUI(tgtTileUIId);
        pieceUIManager.movePieceUI(srcTileUIId, tgtTileUIId);
      } else if (moveType == 3) {
        // TODO animate aggressive lose on the other client
        pieceUIManager.removePieceUI(srcTileUIId);
      }

      gameScreen.activeTileUI = null; // Remove old origin TileUI highlight
      if (isUpdateServer)
        serverSocket.updateMove(gameState.getCurrTurn(), srcTileUIId, tgtTileUIId);
    } else {
      pieceUIManager.animatePieceUIMove(srcPieceUI, srcTileUI.getX(), srcTileUI.getY(), 1);
    }
  }
}