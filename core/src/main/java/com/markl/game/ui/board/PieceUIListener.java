package com.markl.game.ui.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.markl.game.control.MoveManager;
import com.markl.game.control.PieceUIManager;
import com.markl.game.ui.screen.GameScreen;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/07/2020.
 */
public class PieceUIListener extends ClickListener {

  private PieceUI pieceUI;
  private PieceUIManager pieceUIManager;
  private GameScreen gameScreen;
  private MoveManager moveManager;

  private Vector3 mousePos;

  public PieceUIListener(PieceUI pieceUI, GameScreen gameScreen) {
    this.pieceUI        = pieceUI;
    this.gameScreen     = gameScreen;
    this.moveManager    = gameScreen.moveManager;
    this.pieceUIManager = gameScreen.pieceUIManager;
  }

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
    if (isMyPiece()) {
      gameScreen.activeTileUI = pieceUI.tileUI;
      gameScreen.activeSrcPiece = gameScreen.board.getPiece(pieceUI.tileUI.getTileId());
      gameScreen.activeSrcPiece.evaluateMoves();
    }
    return super.touchDown(event, x, y, pointer, button);
  }

  @Override
  public void touchDragged(InputEvent event, float x, float y, int pointer) {
    super.touchDragged(event, x, y, pointer);
    if (isMyPiece()) {
      // Set click/touch position relative to world coordinates
      mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
      gameScreen.app.camera.unproject(mousePos); // mousePos is now in world coordinates

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

      // Follow mouse pointer when the PieceUI is dragged
      pieceUIManager.animatePieceUIMove(pieceUI,
          mousePos.x - pieceUI.getWidth() * 0.5f,
          mousePos.y - pieceUI.getHeight() * 0.5f,
          0.5f); // Make piece transparent

      gameScreen.destTileUI = null;

      // TODO optimize to lookup only the closest TileUI(s)
      for (int i = 0; i < gameScreen.tilesUI.size(); i++) {
        TileUI tileUI = gameScreen.tilesUI.get(i);

        // Get center coords of tile
        final float tX  = GameScreen.TILE_SIZE * 0.5f + tileUI.x;
        final float tY  = GameScreen.TILE_SIZE * 0.5f + tileUI.y;
        // Get center coords of current piece
        final float pX  = pieceUI.getWidth() * 0.5f + pieceUI.getX();
        final float pY  = pieceUI.getWidth() * 0.5f + pieceUI.getY();
        // Get distance between tile and current piece
        final float dX  = Math.abs(tX - pX);
        final float dY  = Math.abs(tY - pY);
        final double dZ = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

        // Activate snap-to-tile tile within tile circle radius
        if (dZ <= GameScreen.TILE_SIZE * 0.5f) {
          gameScreen.tX = tX; gameScreen.tY = tY;
          gameScreen.pX = pX; gameScreen.pY = pY;

          // Update destination tile
          gameScreen.destTileUI = tileUI;
          break;
        }

        if (gameScreen.destTileUI != null)
          break;
      }

      // Clear snap tile and dest highlight then Highlight origin tile
      if (gameScreen.destTileUI == null) {
        clearSnapTileHighlights();
      }
    }
  }

  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    super.touchUp(event, x, y, pointer, button);
    if (isMyPiece()) {
      if (gameScreen.destTileUI != null) {
        moveManager.makeMove(pieceUI.tileUI, gameScreen.destTileUI, true);
      } else {
        pieceUIManager.animatePieceUIMove(pieceUI, pieceUI.tileUI.x, pieceUI.tileUI.y, 1);
      }

      clearSnapTileHighlights();

      // Clear destination tile and active piece
      gameScreen.destTileUI = null;
      gameScreen.activeSrcPiece = null;

      // TODO Delete me later
      // System.out.println("");
      // System.out.println(gameScreen.toString());
      // System.out.println(gameScreen.board.toString());
    }
  }

  public boolean isMyPiece() {
    if (gameScreen.gameState.getMyAlliance() ==
        gameScreen.board.getPiece(pieceUI.tileUI.getTileId()).getAlliance())
      return true;
    return false;
  }

  public void clearSnapTileHighlights() {
    // Clear snap tile and drag tile highlights
    gameScreen.tX = -1;
    gameScreen.tY = -1;
    gameScreen.pX = -1;
    gameScreen.pY = -1;
  }
}
