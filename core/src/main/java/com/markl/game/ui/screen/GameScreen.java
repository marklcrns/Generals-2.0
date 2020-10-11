package com.markl.game.ui.screen;

import static com.markl.game.engine.board.BoardUtils.BOARD_TILES_COL_COUNT;
import static com.markl.game.engine.board.BoardUtils.BOARD_TILES_ROW_COUNT;
import static com.markl.game.engine.board.BoardUtils.TOTAL_BOARD_TILES;
import static com.markl.game.engine.board.BoardUtils.getPieceImagePath;
import static com.markl.game.engine.board.BoardUtils.getTileColNum;
import static com.markl.game.engine.board.BoardUtils.getTileRowNum;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.markl.game.GameState;
import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.BoardBuilder;
import com.markl.game.engine.board.Player;
import com.markl.game.ui.Application;
import com.markl.game.ui.board.PieceUI;
import com.markl.game.ui.board.PieceUIListener;
import com.markl.game.ui.board.TileUI;

/**
 * @author Mark Lucernas
 * Date: Oct 03, 2020
 */
public class GameScreen implements Screen {

  /** Tile metrics */
  public static final float TILE_SIZE          = 60f;
  public static final float BOARD_WIDTH        = TILE_SIZE * BOARD_TILES_COL_COUNT;
  public static final float BOARD_HEIGHT       = TILE_SIZE * BOARD_TILES_ROW_COUNT;
  public static final float BOARD_X_OFFSET     = (Application.V_WIDTH - BOARD_WIDTH) / 2;
  public static final float BOARD_Y_OFFSET     = (Application.V_HEIGHT - BOARD_HEIGHT) / 2;
  /** Color Scheme */
  public final Color TILE_COLOR                = new Color(0x9BB6CBFF);
  public final Color TILE_BORDER_COLOR         = new Color(0x6F83A4FF);
  public final Color AGGRESSIVE_TILE_HIGHLIGHT = Color.MAROON;
  public final Color INVALID_TILE_HIGHLIGHT    = Color.GRAY;
  public final Color NORMAL_TILE_HIGHLIGHT     = Color.GOLD;
  public final Color ORIGIN_TILE_HIGHLIGHT     = Color.BLUE;

  private Stage stage;
  private ShapeRenderer shapeRend;

  public final Application app;
  public final GameState game;
  public Board board;
  public BoardBuilder builder;
  public Player playerBlack;
  public Player playerWhite;
  public LinkedList<TileUI> tiles;  // List of all Tiles containing data of each piece
  public Map<String, Texture> blackPiecesTex = new HashMap<>();
  public Map<String, Texture> whitePiecesTex = new HashMap<>();

  // TODO: Debug
  public float tX = -1;
  public float tY = -1;
  public float pX = -1;
  public float pY = -1;
  public TileUI origTile;
  public TileUI destTile;

  public GameScreen(final Application app) {
    this.app = app;
    this.shapeRend = new ShapeRenderer();
    this.stage = new Stage(new StretchViewport(Application.V_WIDTH, Application.V_HEIGHT, app.camera));
    Gdx.input.setInputProcessor(stage);

    // Initialize GoG game engine
    game = new GameState();
    board = new Board(game);
    builder = new BoardBuilder(board);
    playerBlack = new Player(Alliance.BLACK);
    playerWhite = new Player(Alliance.WHITE);

    // Initialize TileUI List
    tiles = new LinkedList<TileUI>();
  }

  public void populateTiles() {
    for (int i = 0; i < TOTAL_BOARD_TILES; i++) {

      // Invert Y to have tiles arranged left to right, top to bottom
      float tileWidth = TILE_SIZE;
      float tileHeight = TILE_SIZE;
      float tileX = getTileColNum(i) * tileWidth + BOARD_X_OFFSET;
      float tileY = (TILE_SIZE * (BOARD_TILES_ROW_COUNT - 1)) -
        (getTileRowNum(i) * TILE_SIZE) + BOARD_Y_OFFSET;

      TileUI newTile = new TileUI(i, tileX, tileY, tileWidth, tileHeight);
      tiles.add(newTile);

      // Create PieceUI as stage actor if tile is occupied
      if (board.getTile(i).isTileOccupied()) {
        Alliance alliance = board.getTile(i).getPiece().getAlliance();
        String pieceRank = board.getTile(i).getPiece().getRank();

        PieceUI pieceUI;
        if (alliance == Alliance.BLACK)
          pieceUI = new PieceUI(newTile, blackPiecesTex.get(pieceRank), pieceRank);
        else
          pieceUI = new PieceUI(newTile, whitePiecesTex.get(pieceRank), pieceRank);

        pieceUI.setWidth(tileWidth);
        pieceUI.setHeight(tileHeight);
        pieceUI.setPosition(tileX, tileY);
        pieceUI.addListener(new PieceUIListener(this, pieceUI));

        stage.addActor(pieceUI);
        newTile.pieceUI = pieceUI;
      }
    }
  }

