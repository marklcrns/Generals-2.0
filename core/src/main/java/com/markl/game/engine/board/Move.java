package com.markl.game.engine.board;

import com.badlogic.gdx.Gdx;
import com.markl.game.Gog;
import com.markl.game.engine.board.pieces.Piece;

/**
 * Move class that serves as the mobility of each Piece. All pieces movement are
 * the same, i.e. a step forwards, backwards and sidewards. Also, Move class
 * evaluates all the four possible moves and categorized them as one of the
 * following, "aggressive", "draw", "normal", and "invalid." Finally, Move class
 * checks to see if the executing move will conclude the game and declare the
 * winner.
 *
 * Author: Mark Lucernas
 * Date: Sep 20, 2020
 */
public class Move {

	/**
	 * Enum class for Move typification
	 *
	 * INVALID         = -1
	 * DRAW            =  0
	 * NORMAL          =  1
	 * AGGRESSIVE_WIN  =  2
	 * AGGRESSIVE_LOSE =  3
	 */
	public enum MoveType {
		INVALID(-1), DRAW(0), NORMAL(1), AGGRESSIVE_WIN(2), AGGRESSIVE_LOSE(3);
		private final int value;

		MoveType(final int value) { this.value = value; }
		public int getValue() { return this.value; }
	}

	private int turnId;                 // Turn ID that serves as reference.
	private final Board board;          // Reference to the Board to execute the move in
	private final Gog gog;              // Reference to the Game
	private final Player player;        // Reference to the Player that owns the Piece to be moved.
	private final int srcTileId;        // Location of the occupied Tile in which the piece to be moved.
	private final int tgtTileId;        // Location of the Tile to where the source piece will potentially move into
	private MoveType moveType;          // Move type to determine the behavior of piece relocation
	private Piece srcPieceOrigin;       // Copy of the source piece
	private Piece tgtPieceOrigin;       // Copy of the target piece if move type is aggressive or draw
	private Piece eliminatedPiece;      // Copy of the eliminated piece if move type is aggressive
	private boolean isExecuted = false; // boolean that holds if the this Move instance has bee executed

	private MoveType bias = null;

	/**
	 * Constructor that takes in the player who will move the piece, board,
	 * source tile coordinates and target tile coordinates.
	 *
	 * @param player     Player reference making the move.
	 * @param board      Board reference.
	 * @param srcTileId  location of the Tile containing a piece to be moved.
	 * @param tgtTileId  location of the destination of the piece to be moved.
	 */
	public Move(final Player player, final Board board,
			final int srcTileId, final int tgtTileId) {
		this.player    = player;
		this.board     = board;
		this.gog      = board.getGog();
		this.turnId    = this.gog.getCurrTurn();
		this.srcTileId = srcTileId;
		this.tgtTileId = tgtTileId;
	}

	/**
	 * Evaluate the move based on the target Tile id and the source piece
	 * to be moved.
	 *
	 * INVALID         = if target Tile contains friendly piece Alliance.
	 * DRAW            = if target Tile contains opposing piece and has the same
	 *                   rank, with the exception of Flag rank.
	 * NORMAL          = if target Tile is empty.
	 * AGGRESSIVE_WIN  = if target Tile contains opposing lower ranking piece.
	 * AGGRESSIVE_LOSE = if target Tile contains opposing higher ranking piece.
	 */
	public void evaluate() {
		// Make source piece origin copy
		this.srcPieceOrigin = this.board.getTile(srcTileId).getPiece().clone();
		// Make target piece origin copy if exist
		if (board.getTile(tgtTileId).isTileOccupied())
			this.tgtPieceOrigin = this.board.getTile(tgtTileId).getPiece().clone();
		else
			this.tgtPieceOrigin = null;

		// Evaluate with bias regardless of engaging pieces ranks
		if (this.bias != null) {
			this.moveType = this.bias;
			return;
		}

		// Return if out of bounds or edge piece wrapping
		if (isOutOfBounds() || isSrcEdgePieceWrapping()) {
			this.moveType = MoveType.INVALID;
			return;
		}

		if (isMoveLegal()) {
			if (board.getTile(tgtTileId).isTileOccupied())
				if (isFriendlyFire())
					this.moveType = MoveType.INVALID;
				else
					if (isSameRank() && isTgtPieceFlag())
						this.moveType = MoveType.AGGRESSIVE_WIN;
					else if (isSameRank())
						this.moveType = MoveType.DRAW;
					else
						if (isTgtPieceEliminated())
							this.moveType = MoveType.AGGRESSIVE_WIN;
						else
							this.moveType = MoveType.AGGRESSIVE_LOSE;
			else
				this.moveType = MoveType.NORMAL;

			return;
		}

		this.moveType = MoveType.INVALID;
	}

