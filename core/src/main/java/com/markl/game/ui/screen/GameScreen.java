package com.markl.game.ui.screen;

import static com.markl.game.engine.board.BoardUtils.BOARD_TILES_ROW_COUNT;
import static com.markl.game.engine.board.BoardUtils.TOTAL_BOARD_TILES;
import static com.markl.game.engine.board.BoardUtils.getTileColNum;
import static com.markl.game.engine.board.BoardUtils.getTileRowNum;
import static com.markl.game.util.Constants.BOARD_ACTIVE_COLOR;
import static com.markl.game.util.Constants.BOARD_HEIGHT;
import static com.markl.game.util.Constants.BOARD_INACTIVE_COLOR;
import static com.markl.game.util.Constants.BOARD_OUTLINE_COLOR;
import static com.markl.game.util.Constants.BOARD_OUTLINE_THICKNESS;
import static com.markl.game.util.Constants.BOARD_WIDTH;
import static com.markl.game.util.Constants.BOARD_X_OFFSET;
import static com.markl.game.util.Constants.BOARD_Y_OFFSET;
import static com.markl.game.util.Constants.TILE_ACTIVE_COLOR;
import static com.markl.game.util.Constants.TILE_ACTIVE_PIECE_COLOR;
import static com.markl.game.util.Constants.TILE_AGGRESSIVE_HIGHLIGHT_COLOR;
import static com.markl.game.util.Constants.TILE_BORDER_COLOR;
import static com.markl.game.util.Constants.TILE_INVALID_HIGHLIGHT_COLOR;
import static com.markl.game.util.Constants.TILE_NORMAL_HIGHLIGHT_COLOR;
import static com.markl.game.util.Constants.TILE_PREV_AGGRESSIVE_MOVE_COLOR;
import static com.markl.game.util.Constants.TILE_PREV_NORMAL_MOVE_COLOR;
import static com.markl.game.util.Constants.TILE_SIZE;
import static com.markl.game.util.Constants.VIEWPORT_HEIGHT;
import static com.markl.game.util.Constants.VIEWPORT_WIDTH;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.markl.game.Gog;
import com.markl.game.ai.minimax.AIDumb;
import com.markl.game.ai.minimax.AIMinimax;
import com.markl.game.control.MoveManager;
import com.markl.game.control.PieceUIManager;
import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.BoardBuilder;
import com.markl.game.engine.board.Move;
import com.markl.game.engine.board.Move.MoveType;
import com.markl.game.engine.board.Player;
import com.markl.game.engine.board.pieces.Piece;
import com.markl.game.network.ServerSocket;
import com.markl.game.ui.Application;
import com.markl.game.ui.board.TileUI;
import com.markl.game.ui.screen.window.AiDebuggerWindow;
import com.markl.game.util.Constants;

/**
 * @author Mark Lucernas
 * Date: Oct 03, 2020
 */
public class GameScreen implements Screen {

  public enum GameMode {
    SINGLE, LOCAL, ONLINE;
  }

  public Stage stage;
  public ShapeRenderer shapeRend;

  public Application app;
  public GameMode gameMode;
  public Gog gog;
  public Board board;
  public PieceUIManager pieceUIManager;
  public MoveManager moveManager;
  public BoardBuilder boardBuilder;
  public Player playerBlack;
  public Player playerWhite;
  public LinkedList<TileUI> tilesUI;  // List of all Tiles containing data of each piece
  public Map<String, AtlasRegion> blackPiecesTex = new HashMap<>();
  public Map<String, AtlasRegion> whitePiecesTex = new HashMap<>();
  public boolean isBoardInverted;

  // TODO: Debug
  public float tX = -1;
  public float tY = -1;
  public float pX = -1;
  public float pY = -1;
  public TileUI activeTileUI;
  public TileUI destTileUI;
  public Piece activeSrcPiece;
  public Move prevMove;

  public GameScreenHUD hud;
  public AiDebuggerWindow aiDebuggerWin;

  // Network
  public ServerSocket serverSocket;

