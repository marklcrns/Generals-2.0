package com.markl.game.control;

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

  public void makeMove(int srcTileId, int tgtTileId, boolean isUpdateServer,
                       boolean isAiMove, boolean isAnimate) {
    gameScreen.gog.clearMoveHistoryForward();

    final TileUI srcTileUI = gameScreen.tilesUI.get(srcTileId);

    final PieceUI srcPieceUI = srcTileUI.getPieceUI();
    final Move newMove = new Move(gameScreen.gog.getCurrTurnMakerPlayer(), gameScreen.board, srcTileId, tgtTileId);
    final int moveType = gameScreen.board.move(newMove, false);

    if (moveType != -1) {

      if (isAnimate) {
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
      }

      // TODO: Delete later //
      // Gdx.app.log(this.getClas().getName(), gameScreen.gog.printMoveHistory());

      if (gameScreen.gameMode == GameMode.ONLINE) {
        if (isUpdateServer)
          gameScreen.serverSocket.updateMove(newMove);
      } else {
        // Make AI Move
        if (gameScreen.gog.isPlaying() && isAiMove) {
          if (gameScreen.gog.getCurrTurnMakerPlayer().getAlliance() ==
              gameScreen.gog.getAI().getAIAlliance()) {
            Move aiMove = gameScreen.gog.getAI().generateMove();
            if (aiMove != null) // Is this necessary?
              makeMove(aiMove.getSrcTileId(), aiMove.getTgtTileId(), false, false, true);
              }
        }
      }
    } else {
      // Move piece back to original position
      gameScreen.pieceUIManager.animateRelapse(srcPieceUI, 1);
    }

    if (gameScreen.gog.isGameOver()) {
      gameScreen.pieceUIManager.showAllPieceUI();
    }
  }

  public boolean undoLastMove(boolean isAnimate) {
    Move lastMove = gameScreen.gog.undoMove();
    if (lastMove == null)
      return false;

    int moveType = lastMove.getMoveType().getValue();
    int srcTileId = lastMove.getSrcTileId();
    int tgtTileId = lastMove.getTgtTileId();

    if (moveType != -1 && isAnimate) {
      if (moveType == 0) {
        gameScreen.pieceUIManager.generatePieceUI(srcTileId);
        gameScreen.pieceUIManager.generatePieceUI(tgtTileId);
        gameScreen.tilesUI.get(tgtTileId).getPieceUI().hide();
      } else if (moveType == 1) {
        gameScreen.pieceUIManager.movePieceUI(tgtTileId, srcTileId);
      } else if (moveType == 2) {
        gameScreen.pieceUIManager.movePieceUI(tgtTileId, srcTileId);
        gameScreen.pieceUIManager.generatePieceUI(tgtTileId);
        gameScreen.tilesUI.get(tgtTileId).getPieceUI().hide();
      } else if (moveType == 3) {
        gameScreen.pieceUIManager.generatePieceUI(srcTileId);
      }

      // TODO: Delete later //
      // Gdx.app.log(this.getClas().getName(), "" + gameScreen.gog.printMoveHistory());

      // Remove old origin TileUI highlight
      gameScreen.activeTileUI = null;
      gameScreen.activeSrcPiece = null;
      // Assign prior move to undoMove into prevMove
      gameScreen.prevMove = gameScreen.gog.getMoveHistory().get(lastMove.getTurnId() - 1);

      if (gameScreen.gog.isPlaying()) {
        gameScreen.pieceUIManager.hideEnemyPieceUI();
      }
    }


    return true;
  }

  public boolean redoNextMove(boolean isAnimate) {
    Move nextMove = gameScreen.gog.redoMove();
    if (nextMove == null)
      return false;

    int srcTileId = nextMove.getSrcTileId();
    int tgtTileId = nextMove.getTgtTileId();
    final TileUI srcTileUI = gameScreen.tilesUI.get(srcTileId);

    final int moveType = gameScreen.board.move(nextMove, true);

    if (moveType != -1 && isAnimate) {
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
      gameScreen.prevMove = nextMove;

      // TODO: Delete later //
      // Gdx.app.log(this.getClas().getName(), "" + gameScreen.gog.printMoveHistory());

      if (gameScreen.gog.isGameOver()) {
        gameScreen.pieceUIManager.showAllPieceUI();
      }
    }
    return true;
  }

  public void relocate(int srcTileId, int tgtTileId, boolean isUpdateServer,
                       boolean isAnimate) {

    final TileUI srcTileUI = gameScreen.tilesUI.get(srcTileId);
    final PieceUI srcPieceUI = srcTileUI.getPieceUI();

    if (gameScreen.board.movePiece(srcTileId, tgtTileId)) {
      if (isAnimate) {
        gameScreen.pieceUIManager.relocatePieceUI(srcTileId, tgtTileId);
      }
    } else {
      gameScreen.pieceUIManager.animateRelapse(srcPieceUI, 1);
    }

    gameScreen.activeTileUI = null; // Remove old origin TileUI highlight

    // TODO: Update server //
    // if (gameScreen.gameMode == GameMode.ONLINE) {
    //  if (isUpdateServer)
    //    gameScreen.serverSocket.updateMove(newMove);
    // }

  }

}
