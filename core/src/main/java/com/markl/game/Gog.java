package com.markl.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.markl.game.ai.minimax.AI;
import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.BoardBuilder;
import com.markl.game.engine.board.Move;
import com.markl.game.engine.board.Player;

/**
 * Game class that stores and control the current state and other important
 * game stats. Also controls the turn of the game.
 *
 * @author Mark Lucernas
 * Date: Sep 27, 2020
 */
public class Gog {

	private Alliance firstMoveMaker;        // First Player to make a move
	private Alliance myAlliance;            // My Player alliance for TileUI orientation
	private Player myPlayer;                // My Player
	private Player enemyPlayer;             // Enemy Player
	private Alliance currentTurnMaker;      // Current player to make move
	private String blackPlayerName = "";    // Black player's name assigned when game initialized
	private String whitePlayerName = "";    // White player's name assigned when game initialized
	private Map<Integer, Move> moveHistory; // Move history
	private Player gameWinner;              // Game winner

	private Board board;                    // Board instance
	private BoardBuilder boardBuilder;
	private Player blackPlayer;             // Player instance that all contains all infos on black pieces
	private Player whitePlayer;             // Player instance that all contains all infos on white pieces
	private AI ai;
	private boolean hasGameStarted = false; // Turns true when game started
	private boolean hasGameEnded = false;   // Turns true when game started
	private int currentTurnId = 0;              // Current turn of the game
	private int blackPiecesCount = 0;
	private int whitePiecesCount = 0;

	/**
	 * No-constructor function
	 */
	public Gog() {
		this.board = new Board(this);
		init();
	}

	/**
	 * Constructor function for Game.
	 *
	 * @param board {@link Board} instance.
	 */
	public Gog(Board board) {
		this.board = board;
		board.setGog(this);
		init();
	}

	/**
	 * Initialize game.
	 */
	public void init() {
		this.moveHistory = new HashMap<Integer, Move>();
		this.blackPlayer = new Player(Alliance.BLACK);
		this.whitePlayer = new Player(Alliance.WHITE);
		this.boardBuilder = new BoardBuilder(board);
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
				setFirstMoveMaker(firstMoveMaker);
				this.currentTurnMaker = firstMoveMaker;
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
	}

	public void restoreGame() {
		this.gameWinner = null;
		this.hasGameEnded = false;
	}

	public void setMyAlliance(Alliance myAlliance) {
		if (currentTurnId == 0) {
			this.myAlliance = myAlliance;
		}
	}

	public void setMyPlayer(Alliance alliance, String playerId) {
		if (currentTurnId == 0) {
			Player player = getPlayer(alliance);
			player.setId(playerId);
			this.myPlayer = player;
			this.myAlliance = alliance;
		}
	}

	public void setEnemyPlayer(Alliance alliance, String playerId) {
		if (currentTurnId == 0) {
			Player player = getPlayer(alliance);
			player.setId(playerId);
			this.enemyPlayer = player;
		}
	}

