package com.markl.game;

import com.markl.game.engine.board.Board;

/**
 * Game class that stores and control the current state and other important
 * game stats. Also controls the turn of the game.
 *
 * @author Mark Lucernas
 * Date: Sep 27, 2020
 */
public class Game {

    public int turnId;                     // Current turn of the game
    public Board board;                    // Board instance
    public boolean hasGameStarted = false; // Turns true when game started
    private String playerBlackName;        // Black player's name assigned when game initialized
    private String playerWhiteName;        // White player's name assigned when game initialized

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
        this.turnId++;
    }

    /**
     * Restart game.
     */
    public void restart() {
        // TODO: Implement <27-09-20, Mark Lucernas> //
    }
}
