package com.markl.game.engine.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.markl.game.Gog;
import com.markl.game.engine.board.pieces.Piece;

/**
 * @author Mark Lucernas
 * Date: Sep 19, 2020
 */
public class Board {

	private Gog gog;								// Game instance reference
	private LinkedList<Tile> tiles; // List of all Tiles containing data of each piece

	// TODO: MOVE into AiMinimax //
	private HashMap<Integer, Integer> bountyMap = new HashMap<Integer, Integer>();

	/**
	 * No argument constructor
	 */
	public Board() {
		this.initBoard();
	}

	/**
	 * Constructor function that takes in Game as a parameter
	 * @param game {@link Gog} instance.
	 */
	public Board(Gog gog) {
		this.gog = gog;
		this.initBoard();
	}

	/**
	 * Initializes Board instance
	 */
	private void initBoard() {
		this.tiles = new LinkedList<Tile>();
		clearBoard();
	}

	public void initGame() {
		this.gog.setBoard(this);
	}

	public int move(Move newMove, boolean isRedo) {
		Player currTurnMaker = gog.getCurrTurnMakerPlayer();
		int srcTileId = newMove.getSrcTileId();

		// TODO: DELETE ME //
		// Gdx.app.log("Board", ascii());

		// Check if piece owned by current turn maker
		if ((!isRedo && currTurnMaker.isMyPiece(getTile(srcTileId).getPiece())) ||
				(isRedo )) {
			if (!isRedo) {
				// TODO: DELETE after //
				// Gdx.app.log("Board", "BEFORE: Evaluate Score " + gog.getCurrTurnMaker() + ": " + evaluateBoard());
				newMove.evaluate();
			}

			newMove.execute();

			// Record if valid move
			if (newMove.getMoveType().getValue() != -1) {
				gog.getMoveHistory().put(newMove.getTurnId(), newMove);
				gog.nextTurn();
			}

			return newMove.getMoveType().getValue();
				}
		// Return invalid if piece does not owned by current turn maker player
		return -1;
	}

	/**
	 * Method that empties board Tiles pieces.
	 */
	public void clearBoard() {
		this.tiles = new LinkedList<Tile>();
		// Add new empty Tiles in board
		for (int i = 0; i < BoardUtils.TOTAL_BOARD_TILES; i++) {
			// Set Tile territory
			if (i < BoardUtils.TOTAL_BOARD_TILES / 2)
				this.addTile(i, Alliance.BLACK);
			else
				this.addTile(i, Alliance.WHITE);
		}
	}

	/**
	 * Inserts piece into an empty tile.
	 * @param srcPieceTileId source piece coordinates.
	 * @param piece Piece instance to insert.
	 * @return boolean true if successful, else false.
	 */
	public boolean insertPiece(final int srcPieceTileId, final Piece piece) {
		if (this.getTile(srcPieceTileId).isTileEmpty()) {
			piece.setPieceTileId(srcPieceTileId);
			this.getAllTiles().get(srcPieceTileId).insertPiece(piece);
			this.getTile(srcPieceTileId).insertPiece(piece);
			return true;
		}
		return false;
	}

	/**
	 * Replaces Tile piece.
	 * @param tgtTileId target occupied tile to replace.
	 * @param srcTileId new Piece instance to replace with.
	 * @return boolean true if successful, else false.
	 */
	public boolean replacePiece(final int tgtTileId, final Piece srcTileId) {
		if (this.getTile(tgtTileId).isTileOccupied()) {
			// TODO: improve piece manipulation efficiency
			srcTileId.setPieceTileId(tgtTileId);
			this.getAllTiles().get(tgtTileId).replacePiece(srcTileId);
			this.getTile(tgtTileId).replacePiece(srcTileId);

			return true;
		}
		return false;
	}

	/**
	 * Moves piece from one Tile to another.
	 * @param srcPieceTileId source piece Tile id.
	 * @param tgtPieceTileId targetPiece Tile id.
	 * @return boolean true if successful, else false.
	 */
	public boolean movePiece(final int srcPieceTileId, final int tgtPieceTileId) {
		// insert copy of source piece into target tile
		if (this.getTile(srcPieceTileId).isTileOccupied() &&
				this.getTile(tgtPieceTileId).isTileEmpty())
		{
			final Piece srcPieceCopy = this.getTile(srcPieceTileId).getPiece().clone();
			srcPieceCopy.setPieceTileId(tgtPieceTileId);
			this.getTile(tgtPieceTileId).insertPiece(srcPieceCopy);
			// delete source piece
			this.getTile(srcPieceTileId).removePiece();

			return true;
		}
		return false;
	}

	/**
	 * Deletes occupied tile.
	 * @param pieceTileId piece coordinates.
	 * @return boolean true if successful, else false.
	 */
	public boolean deletePiece(final int pieceTileId) {
		if (this.getTile(pieceTileId).isTileOccupied()) {
			this.getTile(pieceTileId).removePiece();

			return true;
		}
		return false;
	}

