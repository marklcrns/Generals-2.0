package com.markl.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.Move;
import com.markl.game.engine.board.Player;

/**
 * Game class that stores and control the current state and other important
 * game stats. Also controls the turn of the game.
 *
 * @author Mark Lucernas
 * Date: Sep 27, 2020
 */
public class GameState {

  private Board board;                    // Board instance
  private Alliance firstMoveMaker;        // First Player to make a move
  private Player gameWinner;              // Game winner
  private Alliance myAlliance;            // My Player alliance for TileUI orientation
  private Player myPlayer;                // My Player
  private Player enemyPlayer;             // Enemy Player
  private Player currentTurnMaker;        // Current player to make move
  private String blackPlayerName = "";    // Black player's name assigned when game initialized
  private String whitePlayerName = "";    // White player's name assigned when game initialized
  private Map<Integer, Move> moveHistory; // Move history
  private int currentTurnId;              // Current turn of the game
  private boolean hasGameStarted = false; // Turns true when game started
  private boolean hasGameEnded = false;   // Turns true when game started

  /**
   * No-constructor function
   */
  public GameState() {
    init();
  }

  /**
   * Constructor function for Game.
   *
   * @param board {@link Board} instance.
   */
  public GameState(Board board) {
    this.board = board;
    init();
  }

  /**
   * Initialize game.
   */
  public void init() {
    this.currentTurnId = 0;
    this.moveHistory = new HashMap<Integer, Move>();
  }

  /**
   * Start game.
   */
  public boolean start() {
    if (!this.hasGameStarted) {
      this.currentTurnId = 1;
      this.hasGameStarted = true;
      return true;
    }
    return false;
  }

  /**
   * Start game.
   */
  public boolean start(Alliance firstMoveMaker) {
    if (!this.hasGameStarted) {
      if (this.currentTurnMaker == null) {
        this.currentTurnMaker = board.getPlayer(firstMoveMaker);
        setFirstMoveMaker(firstMoveMaker);
      }
      this.currentTurnId = 1;
      this.hasGameStarted = true;
      return true;
    }
    return false;
  }

  /**
   * Restart game.
   */
  public void restart() {
    // TODO: Implement <27-09-20, Mark Lucernas> //
    this.currentTurnId = 0;
  }

  /**
   * End game and declare winner.
   */
  public void endGame(Player winner) {
    this.gameWinner = winner;
    this.hasGameEnded = true;
    this.currentTurnMaker = null;
  }

  public void setMyAlliance(Alliance myAlliance) {
    if (currentTurnId == 0) {
      this.myAlliance = myAlliance;
    }
  }

  public void setMyPlayer(Alliance alliance, String playerId) {
    if (currentTurnId == 0) {
      Player player = board.getPlayer(alliance);
      player.setId(playerId);
      this.myPlayer = player;
      this.myAlliance = alliance;
    }
  }

  public void setEnemyPlayer(Alliance alliance, String playerId) {
    if (currentTurnId == 0) {
      Player player = board.getPlayer(alliance);
      player.setId(playerId);
      this.enemyPlayer = player;
    }
  }

  public void setFirstMoveMaker(Alliance moveMakerAlliance) {
    if (currentTurnId == 0) {
      this.firstMoveMaker = moveMakerAlliance;
      this.currentTurnMaker = board.getPlayer(moveMakerAlliance);
    }
  }

  public void setRandomMyAlliance() {
    if (currentTurnId == 0) {
      if (Math.random() < 0.5f)
        this.myAlliance = Alliance.BLACK;
      else
        this.myAlliance = Alliance.WHITE;
    }
  }

  public void setRandomFirstMoveMaker() {
    if (currentTurnId == 0) {
      if (Math.random() < 0.5f) {
        this.firstMoveMaker = Alliance.BLACK;
        this.currentTurnMaker = board.getPlayer(Alliance.BLACK);
      } else {
        this.firstMoveMaker = Alliance.WHITE;
        this.currentTurnMaker = board.getPlayer(Alliance.WHITE);
      }
    }
  }