	/**
	 * Executes this Move instance and actuate the Move to reflect the changes
	 * in the Board.
	 *
	 * @return boolean true if successful, else false.
	 */
	public boolean execute() {
		switch (this.moveType.getValue()) {
			case -1: // INVALID
				Gdx.app.log(this.getClass().getName(), "Invalid Move");
				return false;

			case 0: // DRAW
				// Eliminates both pieces from the game.
				this.board.getTile(this.srcTileId).removePiece();
				this.board.getTile(this.tgtTileId).removePiece();
				break;

			case 1: // NORMAL
				// Move Tile normally
				this.board.movePiece(this.srcTileId, this.tgtTileId);
				this.isExecuted = true;

				// Check if Flag has been maneuvered into the opposite end row of the board.
				if (hasFlagSucceeded())
					endGame(srcPieceOrigin.getPieceOwner());

				break;

			case 2: // AGGRESSIVE_WIN
				this.board.replacePiece(this.tgtTileId, this.srcPieceOrigin);
				this.board.getTile(this.srcTileId).removePiece();
				this.eliminatedPiece = this.tgtPieceOrigin;

				// Check if source or target piece is Flag rank, then conclude the game.
				if (isTgtPieceFlag())
					endGame(srcPieceOrigin.getPieceOwner());
				break;

			case 3: // AGGRESSIVE_LOSE
				this.board.getTile(this.srcTileId).removePiece();
				this.eliminatedPiece = this.srcPieceOrigin;

				if (isSrcPieceFlag() && !isTgtPieceFlag())
					endGame(tgtPieceOrigin.getPieceOwner());
				break;

			default:
				return false;
		}

		// If successful, change execution status and return true.
		isExecuted = true;
		return true;
	}

	/**
	 * Checks if source tile id will move a step horizontally or vertically.
	 *
	 * @return boolean true of this Move is a candidate move for the source
	 * piece.
	 */
	private boolean isMoveLegal() {
		int srcTgtDiff = this.tgtTileId - this.srcTileId;
		if (srcTgtDiff ==  1 ||
				srcTgtDiff == -1 ||
				srcTgtDiff ==  9 ||
				srcTgtDiff == -9)
			return true;

		return false;
	}

	/**
	 * Check if source and target tile id are out of bounds.
	 *
	 * @return boolean true if either or both the source or target tile id
	 * is/are out of bounds.
	 */
	private boolean isOutOfBounds() {
		// Check if within bounds
		if (this.srcTileId >= 0 || this.srcTileId <= 71 ||
				this.tgtTileId >= 0 || this.tgtTileId <= 71) {
			return false;
				}
		return true;
	}

	/**
	 * Check if source piece will wrap around the board if on the edges
	 *
	 * @return boolean true if source piece will wrap, else false
	 */
	private boolean isSrcEdgePieceWrapping() {
		// Check far left and right bounds jump to opposite side
		if ((srcTileId % 9 == 0 && (tgtTileId == srcTileId - 1 || tgtTileId == srcTileId + 17)) ||
				(srcTileId % 9 == 8 && (tgtTileId == srcTileId + 1 || tgtTileId == srcTileId - 17)))
			return true;

		return false;
	}

	/**
	 * Checks if targetTileId is occupied.
	 *
	 * @return boolean true if targetTileId is occupied, else false.
	 */
	private boolean isTgtTileOccupied() {
		return tgtPieceOrigin == null;
	}

	/**
	 * Checks if sourcePiece and targetPiece has the same {@link Alliance}.
	 *
	 * @return boolean true if sourcePiece and targetPiece has the same
	 * Alliance, else false.
	 */
	private boolean isFriendlyFire() {
		if (tgtPieceOrigin.getAlliance() == srcPieceOrigin.getAlliance())
			return true;

		return false;
	}

	/**
	 * Checks if sourcePiece and targetPiece has the same rank.
	 *
	 * @return boolean true if same rank, else false.
	 */
	private boolean isSameRank() {
		if (srcPieceOrigin.getRank() == tgtPieceOrigin.getRank())
			return true;

		return false;
	}

