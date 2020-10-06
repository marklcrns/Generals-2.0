package com.markl.game.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.markl.game.engine.board.BoardUtils;
import com.markl.game.ui.GoG;
import com.markl.game.ui.board.TileUI;

/**
 * @author Mark Lucernas
 * Date: Oct 03, 2020
 */
public class GameScreen implements Screen {

  private final GoG game;

  private Stage stage;
  private Texture pieceImg;
  private Rectangle piece;
  private ShapeRenderer shapeRend;

  public TileUI[][] tiles;

  public GameScreen(final GoG game) {
    this.game = game;
    this.shapeRend = new ShapeRenderer();
    this.stage = new Stage(new StretchViewport(GoG.V_WIDTH, GoG.V_HEIGHT, game.camera));
    Gdx.input.setInputProcessor(stage);

    // Load image as Texture
    pieceImg = new Texture(Gdx.files.internal("pieces/original/black/BLACK_Flag.png"));

    piece = new Rectangle();
    piece.x = GoG.V_WIDTH / 2 - 64 / 2; // center the piece horizontally
    piece.y = 20; // bottom left corner
    piece.width = 64;
    piece.height = 64;

    tiles = new TileUI[BoardUtils.BOARD_TILES_COL_COUNT][BoardUtils.BOARD_TILES_ROW_COUNT];
  }

  public void populateTiles() {
    // TODO: Clean up <05-10-20, Mark Lucernas> //
    int tileCount = 0;
    for (int i = 0; i < BoardUtils.BOARD_TILES_COL_COUNT; i++) {
      for (int j = 0; j < BoardUtils.BOARD_TILES_ROW_COUNT; j++) {

        int tileId = BoardUtils.BOARD_TILES_ROW_COUNT * i + j;
        float tileX = 0 + (i * BoardUtils.TILE_SIZE);
        float tileY = 0 + (j * BoardUtils.TILE_SIZE);
        float tileWidth = BoardUtils.TILE_SIZE;
        float tileHeight = BoardUtils.TILE_SIZE;

        tiles[i][j] = new TileUI(tileId, tileX, tileY, tileWidth, tileHeight);
        System.out.println(tiles[i][j].toString());
        tileCount++;
      }
    }
    System.out.println(tileCount);
  }

  @Override
  public void show() {
    System.out.println("GameScreen show");
    populateTiles();
  }

  public void update(float delta) {
    stage.act(delta);
  }

  public void drawBoard(Batch batch, ShapeRenderer shapeRend) {
    batch.begin();
    shapeRend.setProjectionMatrix(batch.getProjectionMatrix());
    shapeRend.setTransformMatrix(batch.getTransformMatrix());

    shapeRend.begin(ShapeType.Filled);
    for (int i = 0; i < BoardUtils.BOARD_TILES_COL_COUNT; i++) {
      for (int j = 0; j < BoardUtils.BOARD_TILES_ROW_COUNT; j++) {
        if (j < BoardUtils.BOARD_TILES_ROW_COUNT / 2)
          shapeRend.setColor(Color.ORANGE);
        else
          shapeRend.setColor(Color.RED);

        TileUI tile = tiles[i][j];

        tiles[i][j] = new TileUI(tile.id, tile.x, tile.y, tile.width, tile.height, false);
        shapeRend.rect(tile.x, tile.y, tile.width, tile.height);
      }
    }
    shapeRend.end();

    shapeRend.begin(ShapeType.Line);
    for (int i = 0; i < BoardUtils.BOARD_TILES_COL_COUNT; i++) {
      for (int j = 0; j < BoardUtils.BOARD_TILES_ROW_COUNT; j++) {
        shapeRend.setColor(Color.BLACK);

        TileUI tile = tiles[i][j];
        shapeRend.rect(tile.x, tile.y, tile.width, tile.height);
      }
    }
    shapeRend.end();

    batch.end();
  }

  public void drawPieces(Batch batch) {
    batch.begin();
    for (int i = 0; i < BoardUtils.BOARD_TILES_COL_COUNT; i++) {
      for (int j = 0; j < BoardUtils.BOARD_TILES_ROW_COUNT; j++) {
        TileUI tile = tiles[i][j];
        batch.draw(pieceImg, tile.x, tile.y, tile.width, tile.height);
      }
    }
    batch.end();
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
    game.camera.update();
    game.batch.setProjectionMatrix(game.camera.combined);
    update(delta);

    stage.draw();

    drawBoard(game.batch, shapeRend);
    drawPieces(game.batch);

    game.batch.begin();
    game.font.draw(game.batch, "Generals", 0, GoG.V_HEIGHT);
    game.batch.end();

    // // process user input
    // if(Gdx.input.isTouched()) {
    //   Vector3 touchPos = new Vector3();
    //   touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
    //   game.camera.unproject(touchPos);
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
    pieceImg.dispose();
    stage.dispose();
  }
}

