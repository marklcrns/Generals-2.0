package com.markl.game.util;

import static com.markl.game.engine.board.BoardUtils.BOARD_TILES_COL_COUNT;
import static com.markl.game.engine.board.BoardUtils.BOARD_TILES_ROW_COUNT;

import com.badlogic.gdx.graphics.Color;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 11/24/2020.
 */
public class Constants {

  public static final float VERSION = 0.1f;

  // Window
  public static final int VIEWPORT_WIDTH = 800;
  public static final int VIEWPORT_HEIGHT = 600;

  // Game board UI
  public static final float TILE_SIZE          = 60f;
  public static final float BOARD_WIDTH        = TILE_SIZE * BOARD_TILES_COL_COUNT;
  public static final float BOARD_HEIGHT       = TILE_SIZE * BOARD_TILES_ROW_COUNT;
  public static final float BOARD_X_OFFSET     = (VIEWPORT_WIDTH - BOARD_WIDTH) / 2;
  public static final float BOARD_Y_OFFSET     = (VIEWPORT_HEIGHT - BOARD_HEIGHT) / 2;

  // Game PieceUI
  public static final float PIECE_UI_ANIMATION_SPEED = 0.08f;

  // Game board colorcheme
  public static final Color TILE_COLOR_ACTIVE         = new Color(0x9BB6CBFF);
  public static final Color TILE_COLOR_INACTIVE       = new Color(0xB8BCC8FF);
  public static final Color TILE_BORDER_COLOR         = new Color(0x6F83A4FF);
  public static final Color TILE_BORDER_COLOR_ACTIVE  = new Color(0xFFFFFFFF);
  public static final Color AGGRESSIVE_TILE_HIGHLIGHT = Color.MAROON;
  public static final Color INVALID_TILE_HIGHLIGHT    = Color.GRAY;
  public static final Color NORMAL_TILE_HIGHLIGHT     = Color.GOLD;
  public static final Color ORIGIN_TILE_HIGHLIGHT     = Color.BLUE;

  /**
   * Constructor method that ensures this Utils Constants cannot be instantiated.
   */
  private Constants() {
    throw new RuntimeException("You cannot instantiate Utils class");
  }
}
