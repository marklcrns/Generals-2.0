package com.markl.game.control;

import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.markl.game.engine.board.Alliance;
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
		TileUI tileUI = gameScreen.tilesUI.get(tileId);
		AtlasRegion hiddenBlackPiece = gameScreen.blackPiecesTex.get("Hidden");
		AtlasRegion hiddenWhitePiece = gameScreen.whitePiecesTex.get("Hidden");

		// Make sure tile is occupied by Piece but missing PieceUI
		if (gameScreen.board.getTile(tileUI.getTileId()).isTileOccupied() &&
				tileUI.getPieceUI() == null) {
			Alliance alliance = gameScreen.board.getTile(tileUI.getTileId()).getPiece().getAlliance();
			String pieceRank = gameScreen.board.getTile(tileUI.getTileId()).getPiece().getRank();

			PieceUI pieceUI;
			if (alliance == Alliance.BLACK) {
				pieceUI = new PieceUI(tileUI, pieceRank, alliance,
						gameScreen.blackPiecesTex.get(pieceRank), hiddenBlackPiece);
			} else {
				pieceUI = new PieceUI(tileUI, pieceRank, alliance,
						gameScreen.whitePiecesTex.get(pieceRank), hiddenWhitePiece);
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
		TileUI srcTileUI = gameScreen.tilesUI.get(srcPieceUITileId);
		TileUI tgtTileUI = gameScreen.tilesUI.get(tgtPieceUITileId);
		if (srcTileUI.getPieceUI() != null && tgtTileUI.getPieceUI() == null) {
			// Make target TileUI the tile for source PieceUI
			animateFollow(srcTileUI.getPieceUI(), tgtTileUI.x, tgtTileUI.y, 1);
			tgtTileUI.setPieceUI(srcTileUI.getPieceUI());
			srcTileUI.clearPieceUI();
			return true;
		}
		return false;
	}

	public boolean movePieceUI(int srcPieceUITileId, int tgtPieceUITileId) {
		TileUI srcTileUI = gameScreen.tilesUI.get(srcPieceUITileId);
		TileUI tgtTileUI = gameScreen.tilesUI.get(tgtPieceUITileId);
		if (srcTileUI.getPieceUI() != null && tgtTileUI.getPieceUI() == null) {
			// Make target TileUI the tile for source PieceUI
			animateMove(srcTileUI.getPieceUI(), tgtTileUI.x, tgtTileUI.y, 1);
			tgtTileUI.setPieceUI(srcTileUI.getPieceUI());
			srcTileUI.clearPieceUI();
			return true;
		}
		return false;
	}

	public boolean removePieceUI(int tileId) {
		TileUI tileUI = gameScreen.tilesUI.get(tileId);
		if (tileUI.getPieceUI() != null) {
			tileUI.getPieceUI().addAction(
					Actions.sequence(Actions.fadeOut(pieceAnimationDuration), Actions.removeActor()));
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

	public void animateMove(PieceUI pieceUI, float destX, float destY, float alpha) {
		MoveToAction mtaLift = new MoveToAction();
		mtaLift.setX(pieceUI.getX());
		mtaLift.setY(pieceUI.getY() + 10);
		mtaLift.setDuration(pieceAnimationDuration);

		MoveToAction mtaTravel = new MoveToAction();
		mtaTravel.setX(destX);
		mtaTravel.setY(destY);
		mtaTravel.setDuration(pieceAnimationDuration);

		pieceUI.addAction(Actions.sequence(
					Actions.parallel(
						mtaLift,
						Actions.sizeTo(pieceUI.getWidth() + 10, pieceUI.getHeight() + 10, pieceAnimationDuration)),
					Actions.parallel(
						mtaTravel,
						Actions.sizeTo(pieceUI.getWidth(), pieceUI.getHeight(), pieceAnimationDuration)))
				);

		pieceUI.setZIndex(999);   // Always on top of any pieces
		pieceUI.getColor().a = alpha; // Remove transparency
	}

	public void animateFollow(PieceUI pieceUI, float destX, float destY, float alpha) {
		MoveToAction mtaTravel = new MoveToAction();
		mtaTravel.setX(destX);
		mtaTravel.setY(destY);
		mtaTravel.setDuration(0.02f);
		pieceUI.addAction(mtaTravel);

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

	public void setPieceAnimationDuration(float duration) {
		this.pieceAnimationDuration = duration;
	}
}