	public void setFirstMoveMaker(Alliance moveMakerAlliance) {
		if (currentTurnId == 0) {
			this.firstMoveMaker = moveMakerAlliance;
			this.currentTurnMaker = firstMoveMaker;
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
				this.currentTurnMaker = Alliance.BLACK;
			} else {
				this.firstMoveMaker = Alliance.WHITE;
				this.currentTurnMaker = Alliance.WHITE;
			}
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
			// Gdx.app.log("Gog", "undoMove(): " + lastMove.toString());
			if (lastMove.getTurnId() == currentTurnId - 1 &&
					lastMove.undoExecution()) {
				prevTurn();
				// Gdx.app.log("Gog", board.ascii());
				return lastMove;
					}
		}
		return null;
	}

	public Move redoMove() {
		if (hasRedo()) {
			Move nextMove = moveHistory.get(currentTurnId);
			// Gdx.app.log("Gog", "redoMove(): " + nextMove.toString());
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
			// TODO: DELETE ME //
			// Gdx.app.log("Removed Move", removedMove.toString());
		}
	}

	public void clearMoveHistoryBackward() {
		// TODO: Implement //
	}

	public void switchTurnMakerPlayer() {
		if (currentTurnMaker != null) {
			if (currentTurnMaker == Alliance.BLACK)
				this.currentTurnMaker = Alliance.WHITE;
			else
				this.currentTurnMaker = Alliance.BLACK;
		}
	}

	public boolean isRunning() {
		if (this.hasGameStarted && !this.hasGameEnded)
			return true;
		return false;
	}

	public boolean hasUndo() {
		if (currentTurnId > 1)
			return true;
		return false;
	}

	public boolean hasRedo() {
		if (moveHistory.size() >= currentTurnId)
			return true;
		return false;
	}

	/**
	 * Gets specific Player currently registered in this Board based on the
	 * alliance.
	 * @param alliance Alliance of the Player.
	 * @return Player based on the alliance param.
	 */
	public Player getPlayer(Alliance alliance) {
		if (alliance == Alliance.WHITE)
			return whitePlayer;
		else if (alliance == Alliance.BLACK)
			return blackPlayer;
		else
			return null;
	}

	/**
	 * Sets the required black Player instance.
	 * @param player black Player instance.
	 */
	public void setPlayerBlack(final Player player) {
		this.blackPlayer = player;
	}

	/**
	 * Sets the required white Player instance.
	 * @param player white Player instance.
	 */
	public void setPlayerWhite(final Player player) {
		this.whitePlayer = player;
	}

	public void incBlackPiecesCount() {
		this.blackPiecesCount++;
	}

	public void incWhitePiecesCount() {
		this.whitePiecesCount++;
	}

	public void decWhitePiecesCount() {
		if (this.whitePiecesCount > 0)
			this.whitePiecesCount--;
	}

	public void decBlackPiecesCount() {
		if (this.blackPiecesCount > 0)
			this.blackPiecesCount--;
	}

	public int incTotalPiecesCount(Alliance alliance) {
		if (alliance == Alliance.WHITE)
			incWhitePiecesCount();
		else if (alliance == Alliance.BLACK)
			incBlackPiecesCount();

		return this.whitePiecesCount + this.blackPiecesCount;
	}

	public int decTotalPiecesCount(Alliance alliance) {
		if (alliance == Alliance.WHITE)
			decWhitePiecesCount();
		else if (alliance == Alliance.BLACK)
			decBlackPiecesCount();

		return this.whitePiecesCount + this.blackPiecesCount;
	}

	public void setBlackPiecesCount(int blackPiecesCount) {
		this.blackPiecesCount = blackPiecesCount;
	}

	public void setWhitePiecesCount(int whitePiecesCount) {
		this.whitePiecesCount = whitePiecesCount;
	}

	public void addAI(AI ai, Alliance aiAlliance) {
		ai.setGog(this);
		ai.setAIAlliance(aiAlliance);
		this.ai = ai;
	}

	public AI getAI() {
		return this.ai;
	}

	public void decrementTurnId() { if (this.currentTurnId > 0) this.currentTurnId--; }
	public void incrementTurnId() { if (this.currentTurnId > 0) this.currentTurnId++; }

	/** Accessor methods */
	public int getCurrTurn()               { return this.currentTurnId; }
	public Alliance getMyAlliance()        { return this.myAlliance; }
	public Alliance getFirstMoveMaker()    { return this.firstMoveMaker; }
	public Player getPlayerWinner()        { if (this.hasGameEnded) return this.gameWinner; else return null; }
	public Player getMyPlayer()            { return this.myPlayer; }
	public Player getEnemyPlayer()         { return this.enemyPlayer; }
	public String getBlackPlayerName()     { return this.blackPlayerName; }
	public String getWhitePlayerName()     { return this.whitePlayerName; }
	public Alliance getCurrTurnMaker()     { return this.currentTurnMaker; }
	public Player getCurrTurnMakerPlayer() { return getPlayer(this.currentTurnMaker); }
	public Board getBoard()                { return this.board; }
	public BoardBuilder getBoardBuilder()  { return this.boardBuilder; }

	/** Modifier methods */
	public void setBoard(Board board)           { this.board = board; }
	public void setBlackPlayerName(String name) { this.blackPlayerName = name; }
	public void setWhitePlayerName(String name) { this.whitePlayerName = name; }

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Gog newGameState = new Gog();

		newGameState.setFirstMoveMaker(firstMoveMaker);
		newGameState.setMyAlliance(myAlliance);

		return super.clone();
	}
}
