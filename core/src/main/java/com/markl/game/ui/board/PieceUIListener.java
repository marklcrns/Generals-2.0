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
  private PieceUI piece;
  private Camera camera;
  private LinkedList<TileUI> tiles;

  private Vector3 mousePos;
  private TileUI destTile;
  private TileUI origTile;
  boolean hasDestTile = false;

  public PieceUIListener(GameScreen gameScreen, PieceUI piece, Camera camera) {
    this.gameScreen = gameScreen;
    this.piece = piece;
    this.camera = camera;
    this.tiles = gameScreen.tiles;
  }

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
    origTile = piece.tile;
    piece.getColor().a = 0.3f; // Make piece transparent
    return super.touchDown(event, x, y, pointer, button);
  }

  @Override
  public void touchDragged(InputEvent event, float x, float y, int pointer) {
    super.touchDragged(event, x, y, pointer);

    // Set click/touch position relative to world coordinates
    mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    camera.unproject(mousePos); // mousePos is now in world coordinates

    // Prevent from leaving the board
    if (mousePos.x + piece.getWidth() * 0.5f > GameScreen.BOARD_WIDTH)
      mousePos.x = GameScreen.BOARD_WIDTH - piece.getWidth() * 0.5f;
    else if (mousePos.x - piece.getWidth() * 0.5f < 0)
      mousePos.x = piece.getWidth() * 0.5f;
    if (mousePos.y + piece.getHeight() * 0.5f > GameScreen.BOARD_HEIGHT)
      mousePos.y = GameScreen.BOARD_HEIGHT - piece.getHeight() * 0.5f;
    else if (mousePos.y - piece.getHeight() * 0.5f < 0)
      mousePos.y = piece.getHeight() * 0.5f;

    // add MoveToAction to piece actor
    MoveToAction mta = new MoveToAction();
    mta.setX(mousePos.x - piece.getWidth() * 0.5f);
    mta.setY(mousePos.y - piece.getHeight() * 0.5f);
    mta.setDuration(0.08f);
    piece.addAction(mta);
    piece.setZIndex(999); // Always on top of any pieces

    // Update snap line path
    hasDestTile = false;

    for (int i = 0; i < tiles.size(); i++) {
      TileUI tile = tiles.get(i);

      // Get center coords of tile
      final float tX = GameScreen.TILE_SIZE * 0.5f + tile.x;
      final float tY = GameScreen.TILE_SIZE * 0.5f + tile.y;
      // Get center coords of current piece
      final float pX = piece.getWidth() * 0.5f + piece.getX();
      final float pY = piece.getWidth() * 0.5f + piece.getY();
      // Get distance between tile and current piece
      final float dX = Math.abs(tX - pX);
      final float dY = Math.abs(tY - pY);
      final double dZ = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

      // Activate snap tile
      if (dZ <= GameScreen.TILE_SIZE * 0.5f) {
        gameScreen.tX = tX;
        gameScreen.tY = tY;
        gameScreen.pX = pX;
        gameScreen.pY = pY;

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

    float destX = origTile.x;
    float destY = origTile.y;

    if (hasDestTile && destTile != null) {
      destX = destTile.x;
      destY = destTile.y;
      hasDestTile = false;
    }

    // add MoveToAction to piece actor
    MoveToAction mta = new MoveToAction();
    mta.setX(destX);
    mta.setY(destY);
    mta.setDuration(0.08f);

    System.out.println("dest coords = x:" + mta.getX() + "; y:" + mta.getY());

    piece.addAction(mta);
    piece.setZIndex(999); // Always on top of any pieces
    piece.getColor().a = 1; // Remove transparency

    // Clear snap tile and drag tile highlights
    gameScreen.tX = -1;
    gameScreen.tY = -1;
    gameScreen.pX = -1;
    gameScreen.pY = -1;
    gameScreen.origTile = null;
    gameScreen.destTile = null;
    destTile = null;
  }
}