	/**
	 * Checks if target piece has been eliminated by the source piece. Higher
	 * ranking piece will eliminate lower ranking piece.
	 * Spy piece will eliminated all pieces regardless of rank except the Private
	 * piece.
	 *
	 * If both pieces were are of Flag rank, the aggressor piece will win
	 * the engagement.
	 *
	 * @return boolean true if target piece is subordinate to the source piece,
	 * else false.
	 */
	private boolean isTgtPieceEliminated() {
		if (isSrcPieceFlag() && isTgtPieceFlag())
			return true;
		else if (srcPieceOrigin.getRank().equals("Private") && tgtPieceOrigin.getRank().equals("Spy"))
			return true;
		else if (srcPieceOrigin.getRank().equals("Spy") && tgtPieceOrigin.getRank().equals("Private"))
			return false;
		else if (srcPieceOrigin.getPowLvl() > tgtPieceOrigin.getPowLvl())
			return true;
		else
			return false;
	}

	/**
	 * Checks if the target piece is ranked Flag.
	 *
	 * @return boolean true if target piece is Flag, else false.
	 */
	private boolean isTgtPieceFlag() {
		if (tgtPieceOrigin.getRank().equals("Flag"))
			return true;
		else
			return false;
	}

	/**
	 * Checks if the source piece is ranked Flag.
	 *
	 * @return boolean true if source piece is Flag, else false.
	 */
	private boolean isSrcPieceFlag() {
		if (srcPieceOrigin.getRank().equals("Flag"))
			return true;
		else
			return false;
	}

	/**
	 * Checks if the Flag piece has succeeded maneuvering into opposite end row
	 * of the Board without being eliminated.
	 *
	 * @return boolean true if Flag has succeeded, else false.
	 */
	private boolean hasFlagSucceeded() {
		if (srcPieceOrigin.getRank().equals("Flag") && isTgtTileOccupied()) {
			// Check if Flag piece is in the respective opposite end row of the board.
			if ((srcPieceOrigin.getAlliance() == Alliance.BLACK &&
						tgtTileId >= BoardUtils.LAST_ROW_INIT) ||
					(srcPieceOrigin.getAlliance() == Alliance.WHITE &&
					 tgtTileId < BoardUtils.SECOND_ROW_INIT))
				return true;
		}

		return false;
	}

	/**
	 * Gets the player executing this Move.
	 *
	 * @return Player player field.
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Gets the source Tile from Board.
	 *
	 * @return Tile from the Board.
	 */
	public Tile getSrcTile() {
		return board.getTile(srcTileId);
	}

	/**
	 * Gets the target Tile from Board.
	 *
	 * @return Tile from the Board.
	 */
	public Tile getTgtTile() {
		return board.getTile(tgtTileId);
	}

	/**
	 * Gets the source piece or Tile coordinates.
	 *
	 * @return int source piece or Tile index or ID.
	 */
	public int getSrcTileId() {
		return this.srcTileId;
	}

	/**
	 * Gets the target piece destination or target Tile coordinates.
	 *
	 * @return int target Tile index or ID.
	 */
	public int getTgtTileId() {
		return this.tgtTileId;
	}

	/**
	 * Gets the move type of this Move instance.
	 *
	 * @return String moveType field. Null if uninitialized.
	 */
	public MoveType getMoveType() {
		if (this.moveType != null)
			return this.moveType;

		return null;
	}

	/**
	 * Gets the turn ID of this Move instance.
	 *
	 * @return int turnId field.
	 */
	public int getTurnId() {
		return this.turnId;
	}

	/**
	 * Gets the eliminated piece of the aggressive execution.
	 *
	 * @return Piece the eliminated piece after aggressive engagement.
	 */
	public Piece getEliminatedPiece() {
		if (eliminatedPiece != null)
			return this.eliminatedPiece;

		return null;
	}

	/**
	 * Gets the source piece of this Move instance.
	 *
	 * @return Piece sourcePieceCopy field. Null if uninitialized.
	 */
	public Piece getSrcPieceOrigin() {
		if (this.srcPieceOrigin != null)
			return this.srcPieceOrigin;

		return null;
	}

	/**
	 * Gets the target piece of this Move instance.
	 *
	 * @return Piece targetPieceCopy field. Null if uninitialized.
	 */
	public Piece getTgtPieceOrigin() {
		if (this.tgtPieceOrigin != null)
			return this.tgtPieceOrigin;

		return null;
	}