  public GameScreen(final Application app, GameMode gameMode) {
    this.app = app;
    this.gameMode = gameMode;
  }

  @Override
  public void show() {
    Gdx.app.log("GameScreen", "show " + gameMode);
    Gdx.graphics.setContinuousRendering(false);
    this.shapeRend = new ShapeRenderer();
    this.stage = new Stage(new StretchViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, app.camera));
    this.hud = new GameScreenHUD(this);

    // TODO: Delete. For testing only!
    aiDebuggerWin = new AiDebuggerWindow("Test", app.uiskin, this);
    aiDebuggerWin.setWidth(VIEWPORT_WIDTH - (VIEWPORT_WIDTH / 5));
    aiDebuggerWin.setHeight(VIEWPORT_HEIGHT - (VIEWPORT_HEIGHT / 5));
    aiDebuggerWin.setZIndex(9999);

    hud.rebuildGameHUD();
    getAssetImages();

    this.tilesUI = new LinkedList<TileUI>();
    this.pieceUIManager = new PieceUIManager(this);

    Gdx.input.setInputProcessor(stage);

    if (gameMode == GameMode.SINGLE) {
      initEngine();
      boardBuilder.createTestBuild();
      gog.setMyPlayer(Alliance.WHITE, "white");
      gog.setEnemyPlayer(Alliance.BLACK, "black");
      gog.addAI(new AIMinimax(3, this, AIMinimax.Disposition.AGGRESSIVE), Alliance.BLACK);
      // gog.addAI(new AIDumb(), Alliance.BLACK);
      initBoardUI();
      initGame(Alliance.WHITE);

      // Make AI move if first move maker
      if (gog.getCurrTurnMakerPlayer().getAlliance() == Alliance.BLACK) {
        Move aiMove = gog.getAI().generateMove();
        if (aiMove != null)
          moveManager.makeMove(aiMove.getSrcTileId(), aiMove.getTgtTileId(), false, true, true);
      }

    } else if (gameMode == GameMode.LOCAL) {
      initEngine();
      boardBuilder.createBoardRandomBuild();
      initBoardUI();
      initGame();
    } else if (gameMode == GameMode.ONLINE) {
      // Connect to server
      this.serverSocket = new ServerSocket("localhost", 8080, this);
      serverSocket.connectSocket();
      serverSocket.configSocketEvents();
    }
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

    String currTurnMaker;
    int currTurn;