  @Override
  public void show() {
    System.out.println("GameScreen show");
    // Create initial board arrangement and start game
    board.setPlayerBlack(playerBlack);
    board.setPlayerWhite(playerWhite);
    builder.createDemoBoardBuild();
    builder.build(true);
    board.initGame();
    game.start();

    getAssetImages();

    Gdx.input.setInputProcessor(stage);
    populateTiles();
  }

  public void update(float delta) {
    stage.act(delta);
  }

  public void drawBoard() {
    shapeRend.begin(ShapeType.Filled);
    for (int i = 0; i < tiles.size(); i++) {
      TileUI tile = tiles.get(i);

      // Split board into two territory
      // if (getTileRowNum(i) < BOARD_TILES_ROW_COUNT / 2)

      shapeRend.setColor(TILE_COLOR);
      // Draw square tile
      shapeRend.rect(tile.x, tile.y, tile.width, tile.height);
    }
    shapeRend.end();

    // Draw tile borders
    shapeRend.begin(ShapeType.Line);
    for (int i = 0; i < tiles.size(); i++) {
      TileUI tile = tiles.get(i);
      shapeRend.setColor(TILE_BORDER_COLOR);
      shapeRend.rect(tile.x, tile.y, tile.width, tile.height);
    }
    shapeRend.end();
  }

  // Draw snap-to-tile tile highlight
  public void drawTileHighlights() {
    // TODO: Adjust alpha <10-10-20, yourname> //
    if (destTile != null && destTile.id != origTile.id) {
      shapeRend.begin(ShapeType.Filled);

      // TODO: Depend on Move.moveType
      // Highlight destination tile based on move type
      if (board.getTile(destTile.id).isTileOccupied()) {
        if (board.getTile(origTile.id).getPiece().getAlliance() != // Aggressive move tile highlight
            board.getTile(destTile.id).getPiece().getAlliance())
          shapeRend.setColor(AGGRESSIVE_TILE_HIGHLIGHT);
        else                                                       // Invalid friendly-fire move tile highlight
          shapeRend.setColor(INVALID_TILE_HIGHLIGHT);
      } else {
        shapeRend.setColor(NORMAL_TILE_HIGHLIGHT);                 // Normal move tile highlight
      }

      shapeRend.rect(destTile.x, destTile.y, destTile.width, destTile.height);
      shapeRend.end();
    } else if (origTile != null) {
      shapeRend.begin(ShapeType.Line);
      shapeRend.setColor(ORIGIN_TILE_HIGHLIGHT);
      shapeRend.rect(origTile.x, origTile.y, origTile.width, origTile.height);
      shapeRend.end();
    }
  }

  // Draw snap-to-tile line
  public void drawTileSnapLinePath() {
    if (tX != -1 && tY != -1 && pX != -1 && pY != -1) {
      shapeRend.begin(ShapeType.Filled);
      shapeRend.setColor(Color.WHITE);
      shapeRend.rectLine(tX, tY, pX, pY, 1.5f);
      shapeRend.end();
    }
  }

  public boolean movePieceUI(int srcPieceUITileId, int tgtPieceUITileId) {
    PieceUI srcPieceUI = tiles.get(srcPieceUITileId).pieceUI;
    PieceUI tgtPieceUI = tiles.get(tgtPieceUITileId).pieceUI;
    if (srcPieceUI != null && tgtPieceUI == null) {
      // Make target TileUI the tile for source PieceUI
      tiles.get(tgtPieceUITileId).setPieceUI(srcPieceUI);
      tiles.get(srcPieceUITileId).setPieceUI(null);
      return true;
    }
    return false;
  }

