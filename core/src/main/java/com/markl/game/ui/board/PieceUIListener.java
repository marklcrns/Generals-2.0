package com.markl.game.ui.board;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
    pieceUI.getColor().a = 0.3f; // Make piece transparent
    return super.touchDown(event, x, y, pointer, button);
  }

  @Override
  public void touchDragged(InputEvent event, float x, float y, int pointer) {
    super.touchDragged(event, x, y, pointer);

    // Set click/touch position relative to world coordinates
    mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    camera.unproject(mousePos); // mousePos is now in world coordinates

    // Prevent from leaving the board
    if (mousePos.x + pieceUI.getWidth() * 0.5f > GameScreen.BOARD_WIDTH)
      mousePos.x = GameScreen.BOARD_WIDTH - pieceUI.getWidth() * 0.5f;
    else if (mousePos.x - pieceUI.getWidth() * 0.5f < 0)
      mousePos.x = pieceUI.getWidth() * 0.5f;
    if (mousePos.y + pieceUI.getHeight() * 0.5f > GameScreen.BOARD_HEIGHT)
      mousePos.y = GameScreen.BOARD_HEIGHT - pieceUI.getHeight() * 0.5f;
    else if (mousePos.y - pieceUI.getHeight() * 0.5f < 0)
      mousePos.y = pieceUI.getHeight() * 0.5f;

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
        gameScreen.origTile = null;
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
      gameScreen.origTile = origTile;
      destTile = null;
    }
  }

  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    super.touchUp(event, x, y, pointer, button);

    float toX = origTile.x;
    float toY = origTile.y;

    if (hasDestTile && destTile != null) {
      toX = destTile.x;
      toY = destTile.y;

      System.out.println("origin = " + origTile.toString());
      System.out.println("destination = " + destTile.toString());

      if (gameScreen.movePieceUI(origTile.id, destTile.id))
        origTile = destTile;
      else {
        // Move back to origin tile if error
        toX = origTile.x; toY = origTile.y;
        System.out.println("\nMOVE ERROR\n");
      }

      hasDestTile = false;
    }

    // add MoveToAction to piece actor
    MoveToAction mta = new MoveToAction();
    mta.setX(toX);
    mta.setY(toY);
    mta.setDuration(0.08f);
    pieceUI.addAction(mta);
    pieceUI.setZIndex(999); // Always on top of any pieces
    pieceUI.getColor().a = 1; // Remove transparency

    // Clear snap tile and drag tile highlights
    gameScreen.tX = -1;
    gameScreen.tY = -1;
    gameScreen.pX = -1;
    gameScreen.pY = -1;
    gameScreen.origTile = null;
    gameScreen.destTile = null;
    destTile = null;


    System.out.println(gameScreen.board.toString());
  }
}
