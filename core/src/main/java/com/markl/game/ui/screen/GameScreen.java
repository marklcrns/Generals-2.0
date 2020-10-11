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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.markl.game.GameState;
import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.BoardBuilder;
import com.markl.game.ui.Application;
import com.markl.game.ui.board.PieceUI;
import com.markl.game.ui.board.PieceUIListener;
import com.markl.game.ui.board.TileUI;

/**
 * @author Mark Lucernas
 * Date: Oct 03, 2020
 */
public class GameScreen implements Screen {

  public static final float TILE_SIZE = 70f;
  public static final float BOARD_WIDTH = TILE_SIZE * BOARD_TILES_COL_COUNT;
  public static final float BOARD_HEIGHT = TILE_SIZE * BOARD_TILES_ROW_COUNT;

  private Stage stage;
  private ShapeRenderer shapeRend;

  public final Application app;
  public final GameState game;
  public Board board;
  public BoardBuilder builder;
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
    board = new Board(this.game);

    // Initialize TileUI List
    tiles = new LinkedList<TileUI>();
  }

  public void populateTiles() {
    for (int i = 0; i < TOTAL_BOARD_TILES; i++) {

      // Invert Y to have tiles arranged left to right, top to bottom
      float tileWidth = TILE_SIZE;
      float tileHeight = TILE_SIZE;
      float tileX = getTileColNum(i) * tileWidth;
      float tileY = (TILE_SIZE * (BOARD_TILES_ROW_COUNT - 1)) - (getTileRowNum(i) * TILE_SIZE);

      TileUI newTile = new TileUI(i, tileX, tileY, tileWidth, tileHeight);
      tiles.add(newTile);

      // Create PieceUI as stage actor if tile is occupied
      if (board.getTile(i).isTileOccupied()) {
        Alliance alliance = board.getTile(i).getPiece().getAlliance();
        String pieceRank = board.getTile(i).getPiece().getRank();

        PieceUI pieceUI;
        if (alliance == Alliance.BLACK)
          pieceUI = new PieceUI(newTile, blackPiecesTex.get(pieceRank));
        else
          pieceUI = new PieceUI(newTile, whitePiecesTex.get(pieceRank));

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
    BoardBuilder builder = new BoardBuilder(board);
    builder.createDemoBoardBuild();
    builder.build(true);
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
      if (getTileRowNum(i) < BOARD_TILES_ROW_COUNT / 2)
        shapeRend.setColor(Color.BLUE);
      else
        shapeRend.setColor(Color.RED);
      // Draw square tile
      shapeRend.rect(tile.x, tile.y, tile.width, tile.height);
    }
    shapeRend.end();

    // Draw tile borders
    shapeRend.begin(ShapeType.Line);
    for (int i = 0; i < tiles.size(); i++) {
      TileUI tile = tiles.get(i);
      shapeRend.setColor(Color.BLACK);
      shapeRend.rect(tile.x, tile.y, tile.width, tile.height);
    }
    shapeRend.end();
  }

  // Draw snap-to-tile tile highlight
  public void drawTileHighlights() {
    if (destTile != null) {
      shapeRend.begin(ShapeType.Filled);
      shapeRend.setColor(Color.YELLOW);
      shapeRend.rect(destTile.x, destTile.y, destTile.width, destTile.height);
      shapeRend.end();
    } else if (origTile != null) {
      shapeRend.begin(ShapeType.Line);
      shapeRend.setColor(Color.YELLOW);
      shapeRend.rect(origTile.x, origTile.y, origTile.width, origTile.height);
      shapeRend.end();
    }
  }

  // Draw snap-to-tile line
  public void drawTileSnapLinePath() {
    if (tX != -1 && tY != -1 && pX != -1 && pY != -1) {
      shapeRend.begin(ShapeType.Filled);
      shapeRend.setColor(Color.MAROON);
      shapeRend.rectLine(tX, tY, pX, pY, 1.5f);
      shapeRend.end();
    }
  }

  public boolean movePieceUI(int srcPieceUITileId, int tgtPieceUITileId) {
    if (board.movePiece(srcPieceUITileId, tgtPieceUITileId)) {
      TileUI srcTileUI = tiles.get(srcPieceUITileId);
      TileUI tgtTileUI = tiles.get(srcPieceUITileId);
      // Make target TileUI the tile for source PieceUI
      tgtTileUI.setPieceUI(srcTileUI.pieceUI);
      srcTileUI.setPieceUI(null);
      return true;
    }
    return false;
  }

  @Override
  public void render(float delta) {
    // Background
    Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen

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
    app.font.draw(app.batch, "Generals", 0, Application.V_HEIGHT);

    app.batch.end();

    // For debug
    app.batch.begin();
    drawTileSnapLinePath();
    app.batch.end();

    // // process user input
    // if(Gdx.input.isTouched()) {
    //   Vector3 touchPos = new Vector3();
    //   touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
    //   gameUI.camera.unproject(touchPos);
    //   piece.x = touchPos.x - 64 / 2;
    //   piece.y = touchPos.y - 64 / 2;
    // }
    // if(Gdx.input.isKeyPressed(Keys.LEFT)) piece.x -= 200 * Gdx.graphics.getDeltaTime();
    // if(Gdx.input.isKeyPressed(Keys.RIGHT)) piece.x += 200 * Gdx.graphics.getDeltaTime();
    //
    // // make sure the bucket stays within the screen bounds
    // if(piece.x < 0) piece.x = 0;
    // if(piece.x > GoG.V_WIDTH - 64) piece.x = GoG.V_WIDTH - 64;
    //
    // if(piece.y < 0) piece.y = 0;
    // if(piece.y > GoG.V_HEIGHT - 64) piece.y = GoG.V_HEIGHT - 64;
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
}
