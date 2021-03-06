package com.markl.game.control;

import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Tile;
import com.markl.game.ui.board.PieceUI;
import com.markl.game.ui.board.PieceUIListener;
import com.markl.game.ui.board.TileUI;
import com.markl.game.ui.screen.GameScreen;
import com.markl.game.util.Constants;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 11/03/2020.
 */
public class PieceUIManager {

  private GameScreen gameScreen;
  public float pieceAnimationDuration = Constants.DEFAULT_PIECEUI_ANIMATION_DURATION;

  public PieceUIManager(GameScreen gameScreen) {
    this.gameScreen = gameScreen;
  }

  public boolean generatePieceUI(int tileId) {
    TileUI tileUI = getTileUI(tileId);
    AtlasRegion hiddenBlackPiece = gameScreen.blackPiecesTex.get("Hidden");
    AtlasRegion hiddenWhitePiece = gameScreen.whitePiecesTex.get("Hidden");

    // Make sure tile is occupied by Piece but missing PieceUI
    if (getBoardTile(tileId).isTileOccupied() && tileUI.isEmpty()) {
      Alliance alliance = getBoardTile(tileId).getPiece().getAlliance();
      String pieceRank = getBoardTile(tileId).getPiece().getRank();
      PieceUI pieceUI;

      if (alliance == Alliance.BLACK) {
        pieceUI = new PieceUI(tileUI, pieceRank, alliance);
        pieceUI.setPieceTexVisible(gameScreen.blackPiecesTex.get(pieceRank));
        pieceUI.setPieceTexHidden(hiddenBlackPiece);
        if (gameScreen.isBlackVisible)
          pieceUI.show();
      } else {
        pieceUI = new PieceUI(tileUI, pieceRank, alliance);
        pieceUI.setPieceTexVisible(gameScreen.whitePiecesTex.get(pieceRank));
        pieceUI.setPieceTexHidden(hiddenWhitePiece);
        if (gameScreen.isWhiteVisible)
          pieceUI.show();
      }

      pieceUI.setWidth(tileUI.width);
      pieceUI.setHeight(tileUI.height);
      pieceUI.setPosition(tileUI.x, tileUI.y);
      pieceUI.addListener(new PieceUIListener(pieceUI, gameScreen));

      gameScreen.stage.addActor(pieceUI);
      tileUI.setPieceUI(pieceUI);
      return true;
    }
    return false;
  }

  public boolean pieceUIFollow(int srcPieceUITileId, int tgtPieceUITileId) {
    TileUI srcTileUI = getTileUI(srcPieceUITileId);
    TileUI tgtTileUI = getTileUI(tgtPieceUITileId);
    if (srcTileUI.isOccupied() && tgtTileUI.isEmpty()) {
      // Make target TileUI the tile for source PieceUI
      animateFollow(srcTileUI.getPieceUI(), tgtTileUI.x, tgtTileUI.y, 1);
      tgtTileUI.setPieceUI(srcTileUI.getPieceUI());
      srcTileUI.clearPieceUI();
      return true;
    }
    return false;
  }

  public boolean movePieceUI(int srcPieceUITileId, int tgtPieceUITileId) {
    TileUI srcTileUI = getTileUI(srcPieceUITileId);
    TileUI tgtTileUI = getTileUI(tgtPieceUITileId);
    if (srcTileUI.isOccupied() && tgtTileUI.isEmpty()) {
      // Make target TileUI the tile for source PieceUI
      animateMove(srcTileUI.getPieceUI(), tgtTileUI.x, tgtTileUI.y, 1);
      tgtTileUI.setPieceUI(srcTileUI.getPieceUI());
      srcTileUI.clearPieceUI();
      return true;
    }
    return false;
  }

  public boolean relocatePieceUI(int srcPieceUITileId, int tgtPieceUITileId) {
    TileUI srcTileUI = getTileUI(srcPieceUITileId);
    TileUI tgtTileUI = getTileUI(tgtPieceUITileId);
    if (srcTileUI.isOccupied() && tgtTileUI.isEmpty()) {
      // Make target TileUI the tile for source PieceUI
      animateFollow(srcTileUI.getPieceUI(), tgtTileUI.x, tgtTileUI.y, 1);
      tgtTileUI.setPieceUI(srcTileUI.getPieceUI());
      srcTileUI.clearPieceUI();
      return true;
    }
    return false;
  }

  public boolean removePieceUI(int tileId) {
    TileUI tileUI = getTileUI(tileId);
    if (tileUI.isOccupied()) {
      tileUI.getPieceUI().addAction(
          Actions.sequence(Actions.fadeOut(pieceAnimationDuration * 4, Interpolation.pow3Out), Actions.removeActor()));
      tileUI.clearPieceUI();
      return true;
    }
    return false;
  }

  public void swapPieceUI(int srcPieceUITileId, int tgtPieceUITileId) {
    TileUI srcTileUI = getTileUI(srcPieceUITileId);
    TileUI tgtTileUI = getTileUI(tgtPieceUITileId);
    TileUI tmpTileUI = tgtTileUI;
    // Make target TileUI the tile for source PieceUI
    tgtTileUI.setPieceUI(srcTileUI.getPieceUI());
    srcTileUI.setPieceUI(tmpTileUI.getPieceUI());
  }