  public void nextTurn() {
    if (isRunning()) {
      switchTurnMakerPlayer();
      incrementTurnId();
    }
  }

  public void prevTurn() {
    if (isRunning()) {
      switchTurnMakerPlayer();
      decrementTurnId();
    }
  }

  public Map<Integer, Move> getMoveHistory() {
    if (moveHistory != null)
      return this.moveHistory;

    return null;
  }

  public String printMoveHistory() {
    String moveHistoryStr = "";

    for (int i = 1; i < moveHistory.size() + 1; i++) {
      if (i == currentTurnId)
        moveHistoryStr += "\n-> " + moveHistory.get(i).toString();
      else
        moveHistoryStr += "\n" + moveHistory.get(i).toString();
    }

    return moveHistoryStr;
  }

  public Move undoMove() {
    if (hasUndo()) {
      Move lastMove = moveHistory.get(currentTurnId - 1);
      Gdx.app.log("GameState.undoMove", lastMove.toString());
      if (lastMove.getTurnId() == currentTurnId - 1 &&
          lastMove.undoExecution()) {
          prevTurn();
        return lastMove;
      }
    }
    return null;
  }

  public Move redoMove() {
    if (hasRedo()) {
      Move nextMove = moveHistory.get(currentTurnId);
      Gdx.app.log("GameState.redoMove", nextMove.toString());
      if (nextMove.getTurnId() == currentTurnId &&
          nextMove.redoExecution()) {
        return nextMove;
      }
    }
    return null;
  }

  public void clearMoveHistoryForward() {
    for (int i = moveHistory.size(); i >= currentTurnId; i--) {
      Move removedMove = moveHistory.remove(i);
      Gdx.app.log("Removed Move", removedMove.toString());
    }
  }

  public void clearMoveHistoryBackward() {
    // TODO: Implement //
  }

  public void switchTurnMakerPlayer() {
    if (currentTurnMaker != null) {
      if (currentTurnMaker.getAlliance() == Alliance.BLACK)
        this.currentTurnMaker = board.getPlayer(Alliance.WHITE);
      else
        this.currentTurnMaker = board.getPlayer(Alliance.BLACK);
    }
  }

  public boolean isRunning() {
    if (this.hasGameStarted && !this.hasGameEnded)
      return true;
    return false;
  }

  public boolean hasUndo() {
    if ((firstMoveMaker == myAlliance && currentTurnId > 1) ||
        (firstMoveMaker != myAlliance && currentTurnId > 2))
      return true;
    return false;
  }

  public boolean hasRedo() {
    if (moveHistory.size() >= currentTurnId)
      return true;
    return false;
  }

  public void decrementTurnId() { if (this.currentTurnId > 0) this.currentTurnId--; }
  public void incrementTurnId() { if (this.currentTurnId > 0) this.currentTurnId++; }

  /** Accessor methods */
  public Alliance getMyAlliance()     { return this.myAlliance; }
  public Alliance getFirstMoveMaker() { return this.firstMoveMaker; }
  public Player getPlayerWinner()     { if (this.hasGameEnded) return this.gameWinner; else return null; }
  public Player getCurrentTurnMaker() { return this.currentTurnMaker; }
  public Player getMyPlayer()         { return this.myPlayer; }
  public Player getEnemyPlayer()      { return this.enemyPlayer; }
  public String getBlackPlayerName()  { return this.blackPlayerName; }
  public String getWhitePlayerName()  { return this.whitePlayerName; }
  public int getCurrTurn()            { return this.currentTurnId; }

  /** Modifier methods */
  public void setBoard(Board board)           { this.board = board; }
  public void setBlackPlayerName(String name) { this.blackPlayerName = name; }
  public void setWhitePlayerName(String name) { this.whitePlayerName = name; }
}
