package com.markl.game.control;

import static com.markl.game.util.Constants.PIECE_UI_ANIMATION_SPEED;

import java.util.Iterator;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.markl.game.engine.board.Alliance;
import com.markl.game.ui.board.PieceUI;
import com.markl.game.ui.board.TileUI;
import com.markl.game.ui.screen.GameScreen;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 11/03/2020.
 */
public class PieceUIManager {

  private GameScreen gameScreen;

  public PieceUIManager(GameScreen gameScreen) {
    this.gameScreen = gameScreen;
  }

  public boolean movePieceUI(int srcPieceUITileId, int tgtPieceUITileId) {
    TileUI srcTileUI = gameScreen.tilesUI.get(srcPieceUITileId);
    TileUI tgtTileUI = gameScreen.tilesUI.get(tgtPieceUITileId);
    if (srcTileUI.getPieceUI() != null && tgtTileUI.getPieceUI() == null) {
      // Make target TileUI the tile for source PieceUI
      animatePieceUIMove(srcTileUI.getPieceUI(), tgtTileUI.x, tgtTileUI.y, 1);
      tgtTileUI.setPieceUI(srcTileUI.getPieceUI());
      srcTileUI.clearPieceUI();
      return true;
    }
    return false;
  }

  public boolean removePieceUI(int tileId) {
    TileUI tileUI = gameScreen.tilesUI.get(tileId);
    if (tileUI.getPieceUI() != null) {
      tileUI.getPieceUI().addAction(Actions.removeActor());
      tileUI.clearPieceUI();
      return true;
    }
    return false;
  }

  public void swapPieceUI(int srcPieceUITileId, int tgtPieceUITileId) {
    TileUI srcTileUI = gameScreen.tilesUI.get(srcPieceUITileId);
    TileUI tgtTileUI = gameScreen.tilesUI.get(srcPieceUITileId);
    TileUI tmpTileUI = tgtTileUI;
    // Make target TileUI the tile for source PieceUI
    tgtTileUI.setPieceUI(srcTileUI.getPieceUI());
    srcTileUI.setPieceUI(tmpTileUI.getPieceUI());
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

  public void hidePieceUISet(Alliance alliance) {
    Iterator<TileUI> iterator = gameScreen.tilesUI.iterator();
    while (iterator.hasNext()) {
      TileUI tileUI = iterator.next();
      if (tileUI.isTileUIOccupied()) {
        PieceUI pieceUI = tileUI.getPieceUI();
        if (pieceUI.alliance == alliance)
          pieceUI.hidePieceDisplay();
        else
          pieceUI.showPieceDisplay();
      }
    }
  }

  public void showAllPieceUI() {
    Iterator<TileUI> iterator = gameScreen.tilesUI.iterator();
    while (iterator.hasNext()) {
      TileUI tileUI = iterator.next();
      if (tileUI.isTileUIOccupied()) {
        tileUI.getPieceUI().showPieceDisplay();;
      }
    }
  }

  public void flipPieceUISetDisplay() {
    Iterator<TileUI> iterator = gameScreen.tilesUI.iterator();
    while (iterator.hasNext()) {
      TileUI tileUI = iterator.next();
      if (tileUI.isTileUIOccupied()) {
        PieceUI pieceUI = tileUI.getPieceUI();
        if (pieceUI.isHidden)
          pieceUI.showPieceDisplay();
        else
          pieceUI.hidePieceDisplay();
      }
    }
  }
}
