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
  public static final float BOARD_OUTLINE_THICKNESS         = 3;
  public static final Color BOARD_OUTLINE_COLOR             = new Color(0x2f4374ff); // Dark Blue
  public static final Color BOARD_ACTIVE_COLOR              = new Color(0x9bb6cbff); // Steel Blue
  public static final Color BOARD_INACTIVE_COLOR            = new Color(0xb8bcc8ff); // Gray
  public static final Color TILE_BORDER_COLOR               = new Color(0x6f83a4ff); // Faded Steel Blue
  public static final Color TILE_ACTIVE_COLOR               = new Color(0xcbe6fbff); // Sky Blue
  public static final Color TILE_ACTIVE_PIECE_COLOR         = new Color(0xffd700ff); // Gold
  public static final Color TILE_AGGRESSIVE_HIGHLIGHT_COLOR = new Color(0xb03060ff); // MAROON
  public static final Color TILE_INVALID_HIGHLIGHT_COLOR    = new Color(0x7f7f7fff); // GRAY
  public static final Color TILE_NORMAL_HIGHLIGHT_COLOR     = new Color(0x0000bfff); // BLUE

  /**
   * Constructor method that ensures this Utils Constants cannot be instantiated.
   */
  private Constants() {
    throw new RuntimeException("You cannot instantiate Utils class");
  }
}