	/**
	 * Undo or set Move execution to false.
	 *
	 * @return boolean true if successful, else false if already false.
	 */
	public boolean undoExecution() {
		if (!this.isExecuted)
			return false;

		switch (this.moveType.getValue()) {
			case -1: // INVALID
				System.out.println("undoExecution() E: Invalid move");
				return false;

			case 0: // DRAW
				this.board.getTile(this.srcTileId).insertPiece(srcPieceOrigin);
				this.board.getTile(this.tgtTileId).insertPiece(tgtPieceOrigin);
				break;

			case 1: // NORMAL
				// Check if Flag has been maneuvered into the opposite end row of the board.
				if (hasFlagSucceeded())
					restoreGame();

				this.board.movePiece(this.tgtTileId, this.srcTileId);
				this.isExecuted = false;


				break;

			case 2: // AGGRESSIVE_WIN
				if (isTgtPieceFlag())
					restoreGame();

				this.board.movePiece(this.tgtTileId, this.srcTileId);
				this.board.getTile(this.tgtTileId).insertPiece(this.eliminatedPiece);
				this.eliminatedPiece = null;

				this.isExecuted = false;
				break;

			case 3: // AGGRESSIVE_LOSE
				if (isSrcPieceFlag() && !isTgtPieceFlag())
					restoreGame();

				this.board.getTile(this.srcTileId).insertPiece(this.eliminatedPiece);
				this.eliminatedPiece = null;

				this.isExecuted = false;
				break;

			default:
				return false;
		}

		return true;
	}

	/**
	 * Redo or set Move execution to true
	 *
	 * @return boolean true if successful, else false if already true.
	 */
	public boolean redoExecution() {
		if (this.isExecuted)
			return false;

		return execute();
	}

	/**
	 * Checks if this Move instance has been executed already.
	 *
	 * @return boolean true if is executed, else false.
	 */
	public boolean isMoveExecuted() {
		return this.isExecuted;
	}

	/**
	 * Sets the execution state to true or false.
	 *
	 * @param isExecuted boolean
	 */
	public void setExecutionState(final boolean isExecuted) {
		this.isExecuted = isExecuted;
	}

	/**
	 * Sets the move type of this Move instance.
	 *
	 * @param moveType String move type to set.
	 */
	public void setMoveType(final MoveType moveType) {
		this.moveType = moveType;
	}

	/**
	 * Sets the turn ID of this move instance.
	 *
	 * @param turnId int turn id
	 */
	public void setTurnId(final int turnId) {
		this.turnId = turnId;
	}

	public void endGame(Player winner) {
		if (this.bias == null)
			this.gog.endGame(winner);
		// Gdx.app.log(this.getClas().getName(), "\n\n" + winner.getAlliance() + " player WON!\n");
	}

	public void restoreGame() {
		if (this.bias == null)
			this.board.getGog().restoreGame();
		// Gdx.app.log(this.getClas().getName(), "Game Restored!");
	}

	public void setBias(MoveType bias) {
		// Can only set loosing or winning bias
		if (bias == MoveType.NORMAL || bias == MoveType.INVALID)
			return;

		this.bias = bias;
	}

	public MoveType getBias() {
		if (this.bias != null)
			return this.bias;

		return null;
	}



	@Override
	public String toString() {
		final Alliance srcPieceAlliance = srcPieceOrigin == null ? null : srcPieceOrigin.getAlliance();
		final String srcPiece = srcPieceOrigin == null ? "blank" : srcPieceOrigin.getRank();
		final String tgtPiece = tgtPieceOrigin == null ? "blank" : tgtPieceOrigin.getRank();

		if (isExecuted) {
			String superiorPieceAlliance = "";
			if (this.moveType == MoveType.AGGRESSIVE_WIN) {
				superiorPieceAlliance = eliminatedPiece.getAlliance() == Alliance.BLACK ?
					" " + Alliance.WHITE: " " + Alliance.BLACK;
			}
			return "Turn " + this.turnId + ": " +
				srcPieceAlliance + " " +
				srcPiece + " " +
				srcTileId + " to " +
				tgtPiece + " " +
				tgtTileId + " " +
				this.moveType + superiorPieceAlliance +
				" EXECUTED";
		} else {
			return "Turn " + this.turnId + ": " +
				srcPieceAlliance + " " +
				srcPiece + " " +
				srcTileId + " to " +
				tgtPiece + " " +
				tgtTileId;
		}
	}
}