    if (gog != null) {
      // Draw game peripherals
      app.batch.begin();
      if (gog.isRunning())
        currTurnMaker = gog.getCurrTurnMakerPlayer().getAlliance().name();
      else
        currTurnMaker = "WAITING";
      currTurn = gog.getCurrTurn();
      app.font.draw(app.batch, "PLAYER: " + currTurnMaker, 0, VIEWPORT_HEIGHT);
      app.font.draw(app.batch, "TURN: " + currTurn, BOARD_X_OFFSET, BOARD_Y_OFFSET);
      app.batch.end();

      // Draw game board
      app.batch.begin();
      drawBoard();
      drawTileHighlights();
      stage.draw();
      drawActiveTileHighlights();
      drawTileSnapLinePath();     // For debugging snap-to-tile
      app.batch.end();
    }
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
    this.stage.dispose();
    this.shapeRend.dispose();
  }

  public void initEngine() {
    // Initialize GoG game engine
    this.gog = app.gog;
    this.board = gog.getBoard();
    this.boardBuilder = gog.getBoardBuilder();
    if (gameMode == GameMode.ONLINE)
      this.moveManager = new MoveManager(this);
    else
      this.moveManager = new MoveManager(this);
  }

  public void initBoardUI() {
    for (int i = 0; i < TOTAL_BOARD_TILES; i++) {

      float tileWidth = TILE_SIZE;
      float tileHeight = TILE_SIZE;
      float tileX = getTileColNum(i) * tileWidth + BOARD_X_OFFSET;
      // Invert Y to have tiles arranged left to right, top to bottom
      float tileY;
      if (isBoardInverted) {
        tileY = (getTileRowNum(i) * TILE_SIZE) + BOARD_Y_OFFSET;
      } else {
        tileY = (TILE_SIZE * (BOARD_TILES_ROW_COUNT - 1)) -
          (getTileRowNum(i) * TILE_SIZE) + BOARD_Y_OFFSET;
      }

      TileUI newTileUI = new TileUI(i, tileX, tileY, tileWidth, tileHeight);
      this.tilesUI.add(newTileUI);
    }
  }

  public void populateTilesUI() {
    Iterator<TileUI> iterator = tilesUI.iterator();

    while (iterator.hasNext()) {
      TileUI tileUI = iterator.next();
      // Create PieceUI as stage actor if tile is occupied
      if (board.getTile(tileUI.getTileId()).isTileOccupied()) {
        pieceUIManager.generatePieceUI(tileUI.getTileId());
      }
    }

    if (gameMode == GameMode.ONLINE || gameMode == GameMode.SINGLE) {
      if (gog.getMyAlliance() == Alliance.WHITE)
        pieceUIManager.hidePieceUISet(Alliance.BLACK);
      else
        pieceUIManager.hidePieceUISet(Alliance.WHITE);
    } else {
      if (gog.getCurrTurnMakerPlayer().getAlliance() == Alliance.WHITE)
        pieceUIManager.hidePieceUISet(Alliance.BLACK);
      else
        pieceUIManager.hidePieceUISet(Alliance.BLACK);
    }
  }

  public void initGame() {
    // Create initial board arrangement and start game
    boardBuilder.build(true);
    board.initGame();
    gog.setRandomFirstMoveMaker();
    gog.start();
    populateTilesUI();
  }

  public void initGame(Alliance firstMoveMaker) {
    // Create initial board arrangement with firstMoveMaker and start game
    boardBuilder.build(true);
    board.initGame();
    gog.setFirstMoveMaker(firstMoveMaker);
    gog.start();
    populateTilesUI();
  }

  public void update(float delta) {
    stage.act(delta);
  }

  public void drawBoard() {
    Iterator<TileUI> iterator;

    shapeRend.begin(ShapeType.Filled);
    iterator = tilesUI.iterator();
    while (iterator.hasNext()) {
      TileUI tileUI = iterator.next();

      if (gog.isRunning()) {
        shapeRend.setColor(BOARD_ACTIVE_COLOR);
      } else {
        shapeRend.setColor(BOARD_INACTIVE_COLOR);
      }

      // Draw square tile
      shapeRend.rect(tileUI.x, tileUI.y, tileUI.width, tileUI.height);
    }
    shapeRend.end();

    // Draw active pieces tile borders
    if (gog.isRunning()) {
      shapeRend.begin(ShapeType.Filled);
      iterator = tilesUI.iterator();
      while (iterator.hasNext()) {
        TileUI tileUI = iterator.next();

        if (tileUI.isTileUIOccupied()) {
          if (tileUI.getPieceUI().alliance == gog.getCurrTurnMakerPlayer().getAlliance()) {
            shapeRend.setColor(TILE_ACTIVE_COLOR);
            shapeRend.rect(tileUI.x, tileUI.y, tileUI.width, tileUI.height);
          }
        }
      }
      shapeRend.end();
    }

    // Draw tile borders
    shapeRend.begin(ShapeType.Line);
    iterator = tilesUI.iterator();
    while (iterator.hasNext()) {
      TileUI tileUI = iterator.next();
      shapeRend.setColor(TILE_BORDER_COLOR);
      shapeRend.rect(tileUI.x, tileUI.y, tileUI.width, tileUI.height);
    }
    shapeRend.end();

    // Draw board outline territory separation line
    shapeRend.begin(ShapeType.Filled);
    float x1, y1, x2, y2;
    shapeRend.setColor(BOARD_OUTLINE_COLOR);

    // Board territory separation line
    x1 = BOARD_X_OFFSET;
    y1 = BOARD_Y_OFFSET + (BOARD_HEIGHT / 2);
    x2 = x1 + BOARD_WIDTH;
    y2 = y1;
    shapeRend.rectLine(x1, y1, x2, y2, BOARD_OUTLINE_THICKNESS / 2);

    // Board left segment outline
    x1 = BOARD_X_OFFSET;
    y1 = BOARD_Y_OFFSET;
    x2 = x1;
    y2 = y1 + BOARD_HEIGHT;
    shapeRend.rectLine(x1, y1, x2, y2, BOARD_OUTLINE_THICKNESS);

    // Board upper segment outline
    x1 = BOARD_X_OFFSET;
    y1 = BOARD_Y_OFFSET + BOARD_HEIGHT;
    x2 = BOARD_X_OFFSET + BOARD_WIDTH;
    y2 = y1;
    shapeRend.rectLine(x1, y1, x2, y2, BOARD_OUTLINE_THICKNESS);

    // Board right segment outline
    x1 = BOARD_X_OFFSET + BOARD_WIDTH;
    y1 = BOARD_Y_OFFSET + BOARD_HEIGHT;
    x2 = BOARD_X_OFFSET + BOARD_WIDTH;
    y2 = BOARD_Y_OFFSET;
    shapeRend.rectLine(x1, y1, x2, y2, BOARD_OUTLINE_THICKNESS);

    // Board lower segment outline
    x1 = BOARD_X_OFFSET + BOARD_WIDTH;
    y1 = BOARD_Y_OFFSET;
    x2 = BOARD_X_OFFSET;
    y2 = BOARD_Y_OFFSET;
    shapeRend.rectLine(x1, y1, x2, y2, BOARD_OUTLINE_THICKNESS);

    shapeRend.end();
  }

  // Draw snap-to-tile tile highlight
  public void drawTileHighlights() {
    // Only highlight legal move tile destination when dragged over
    boolean isHighlight = false;
    if (activeSrcPiece != null && destTileUI != null) {
      for (Map.Entry<Integer, Move> entry : activeSrcPiece.moveSet.entrySet()) {
        if (entry.getValue().getTgtTileId() == destTileUI.getTileId()) {
          isHighlight = true;
          break;
        }
      }
    }

    shapeRend.begin(ShapeType.Filled);
    // Draw previous move target tile
    if (prevMove != null) {
      TileUI tgtTileUI = tilesUI.get(prevMove.getTgtTileId());
      MoveType moveType = prevMove.getMoveType();

      if (moveType == MoveType.NORMAL) {
        shapeRend.setColor(TILE_PREV_NORMAL_MOVE_COLOR);
        shapeRend.rect(tgtTileUI.x, tgtTileUI.y, tgtTileUI.width, tgtTileUI.height);
      } else if (moveType == MoveType.DRAW) {
        shapeRend.setColor(TILE_PREV_AGGRESSIVE_MOVE_COLOR);
        shapeRend.rect(tgtTileUI.x, tgtTileUI.y, tgtTileUI.width, tgtTileUI.height);
      } else if (moveType == MoveType.AGGRESSIVE_WIN) {
        shapeRend.setColor(TILE_PREV_NORMAL_MOVE_COLOR);
        shapeRend.setColor(TILE_PREV_AGGRESSIVE_MOVE_COLOR);
        shapeRend.rect(tgtTileUI.x, tgtTileUI.y, tgtTileUI.width, tgtTileUI.height);
      } else if (moveType == MoveType.AGGRESSIVE_LOSE) {
        shapeRend.setColor(TILE_PREV_AGGRESSIVE_MOVE_COLOR);
        shapeRend.setColor(TILE_PREV_NORMAL_MOVE_COLOR);
        shapeRend.rect(tgtTileUI.x, tgtTileUI.y, tgtTileUI.width, tgtTileUI.height);
      }
    }
    shapeRend.end();

    shapeRend.begin(ShapeType.Line);
    // Draw active tile highlight
    if (activeTileUI != null) {
      shapeRend.setColor(TILE_ACTIVE_PIECE_COLOR);
      shapeRend.rect(activeTileUI.x, activeTileUI.y, activeTileUI.width, activeTileUI.height);
    }

    // Draw previous move source tile
    if (prevMove != null) {
      TileUI srcTileUI = tilesUI.get(prevMove.getSrcTileId());
      MoveType moveType = prevMove.getMoveType();

      if (moveType == MoveType.NORMAL) {
        shapeRend.setColor(TILE_PREV_NORMAL_MOVE_COLOR);
        shapeRend.rect(srcTileUI.x, srcTileUI.y, srcTileUI.width, srcTileUI.height);
      } else if (moveType == MoveType.DRAW) {
        shapeRend.setColor(TILE_PREV_AGGRESSIVE_MOVE_COLOR);
        shapeRend.rect(srcTileUI.x, srcTileUI.y, srcTileUI.width, srcTileUI.height);
      } else if (moveType == MoveType.AGGRESSIVE_WIN) {
        shapeRend.setColor(TILE_PREV_NORMAL_MOVE_COLOR);
        shapeRend.rect(srcTileUI.x, srcTileUI.y, srcTileUI.width, srcTileUI.height);
        shapeRend.setColor(TILE_PREV_AGGRESSIVE_MOVE_COLOR);
      } else if (moveType == MoveType.AGGRESSIVE_LOSE) {
        shapeRend.setColor(TILE_PREV_AGGRESSIVE_MOVE_COLOR);
        shapeRend.rect(srcTileUI.x, srcTileUI.y, srcTileUI.width, srcTileUI.height);
        shapeRend.setColor(TILE_PREV_NORMAL_MOVE_COLOR);
      }
    }

    if (isHighlight && destTileUI != null && destTileUI.getTileId() != activeTileUI.getTileId()) {
      // Highlight destination Tile background based on move type
      if (board.getTile(destTileUI.getTileId()).isTileOccupied()) {
        if (board.getTile(activeTileUI.getTileId()).getPiece().getAlliance() !=
            board.getTile(destTileUI.getTileId()).getPiece().getAlliance())
          shapeRend.setColor(TILE_AGGRESSIVE_HIGHLIGHT_COLOR); // Aggressive move tile highlight
        else
          shapeRend.setColor(TILE_INVALID_HIGHLIGHT_COLOR);    // Invalid friendly-fire move tile highlight
      } else {
        shapeRend.setColor(TILE_NORMAL_HIGHLIGHT_COLOR);       // Normal move tile highlight
      }

      shapeRend.rect(destTileUI.x, destTileUI.y, destTileUI.width, destTileUI.height);
    }

    shapeRend.end();
  }

  public void drawActiveTileHighlights() {
    // Highlight origin Tile border
    if (activeTileUI != null) {
      shapeRend.begin(ShapeType.Filled);

      if (activeSrcPiece != null) {
        for (Map.Entry<Integer, Move> entry : activeSrcPiece.moveSet.entrySet()) {

          // Skip if hovering active piece hovering on destination tile
          if (destTileUI != null) {
            if (entry.getValue().getTgtTileId() == destTileUI.getTileId()) {
              continue;
            }
          }

          if (entry.getValue().getMoveType().getValue() != -1 &&
              entry.getValue().getTgtTileId() != activeTileUI.getTileId()) {
            // Highlight destination Tile background based on move type
            if (entry.getValue().getMoveType().getValue() != 1) {
              shapeRend.setColor(TILE_AGGRESSIVE_HIGHLIGHT_COLOR);
            } else {
              shapeRend.setColor(TILE_NORMAL_HIGHLIGHT_COLOR);
            }

            TileUI candidateTileUI = tilesUI.get(entry.getValue().getTgtTileId());
            shapeRend.circle(candidateTileUI.x + (candidateTileUI.width / 2),
                candidateTileUI.y + (candidateTileUI.height / 2), 4);
          }

        }
      }
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

  private void getAssetImages() {
    TextureAtlas atlas = app.assets.get(Constants.PIECE_ATLAS_PATH, TextureAtlas.class);

    // Get black pieces
    blackPiecesTex.put("GeneralFive", atlas.findRegion("black/GeneralFive"));
    blackPiecesTex.put("GeneralFour", atlas.findRegion("black/GeneralFour"));
    blackPiecesTex.put("GeneralThree", atlas.findRegion("black/GeneralThree"));
    blackPiecesTex.put("GeneralTwo", atlas.findRegion("black/GeneralTwo"));
    blackPiecesTex.put("GeneralOne", atlas.findRegion("black/GeneralOne"));
    blackPiecesTex.put("Colonel", atlas.findRegion("black/Colonel"));
    blackPiecesTex.put("LtCol", atlas.findRegion("black/LtCol"));
    blackPiecesTex.put("Major", atlas.findRegion("black/Major"));
    blackPiecesTex.put("Captain", atlas.findRegion("black/Captain"));
    blackPiecesTex.put("LtOne", atlas.findRegion("black/LtOne"));
    blackPiecesTex.put("LtTwo", atlas.findRegion("black/LtTwo"));
    blackPiecesTex.put("Sergeant", atlas.findRegion("black/Sergeant"));
    blackPiecesTex.put("Private", atlas.findRegion("black/Private"));
    blackPiecesTex.put("Spy", atlas.findRegion("black/Spy"));
    blackPiecesTex.put("Flag", atlas.findRegion("black/Flag"));
    blackPiecesTex.put("Hidden", atlas.findRegion("black/Hidden"));

    // Get white pieces
    whitePiecesTex.put("GeneralFive", atlas.findRegion("white/GeneralFive"));
    whitePiecesTex.put("GeneralFour", atlas.findRegion("white/GeneralFour"));
    whitePiecesTex.put("GeneralThree", atlas.findRegion("white/GeneralThree"));
    whitePiecesTex.put("GeneralTwo", atlas.findRegion("white/GeneralTwo"));
    whitePiecesTex.put("GeneralOne", atlas.findRegion("white/GeneralOne"));
    whitePiecesTex.put("Colonel", atlas.findRegion("white/Colonel"));
    whitePiecesTex.put("LtCol", atlas.findRegion("white/LtCol"));
    whitePiecesTex.put("Major", atlas.findRegion("white/Major"));
    whitePiecesTex.put("Captain", atlas.findRegion("white/Captain"));
    whitePiecesTex.put("LtOne", atlas.findRegion("white/LtOne"));
    whitePiecesTex.put("LtTwo", atlas.findRegion("white/LtTwo"));
    whitePiecesTex.put("Sergeant", atlas.findRegion("white/Sergeant"));
    whitePiecesTex.put("Private", atlas.findRegion("white/Private"));
    whitePiecesTex.put("Spy", atlas.findRegion("white/Spy"));
    whitePiecesTex.put("Flag", atlas.findRegion("white/Flag"));
    whitePiecesTex.put("Hidden", atlas.findRegion("white/Hidden"));
  }

  public String ascii() {
    String debugBoard = "\nGameScreen Debug Board\n";
    debugBoard += "    0 1 2 3 4 5 6 7 8\n";
    debugBoard += "    _________________\n";
    for (int i = 0; i < TOTAL_BOARD_TILES / 2; i += 9) {
      if (i < 10)
        debugBoard += " " + i + " |";
      else
        debugBoard += i + " |";
      for (int j = i; j < i + 9; j++) {
        if (tilesUI.get(j).isTileUIEmpty()) {
          debugBoard += "-";
        } else {
          final String rank = tilesUI.get(j).getPieceUI().pieceRank;
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
        if (tilesUI.get(j).isTileUIEmpty()) {
          debugBoard += "-";
        } else {
          final String rank = tilesUI.get(j).getPieceUI().pieceRank;
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

  /**
   * @return String representation of all current TilesUI for debugging
   */
  @Override
  public String toString() {
    return "Implement GameScreen toString()";
  }
}