  public boolean removePieceUI(int tileId) {
    TileUI tile = tiles.get(tileId);
    if (tile.pieceUI != null) {
      tiles.get(tileId).pieceUI.addAction(Actions.removeActor());
      tiles.get(tileId).setPieceUI(null);
      return true;
    }
    return false;
  }

  // TODO: Make sure TileUI is cloned <11-10-20, yourname> //
  public void swapPieceUI(int srcPieceUITileId, int tgtPieceUITileId) {
    TileUI srcTileUI = tiles.get(srcPieceUITileId);
    TileUI tgtTileUI = tiles.get(srcPieceUITileId);
    TileUI tmpTileUI = tgtTileUI;
    // Make target TileUI the tile for source PieceUI
    tgtTileUI.setPieceUI(srcTileUI.pieceUI);
    srcTileUI.setPieceUI(tmpTileUI.pieceUI);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);     // Background color
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);     // Clear screen
    Gdx.gl.glEnable(GL20.GL_BLEND);               // Enable color transparent
    Gdx.gl20.glLineWidth(2.5f / app.camera.zoom); // Set line width

    app.camera.update();
    app.batch.setProjectionMatrix(app.camera.combined);
    shapeRend.setProjectionMatrix(app.batch.getProjectionMatrix());
    shapeRend.setTransformMatrix(app.batch.getTransformMatrix());

    // Update stage
    update(delta);

    // Draw board -> tile highlights -> piece actors
    app.batch.begin();
    drawBoard();
    drawTileHighlights();
    stage.draw();
    app.font.draw(app.batch, "PLAYER: " + game.getCurrentTurnMaker().getAlliance(), 0, Application.V_HEIGHT);
    app.font.draw(app.batch, "TURN: " + game.getCurrTurn(), BOARD_X_OFFSET, BOARD_Y_OFFSET);
    app.batch.end();

    // For debug
    app.batch.begin();
    drawTileSnapLinePath();
    app.batch.end();
  }

  @Override
  public void resize(int width, int height) {
    System.out.println("GameScreen resize");
    stage.getViewport().update(width, height, false);
  }

  @Override
  public void pause() {
    System.out.println("GameScreen pause");
  }

  @Override
  public void resume() {
    System.out.println("GameScreen resume");
  }

  @Override
  public void hide() {
    System.out.println("GameScreen hide");
  }

  @Override
  public void dispose() {
    System.out.println("GameScreen dispose");
    stage.dispose();
    shapeRend.dispose();
  }

  private void getAssetImages() {
    // Get black pieces
    blackPiecesTex.put("GeneralFive", app.assets.get(getPieceImagePath("black", "GeneralFive"), Texture.class));
    blackPiecesTex.put("GeneralFour", app.assets.get(getPieceImagePath("black", "GeneralFour"), Texture.class));
    blackPiecesTex.put("GeneralThree", app.assets.get(getPieceImagePath("black", "GeneralThree"), Texture.class));
    blackPiecesTex.put("GeneralTwo", app.assets.get(getPieceImagePath("black", "GeneralTwo"), Texture.class));
    blackPiecesTex.put("GeneralOne", app.assets.get(getPieceImagePath("black", "GeneralOne"), Texture.class));
    blackPiecesTex.put("Colonel", app.assets.get(getPieceImagePath("black", "Colonel"), Texture.class));
    blackPiecesTex.put("LtCol", app.assets.get(getPieceImagePath("black", "LtCol"), Texture.class));
    blackPiecesTex.put("Major", app.assets.get(getPieceImagePath("black", "Major"), Texture.class));
    blackPiecesTex.put("Captain", app.assets.get(getPieceImagePath("black", "Captain"), Texture.class));
    blackPiecesTex.put("LtOne", app.assets.get(getPieceImagePath("black", "LtOne"), Texture.class));
    blackPiecesTex.put("LtTwo", app.assets.get(getPieceImagePath("black", "LtTwo"), Texture.class));
    blackPiecesTex.put("Sergeant", app.assets.get(getPieceImagePath("black", "Sergeant"), Texture.class));
    blackPiecesTex.put("Private", app.assets.get(getPieceImagePath("black", "Private"), Texture.class));
    blackPiecesTex.put("Spy", app.assets.get(getPieceImagePath("black", "Spy"), Texture.class));
    blackPiecesTex.put("Flag", app.assets.get(getPieceImagePath("black", "Flag"), Texture.class));

    // Get white pieces
    whitePiecesTex.put("GeneralFive", app.assets.get(getPieceImagePath("white", "GeneralFive"), Texture.class));
    whitePiecesTex.put("GeneralFour", app.assets.get(getPieceImagePath("white", "GeneralFour"), Texture.class));
    whitePiecesTex.put("GeneralThree", app.assets.get(getPieceImagePath("white", "GeneralThree"), Texture.class));
    whitePiecesTex.put("GeneralTwo", app.assets.get(getPieceImagePath("white", "GeneralTwo"), Texture.class));
    whitePiecesTex.put("GeneralOne", app.assets.get(getPieceImagePath("white", "GeneralOne"), Texture.class));
    whitePiecesTex.put("Colonel", app.assets.get(getPieceImagePath("white", "Colonel"), Texture.class));
    whitePiecesTex.put("LtCol", app.assets.get(getPieceImagePath("white", "LtCol"), Texture.class));
    whitePiecesTex.put("Major", app.assets.get(getPieceImagePath("white", "Major"), Texture.class));
    whitePiecesTex.put("Captain", app.assets.get(getPieceImagePath("white", "Captain"), Texture.class));
    whitePiecesTex.put("LtOne", app.assets.get(getPieceImagePath("white", "LtOne"), Texture.class));
    whitePiecesTex.put("LtTwo", app.assets.get(getPieceImagePath("white", "LtTwo"), Texture.class));
    whitePiecesTex.put("Sergeant", app.assets.get(getPieceImagePath("white", "Sergeant"), Texture.class));
    whitePiecesTex.put("Private", app.assets.get(getPieceImagePath("white", "Private"), Texture.class));
    whitePiecesTex.put("Spy", app.assets.get(getPieceImagePath("white", "Spy"), Texture.class));
    whitePiecesTex.put("Flag", app.assets.get(getPieceImagePath("white", "Flag"), Texture.class));
  }

  /**
   * @return String representation of all current TilesUI for debugging
   */
  @Override
  public String toString() {
    String debugBoard = "GameScreen Debug Board\n";
    debugBoard += "    0 1 2 3 4 5 6 7 8\n";
    debugBoard += "    _________________\n";
    for (int i = 0; i < TOTAL_BOARD_TILES / 2; i += 9) {
      if (i < 10)
        debugBoard += " " + i + " |";
      else
        debugBoard += i + " |";
      for (int j = i; j < i + 9; j++) {
        if (tiles.get(j).isTileUIEmpty()) {
          debugBoard += "-";
        } else {
          final String rank = tiles.get(j).pieceUI.pieceRank;
          if (rank == "GeneralOne")
            debugBoard += "1";
          else if (rank == "GeneralTwo")
            debugBoard += "2";
          else if (rank == "GeneralThree")
            debugBoard += "3";
          else if (rank == "GeneralFour")
            debugBoard += "4";
          else if (rank == "GeneralFive")
            debugBoard += "5";
          else
            debugBoard += rank.substring(0, 1);
        }
        debugBoard += " ";
      }
      debugBoard += "\n";
    }

    debugBoard += "   |-----------------\n";

    for (int i = TOTAL_BOARD_TILES / 2; i < TOTAL_BOARD_TILES; i += 9) {
      if (i < 10)
        debugBoard += " " + i + " |";
      else
        debugBoard += i + " |";
      for (int j = i; j < i + 9; j++) {
        if (tiles.get(j).isTileUIEmpty()) {
          debugBoard += "-";
        } else {
          final String rank = tiles.get(j).pieceUI.pieceRank;
          if (rank == "GeneralOne")
            debugBoard += "1";
          else if (rank == "GeneralTwo")
            debugBoard += "2";
          else if (rank == "GeneralThree")
            debugBoard += "3";
          else if (rank == "GeneralFour")
            debugBoard += "4";
          else if (rank == "GeneralFive")
            debugBoard += "5";
          else
            debugBoard += rank.substring(0, 1);
        }
        debugBoard += " ";
      }
      debugBoard += "\n";
    }

    return debugBoard;
  }
}
