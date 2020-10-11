package com.markl.game.ui.board;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.markl.game.ui.screen.GameScreen;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/07/2020.
 */
public class PieceUIListener extends ClickListener {

  public float alpha = 0.5f; // piece touch drag transparency

  private GameScreen gameScreen;
  private Camera camera;
  private LinkedList<TileUI> tiles;
  private PieceUI pieceUI;

  private Vector3 mousePos;
  private TileUI destTile;
  private TileUI origTile;
  boolean hasDestTile = false;

  public PieceUIListener(GameScreen gameScreen, PieceUI pieceUI) {
    this.gameScreen = gameScreen;
    this.pieceUI    = pieceUI;
    tiles           = gameScreen.tiles;
    camera          = gameScreen.app.camera;
    origTile        = pieceUI.tileUI;
  }

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
    pieceUI.getColor().a = alpha; // Make piece transparent
    gameScreen.origTile = origTile;
    return super.touchDown(event, x, y, pointer, button);
  }

  @Override
  public void touchDragged(InputEvent event, float x, float y, int pointer) {
    super.touchDragged(event, x, y, pointer);

    // Set click/touch position relative to world coordinates
    mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    camera.unproject(mousePos); // mousePos is now in world coordinates

    // Adjusted board borders
    float boardRightBorder = GameScreen.BOARD_WIDTH + GameScreen.BOARD_X_OFFSET;
    float boardLeftBorder = GameScreen.BOARD_X_OFFSET;
    float boardTopBorder = GameScreen.BOARD_HEIGHT + GameScreen.BOARD_Y_OFFSET;
    float boardBottomBorder = GameScreen.BOARD_Y_OFFSET;

    // Prevent piece from leaving the board
    if (mousePos.x + pieceUI.getWidth() * 0.5f > boardRightBorder)        // Right border stopper
      mousePos.x = boardRightBorder - pieceUI.getWidth() * 0.5f;
    else if (mousePos.x - pieceUI.getWidth() * 0.5f < boardLeftBorder)    // Left border stopper
      mousePos.x = GameScreen.BOARD_X_OFFSET + pieceUI.getWidth() * 0.5f;
    if (mousePos.y + pieceUI.getHeight() * 0.5f > boardTopBorder)         // Top border stopper
      mousePos.y = boardTopBorder - pieceUI.getHeight() * 0.5f;
    else if (mousePos.y - pieceUI.getHeight() * 0.5f < boardBottomBorder) // Bottom border stopper
      mousePos.y = boardBottomBorder + pieceUI.getHeight() * 0.5f;

    // add MoveToAction to piece actor
    MoveToAction mta = new MoveToAction();
    mta.setX(mousePos.x - pieceUI.getWidth() * 0.5f);
    mta.setY(mousePos.y - pieceUI.getHeight() * 0.5f);
    mta.setDuration(0.08f);
    pieceUI.addAction(mta);
    pieceUI.setZIndex(999); // Always on top of any pieces

    // Update snap line path
    hasDestTile = false;

    for (int i = 0; i < tiles.size(); i++) {
      TileUI tile = tiles.get(i);

      // Get center coords of tile
      final float tX  = GameScreen.TILE_SIZE * 0.5f + tile.x;
      final float tY  = GameScreen.TILE_SIZE * 0.5f + tile.y;
      // Get center coords of current piece
      final float pX  = pieceUI.getWidth() * 0.5f + pieceUI.getX();
      final float pY  = pieceUI.getWidth() * 0.5f + pieceUI.getY();
      // Get distance between tile and current piece
      final float dX  = Math.abs(tX - pX);
      final float dY  = Math.abs(tY - pY);
      final double dZ = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

      // Activate snap tile
      if (dZ <= GameScreen.TILE_SIZE * 0.5f) {
        gameScreen.tX = tX; gameScreen.tY = tY;
        gameScreen.pX = pX; gameScreen.pY = pY;

        // Update destination tile
        destTile = tile;
        gameScreen.destTile = tile;
        hasDestTile = true;
        break;
      }

      if (hasDestTile)
        break;
    }

    // Clear snap tile and dest highlight then Highlight origin tile
    if (!hasDestTile) {
      gameScreen.tX = -1;
      gameScreen.tY = -1;
      gameScreen.pX = -1;
      gameScreen.pY = -1;
      gameScreen.destTile = null;
      destTile = null;
    }
  }

  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    super.touchUp(event, x, y, pointer, button);

    int moveType = - 1;
    if (hasDestTile && destTile != null) {
      moveType = gameScreen.board.makeMove(origTile.id, destTile.id);
      hasDestTile = false;
    }

    if (moveType != -1) {
      if (moveType == 0) {
        gameScreen.removePieceUI(origTile.id);
        gameScreen.removePieceUI(destTile.id);
        System.out.println("DRAW");
      } else if (moveType == 1) {
        // add MoveToAction to piece actor
        MoveToAction mta = new MoveToAction();
        mta.setX(destTile.x);
        mta.setY(destTile.y);
        mta.setDuration(0.08f);
        pieceUI.addAction(mta);
        pieceUI.setZIndex(999); // Always on top of any pieces
        pieceUI.getColor().a = 1; // Remove transparency
        origTile = destTile;
        System.out.println("NORMAL");
      } else if (moveType == 2) {
        gameScreen.removePieceUI(destTile.id);
        // add MoveToAction to piece actor
        MoveToAction mta = new MoveToAction();
        mta.setX(destTile.x);
        mta.setY(destTile.y);
        mta.setDuration(0.08f);
        pieceUI.addAction(mta);
        pieceUI.setZIndex(999); // Always on top of any pieces
        pieceUI.getColor().a = 1; // Remove transparency
        origTile = destTile;
        System.out.println("AGGRESSIVE WIN");
      } else if (moveType == 3) {
        gameScreen.removePieceUI(origTile.id);
        System.out.println("AGGRESSIVE LOSE");
      }
    } else {
      // add MoveToAction to piece actor
      MoveToAction mta = new MoveToAction();
      mta.setX(origTile.x);
      mta.setY(origTile.y);
      mta.setDuration(0.08f);
      pieceUI.addAction(mta);
      pieceUI.setZIndex(999); // Always on top of any pieces
      pieceUI.getColor().a = 1; // Remove transparency
      System.out.println("INVALID");
    }

    // Clear snap tile and drag tile highlights
    gameScreen.tX = -1;
    gameScreen.tY = -1;
    gameScreen.pX = -1;
    gameScreen.pY = -1;
    gameScreen.origTile = null;
    gameScreen.destTile = null;
    destTile = null;

    System.out.println("");
    System.out.println(gameScreen.toString());
    System.out.println(gameScreen.board.toString());
  }
}
