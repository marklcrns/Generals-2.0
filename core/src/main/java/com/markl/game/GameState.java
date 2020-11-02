package com.markl.game;

import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Board;
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
  private Alliance myAlliance;            // My Player alliance for TileUI orientation
  private Alliance firstMoveMaker;        // First Player to make a move
  private Player gameWinner;              // Game winner
  private Player currentTurnMaker;        // Current player to make move
  private String blackPlayerName = "";    // Black player's name assigned when game initialized
  private String whitePlayerName = "";    // White player's name assigned when game initialized
  private boolean hasGameStarted = false; // Turns true when game started
  private boolean hasGameEnded = false;   // Turns true when game started
  private int currentTurnId;              // Current turn of the game

  /**
   * No-constructor function
   */
  public GameState() {
    this.currentTurnId = 0;
  }

  /**
   * Constructor function for Game.
   *
   * @param board {@link Board} instance.
   */
  public GameState(Board board) {
    this.currentTurnId = 0;
    this.board = board;
  }

  /**
   * Initialize game.
   */
  public void init() {
    // TODO: Implement <27-09-20, Mark Lucernas> //
  }

  /**
   * Start game.
   */
  public boolean start() {
    if (!this.hasGameStarted) {
      if (this.currentTurnMaker == null) {
        setRandomFirstMoveMaker();
        setFirstMoveMaker(this.currentTurnMaker.getAlliance());
      }
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
  }

  /**
   * End game and declare winner.
   */
  public void endGame(Player winner) {
    if (winner == null)
      this.gameWinner = null;
    else
      this.gameWinner = winner;

    this.hasGameEnded = true;
  }

  public void setMyAlliance(Alliance myAlliance) {
    if (currentTurnId == 0) {
      this.myAlliance = myAlliance;
    }
  }

  public void setFirstMoveMaker(Alliance moveMakerAlliance) {
    if (currentTurnId == 0) {
      this.firstMoveMaker = moveMakerAlliance;
      this.currentTurnMaker = board.getPlayer(moveMakerAlliance);
    }
  }

  public void setRandomFirstMoveMaker() {
    if (currentTurnId == 0) {
      if (Math.random() < 0.5f)
        this.currentTurnMaker = board.getPlayer(Alliance.BLACK);
      else
        this.currentTurnMaker = board.getPlayer(Alliance.WHITE);
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

  public void nextTurn() {
    switchTurnMakerPlayer();
    incrementTurnId();
  }

  public void prevTurn() {
    switchTurnMakerPlayer();
    decrementTurnId();
  }

  public void switchTurnMakerPlayer() {
    if (currentTurnMaker.getAlliance() == Alliance.BLACK)
      this.currentTurnMaker = board.getPlayer(Alliance.WHITE);
    else
      this.currentTurnMaker = board.getPlayer(Alliance.BLACK);
  }

  public void decrementTurnId() { if (this.currentTurnId > 0) this.currentTurnId--; }
  public void incrementTurnId() { if (this.currentTurnId > 0) this.currentTurnId++; }

  /** Accessor methods */
  public int getCurrTurn()            { return this.currentTurnId; }
  public Alliance getMyAlliance()     { return this.myAlliance; }
  public Alliance getFirstMoveMaker() { return this.firstMoveMaker; }
  public Player getPlayerWinner()     { if (this.hasGameEnded) return this.gameWinner; else return null; }
  public Player getCurrentTurnMaker() { return this.currentTurnMaker; }
  public String getBlackPlayerName()  { return this.blackPlayerName; }
  public String getWhitePlayerName()  { return this.whitePlayerName; }
  public boolean isRunning() {
    if (this.hasGameStarted && !this.hasGameEnded)
      return true;
    return false;
  }

  /** Modifier methods */
  public void setBoard(Board board)  { this.board = board; }

}
