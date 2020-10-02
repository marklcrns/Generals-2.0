package com.markl.game;

import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.Player;

/**
 * Game class that stores and control the current state and other important
 * game stats. Also controls the turn of the game.
 *
 * @author Mark Lucernas
 * Date: Sep 27, 2020
 */
public class Game {

    private int turnId;                     // Current turn of the game
    private Board board;                    // Board instance
    private Player gameWinner;              // Game winner
    private String blackPlayerName = "";         // Black player's name assigned when game initialized
    private String whitePlayerName = "";         // White player's name assigned when game initialized
    private boolean hasGameStarted = false; // Turns true when game started
    private boolean hasGameEnded = false;   // Turns true when game started

    /**
     * No-constructor function
     */
    public Game() {
        this.turnId = 0;
    }

    /**
     * Constructor function for Game.
     *
     * @param board {@link Board} instance.
     */
    public Game(Board board) {
        this.turnId = 0;
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
    public void start() {
        if (!this.hasGameStarted)
            this.turnId = 1;
        // TODO: Continue implementation <02-10-20, Mark Lucernas> //
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

    public void nextTurn()             { this.turnId++; }
    public void prevTurn()             { if (this.turnId > 0) this.turnId--; }

    /** Accessor methods */
    public int getCurrTurn()           { return this.turnId; }
    public Player getGameWinner()      { if (this.hasGameEnded) return this.gameWinner; else return null; }
    public String getBlackPlayerName() { return this.blackPlayerName; }
    public String getWhitePlayerName() { return this.whitePlayerName; }

    /** Modifier methods */
    public void setBoard(Board board)  { this.board = board; }
}
