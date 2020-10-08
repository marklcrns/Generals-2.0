package com.markl.game.ui.screen;

import static com.markl.game.engine.board.BoardUtils.BOARD_TILES_COL_COUNT;
import static com.markl.game.engine.board.BoardUtils.BOARD_TILES_ROW_COUNT;
import static com.markl.game.engine.board.BoardUtils.getPieceImagePath;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.markl.game.GameState;
import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.BoardBuilder;
import com.markl.game.ui.GoG;
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

  private final GoG gameUI;

  private Stage stage;
  private ShapeRenderer shapeRend;

  public GameState game;
  public Board board;
  public BoardBuilder builder;
  public TileUI[][] tiles;
  public Map<String, Texture> blackPiecesTex = new HashMap<>();
  public Map<String, Texture> whitePiecesTex = new HashMap<>();
  public Map<Integer, PieceUI> boardActors = new HashMap<>();

  public GameScreen(final GoG gameUI) {
    this.gameUI = gameUI;
    this.shapeRend = new ShapeRenderer();
    this.stage = new Stage(new FitViewport(GoG.V_WIDTH, GoG.V_HEIGHT, gameUI.camera));
    Gdx.input.setInputProcessor(stage);

    // Initialize GoG game engine
    game = new GameState();
    board = new Board(this.game);

    // Initialize TileUI 2D array
    tiles = new TileUI[BOARD_TILES_COL_COUNT][BOARD_TILES_ROW_COUNT];
  }

  public void populateTiles() {
    // Fill tiles top to bottom, left to right
    for (int i = 0; i < BOARD_TILES_COL_COUNT; i++) {
      for (int j = 0; j < BOARD_TILES_ROW_COUNT; j++) {

        int tileId = (BOARD_TILES_COL_COUNT * j) + i;

        // Invert Y to have tiles arranged left to right, top to bottom
        float tileX = 0 + (i * TILE_SIZE);
        float tileY = (TILE_SIZE * (BOARD_TILES_ROW_COUNT - 1)) - (j * TILE_SIZE);
        float tileWidth = TILE_SIZE;
        float tileHeight = TILE_SIZE;

        tiles[i][j] = new TileUI(tileId, tileX, tileY, tileWidth, tileHeight);

        // Create PieceUI as stage actor if tile is occupied
        if (board.getTile(tileId).isTileOccupied()) {
          Alliance alliance = board.getTile(tileId).getPiece().getAlliance();
          String pieceRank = board.getTile(tileId).getPiece().getRank();
          PieceUI piece;

          if (alliance == Alliance.BLACK)
            piece = new PieceUI(blackPiecesTex.get(pieceRank));
          else
            piece = new PieceUI(whitePiecesTex.get(pieceRank));

          piece.setWidth(tileWidth);
          piece.setHeight(tileHeight);
          piece.setPosition(tileX, tileY);

          piece.addListener(new PieceUIListener(gameUI.camera, piece));

          stage.addActor(piece);

          boardActors.put(tileId, piece);
        }
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

  public void drawBoard(Batch batch, ShapeRenderer shapeRend) {
    batch.begin();
    shapeRend.setProjectionMatrix(batch.getProjectionMatrix());
    shapeRend.setTransformMatrix(batch.getTransformMatrix());

    // Draw board background
    shapeRend.begin(ShapeType.Filled);
    for (int i = 0; i < BOARD_TILES_COL_COUNT; i++) {
      for (int j = 0; j < BOARD_TILES_ROW_COUNT; j++) {
        TileUI tile = tiles[i][j];

        // Split board into two territory
        if (j < BOARD_TILES_ROW_COUNT / 2)
          shapeRend.setColor(Color.ORANGE);
        else
          shapeRend.setColor(Color.RED);

        tiles[i][j] = new TileUI(tile.id, tile.x, tile.y, tile.width, tile.height);
        shapeRend.rect(tile.x, tile.y, tile.width, tile.height);
      }
    }
    shapeRend.end();

    // Draw tile borders
    shapeRend.begin(ShapeType.Line);
    for (int i = 0; i < BOARD_TILES_COL_COUNT; i++) {
      for (int j = 0; j < BOARD_TILES_ROW_COUNT; j++) {
        shapeRend.setColor(Color.BLACK);

        TileUI tile = tiles[i][j];
        shapeRend.rect(tile.x, tile.y, tile.width, tile.height);
      }
    }
    shapeRend.end();
    batch.end();
  }

  @Override
  public void render(float delta) {
    // Background
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen

    gameUI.camera.update();
    gameUI.batch.setProjectionMatrix(gameUI.camera.combined);

    // Update stage
    update(delta);

    drawBoard(gameUI.batch, shapeRend);
    // Draw board and board actors
    gameUI.batch.begin();
    stage.draw();
    gameUI.font.draw(gameUI.batch, "Generals", 0, GoG.V_HEIGHT);
    gameUI.batch.end();

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
    blackPiecesTex.put("GeneralFive", gameUI.assets.get(getPieceImagePath("black", "GeneralFive"), Texture.class));
    blackPiecesTex.put("GeneralFour", gameUI.assets.get(getPieceImagePath("black", "GeneralFour"), Texture.class));
    blackPiecesTex.put("GeneralThree", gameUI.assets.get(getPieceImagePath("black", "GeneralThree"), Texture.class));
    blackPiecesTex.put("GeneralTwo", gameUI.assets.get(getPieceImagePath("black", "GeneralTwo"), Texture.class));
    blackPiecesTex.put("GeneralOne", gameUI.assets.get(getPieceImagePath("black", "GeneralOne"), Texture.class));
    blackPiecesTex.put("Colonel", gameUI.assets.get(getPieceImagePath("black", "Colonel"), Texture.class));
    blackPiecesTex.put("LtCol", gameUI.assets.get(getPieceImagePath("black", "LtCol"), Texture.class));
    blackPiecesTex.put("Major", gameUI.assets.get(getPieceImagePath("black", "Major"), Texture.class));
    blackPiecesTex.put("Captain", gameUI.assets.get(getPieceImagePath("black", "Captain"), Texture.class));
    blackPiecesTex.put("LtOne", gameUI.assets.get(getPieceImagePath("black", "LtOne"), Texture.class));
    blackPiecesTex.put("LtTwo", gameUI.assets.get(getPieceImagePath("black", "LtTwo"), Texture.class));
    blackPiecesTex.put("Sergeant", gameUI.assets.get(getPieceImagePath("black", "Sergeant"), Texture.class));
    blackPiecesTex.put("Private", gameUI.assets.get(getPieceImagePath("black", "Private"), Texture.class));
    blackPiecesTex.put("Spy", gameUI.assets.get(getPieceImagePath("black", "Spy"), Texture.class));
    blackPiecesTex.put("Flag", gameUI.assets.get(getPieceImagePath("black", "Flag"), Texture.class));

    // Get white pieces
    whitePiecesTex.put("GeneralFive", gameUI.assets.get(getPieceImagePath("white", "GeneralFive"), Texture.class));
    whitePiecesTex.put("GeneralFour", gameUI.assets.get(getPieceImagePath("white", "GeneralFour"), Texture.class));
    whitePiecesTex.put("GeneralThree", gameUI.assets.get(getPieceImagePath("white", "GeneralThree"), Texture.class));
    whitePiecesTex.put("GeneralTwo", gameUI.assets.get(getPieceImagePath("white", "GeneralTwo"), Texture.class));
    whitePiecesTex.put("GeneralOne", gameUI.assets.get(getPieceImagePath("white", "GeneralOne"), Texture.class));
    whitePiecesTex.put("Colonel", gameUI.assets.get(getPieceImagePath("white", "Colonel"), Texture.class));
    whitePiecesTex.put("LtCol", gameUI.assets.get(getPieceImagePath("white", "LtCol"), Texture.class));
    whitePiecesTex.put("Major", gameUI.assets.get(getPieceImagePath("white", "Major"), Texture.class));
    whitePiecesTex.put("Captain", gameUI.assets.get(getPieceImagePath("white", "Captain"), Texture.class));
    whitePiecesTex.put("LtOne", gameUI.assets.get(getPieceImagePath("white", "LtOne"), Texture.class));
    whitePiecesTex.put("LtTwo", gameUI.assets.get(getPieceImagePath("white", "LtTwo"), Texture.class));
    whitePiecesTex.put("Sergeant", gameUI.assets.get(getPieceImagePath("white", "Sergeant"), Texture.class));
    whitePiecesTex.put("Private", gameUI.assets.get(getPieceImagePath("white", "Private"), Texture.class));
    whitePiecesTex.put("Spy", gameUI.assets.get(getPieceImagePath("white", "Spy"), Texture.class));
    whitePiecesTex.put("Flag", gameUI.assets.get(getPieceImagePath("white", "Flag"), Texture.class));
  }
}