  public void animateMove(PieceUI pieceUI, float destX, float destY, float alpha) {
    MoveToAction mtaTravelHalf = new MoveToAction();
    mtaTravelHalf.setX((destX + pieceUI.getX()) / 2);
    mtaTravelHalf.setY((destY + pieceUI.getY()) / 2);
    mtaTravelHalf.setDuration(pieceAnimationDuration * 2);

    MoveToAction mtaTravel = new MoveToAction();
    mtaTravel.setX(destX);
    mtaTravel.setY(destY);
    mtaTravel.setDuration(pieceAnimationDuration);

    pieceUI.addAction(Actions.sequence(
          Actions.parallel(
            mtaTravelHalf,
            Actions.sizeTo(pieceUI.getWidth() + 10, pieceUI.getHeight() + 10, pieceAnimationDuration)),
          Actions.parallel(
            mtaTravel,
            Actions.sizeTo(pieceUI.getWidth(), pieceUI.getHeight(), pieceAnimationDuration / 2)))
        );

    pieceUI.setZIndex(999);   // Always on top of any pieces
    pieceUI.getColor().a = alpha; // Remove transparency
  }

  public void animateFollow(PieceUI pieceUI, float destX, float destY, float alpha) {
    MoveToAction mtaTravel = new MoveToAction();
    mtaTravel.setX(destX);
    mtaTravel.setY(destY);
    mtaTravel.setDuration(0.1f);
    pieceUI.addAction(mtaTravel);

    pieceUI.setZIndex(999);   // Always on top of any pieces
    pieceUI.getColor().a = alpha; // Remove transparency
  }

  public void animateRelapse(PieceUI pieceUI, float alpha) {
    // Cancel all actions
    pieceUI.clearActions();

    TileUI tileUI = pieceUI.tileUI;

    MoveToAction mtaTravel = new MoveToAction();
    mtaTravel.setX(tileUI.getX());
    mtaTravel.setY(tileUI.getY());
    mtaTravel.setDuration(0.02f);
    pieceUI.addAction(mtaTravel);

    pieceUI.setZIndex(999);   // Always on top of any pieces
    pieceUI.getColor().a = alpha; // Remove transparency
  }

  public void hidePieceUIAlliance(Alliance alliance) {
    Iterator<TileUI> iterator = gameScreen.tilesUI.iterator();
    while (iterator.hasNext()) {
      TileUI tileUI = iterator.next();
      if (tileUI.isOccupied()) {
        PieceUI pieceUI = tileUI.getPieceUI();
        if (pieceUI.alliance == alliance)
          pieceUI.hide();
        else
          pieceUI.show();
      }
    }
  }

  public void showAllPieceUI() {
    Iterator<TileUI> iterator = gameScreen.tilesUI.iterator();
    while (iterator.hasNext()) {
      TileUI tileUI = iterator.next();
      if (tileUI.isOccupied()) {
        tileUI.getPieceUI().show();
      }
    }
  }

  public void hideAllPieceUI() {
    Iterator<TileUI> iterator = gameScreen.tilesUI.iterator();
    while (iterator.hasNext()) {
      TileUI tileUI = iterator.next();
      if (tileUI.isOccupied()) {
        tileUI.getPieceUI().hide();
      }
    }
  }

  public void hideMyPieceUI() {
    Iterator<TileUI> iterator = gameScreen.tilesUI.iterator();
    while (iterator.hasNext()) {
      TileUI tileUI = iterator.next();
      if (tileUI.isOccupied()) {
        boolean isMyPiece = gameScreen.gog.getMyPlayer().getAlliance() == tileUI.getPieceUI().alliance;
        if (isMyPiece) tileUI.getPieceUI().hide();
      }
    }
  }

  public void hideEnemyPieceUI() {
    Iterator<TileUI> iterator = gameScreen.tilesUI.iterator();
    while (iterator.hasNext()) {
      TileUI tileUI = iterator.next();
      if (tileUI.isOccupied()) {
        boolean isEnemyPiece = gameScreen.gog.getEnemyPlayer().getAlliance() == tileUI.getPieceUI().alliance;
        if (isEnemyPiece) tileUI.getPieceUI().hide();
      }
    }
  }

  public void toggleAllPieceUI() {
    Iterator<TileUI> iterator = gameScreen.tilesUI.iterator();
    while (iterator.hasNext()) {
      TileUI tileUI = iterator.next();
      if (tileUI.isOccupied()) {
        PieceUI pieceUI = tileUI.getPieceUI();
        if (pieceUI.isVisible)
          pieceUI.hide();
        else
          pieceUI.show();
      }
    }
  }

  public Tile getBoardTile(int tileId) { return gameScreen.board.getTile(tileId); }
  public TileUI getTileUI(int tileId)  { return gameScreen.tilesUI.get(tileId); }

  public void setPieceAnimationDuration(float duration) { this.pieceAnimationDuration = duration; }
}