	/**
	* Swaps two pieces and update piece coordinates.
	 * @param srcPieceTileId source piece coordinates.
	 * @param tgtPieceTileId target piece coordinates.
	 * @return boolean true if successful, else false.
	 */
	public boolean swapPiece(final int srcPieceTileId, final int tgtPieceTileId) {
		if (this.getTile(srcPieceTileId).isTileOccupied() &&
				this.getTile(tgtPieceTileId).isTileOccupied())
		{
			final Piece tmpSrcPiece = this.getTile(srcPieceTileId).getPiece().clone();
			final Piece tmpTgtPiece = this.getTile(tgtPieceTileId).getPiece().clone();

			tmpSrcPiece.setPieceTileId(tgtPieceTileId);
			tmpTgtPiece.setPieceTileId(srcPieceTileId);

			this.getAllTiles().get(srcPieceTileId).replacePiece(tmpTgtPiece);
			this.getAllTiles().get(tgtPieceTileId).replacePiece(tmpSrcPiece);

			return true;
		}
		return false;
	}

	/**
	 * Method that adds Tile into gameBoard field.
	 * @param tileId		tile id.
	 * @param territory tile territory Alliance.
	 * @param occupied	is tile occupied by a piece.
	 */
	public final void addTile(final int tileId, final Alliance territory) {
		this.tiles.add(new Tile(tileId, territory));
	}

	/**
	 * Gets specific tile from gameBoard field.
	 * @param id tile number.
	 * @return Tile from gameBoard field List.
	 */
	public Tile getTile(int id) {
		return this.tiles.get(id);
	}

	/**
	 * Gets the occupying piece in specified Tile
	 * @param tileId	tile id
	 * @return				Piece if Tile occupied, else null
	 */
	public Piece getPiece(int tileId) {
		if (this.tiles.get(tileId).isTileOccupied())
			return this.tiles.get(tileId).getPiece();

		return null;
	}

	/**
	 * Gets current board state.
	 * @return List<Tile> gameBoard field.
	 */
	public LinkedList<Tile> getAllTiles() {
		return this.tiles;
	}

	public List<Move> getLegalMoves() {
		List<Move> legalMoves = new ArrayList<Move>();
		Iterator<Tile> iter = tiles.iterator();
		Map<Integer, Move> candidateMoves;

		// Loop over tiles
		while (iter.hasNext()) {
			Tile tile = iter.next();
			if (tile.isTileOccupied()) {
				Piece piece = tile.getPiece();
				if (gog.getCurrTurnMaker() == piece.getAlliance()) {
					// Loop over candidate moves
					candidateMoves = piece.evaluateMoves();
					for (Map.Entry<Integer, Move> entry : candidateMoves.entrySet()) {
						Move move = entry.getValue();
						if (move != null && move.getMoveType().getValue() != -1)
							legalMoves.add(move);
					}
				}
			}
		}

		Gdx.app.log(this.getClass().getName(), "legalMoves: " + legalMoves.size());
		return legalMoves;
	}

	public Gog getGog() { return this.gog; }
	public void setGog(Gog gog) { this.gog = gog; }

	public String ascii() {
		String debugBoard = "\nBoard Debug Board\n";
		debugBoard += "		 0 1 2 3 4 5 6 7 8\n";
		debugBoard += "		+-----------------\n";
		for (int i = 0; i < BoardUtils.TOTAL_BOARD_TILES / 2; i += 9) {
			if (i < 10)
				debugBoard += " " + i + " |";
			else
				debugBoard += i + " |";
			for (int j = i; j < i + 9; j++) {
				if (this.getTile(j).isTileEmpty()) {
					debugBoard += ".";
				} else {
					final String rank = this.getTile(j).getPiece().getRank();
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

		debugBoard += "		|-----------------\n";

		for (int i = BoardUtils.TOTAL_BOARD_TILES / 2; i < BoardUtils.TOTAL_BOARD_TILES; i += 9) {
			if (i < 10)
				debugBoard += " " + i + " |";
			else
				debugBoard += i + " |";
			for (int j = i; j < i + 9; j++) {
				if (this.getTile(j).isTileEmpty()) {
					debugBoard += ".";
				} else {
					final String rank = this.getTile(j).getPiece().getRank();
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

	// @Override
	// protected Board clone() throws CloneNotSupportedException {
	//	 LinkedList<Tile> newTiles = new LinkedList<Tile>();
	//
	//	 Iterator<Tile> iterator = this.tiles.iterator();
	//
	//	 while (iterator.hasNext()) {
	//		 newTiles.add(iterator.next().clone());
	//	 }
	//
	// }

	/**
	 * @return String representation of all current Tiles for debugging
	 */
	@Override
	public String toString() {
		return "Impelement board toString()";
	}
}
