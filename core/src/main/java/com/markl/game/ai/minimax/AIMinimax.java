package com.markl.game.ai.minimax;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.markl.game.Gog;
import com.markl.game.engine.board.BoardUtils;
import com.markl.game.engine.board.Move;
import com.markl.game.engine.board.Tile;
import com.markl.game.engine.board.Move.MoveType;
import com.markl.game.engine.board.pieces.Piece;
import com.markl.game.ui.screen.GameScreen;

/**
 * Single player AI using minimax algorithm
 *
 * @author Mark Lucernas
 * Created on 11/25/2020.
 */
public class AIMinimax extends AI {

	// AI strategic disposition for decision weights
	public enum Disposition {
		CONSERVATIVE(1), MODERATE(2), AGGRESSIVE(3);
		private final int value;

		Disposition(final int value) {
			this.value = value;
		}

		public int getValue() { return this.value; }
	}

	private int nodeCount = 0;
	private int depth;
	private GameScreen gameScreen;
	private Disposition disposition;

	private int rankPredictionWeight = 0;
	private int rankEvaluateBias = 0;

	private HashMap<Integer, Integer> bountyMap = new HashMap<Integer, Integer>();

	public AIMinimax(int depth, GameScreen gameScreen, Disposition disposition) {
		this.depth = depth;
		this.gameScreen = gameScreen;
		this.disposition = disposition;
		applyDisposition();
	}

	public void applyDisposition() {
		if (this.disposition != null) {
			switch(this.disposition.getValue()) {
				case 1:
					rankPredictionWeight = 4;
					rankEvaluateBias = 10;
					break;
				case 2:
					rankPredictionWeight = 2;
					rankEvaluateBias = 7;
					break;
				case 3:
					rankPredictionWeight = 1;
					rankEvaluateBias = 5;
					break;
			}
		}
	}

	public int evaluateBoard() {
		int score = 0;
		Iterator<Tile> iter = gog.getBoard().getAllTiles().iterator();

		while (iter.hasNext()) {
			Tile tile = iter.next();

			if (tile.isTileOccupied()) {
				Piece piece = tile.getPiece();

				// Evaluate pieces rank into scores
				if (piece.getAlliance() == gog.getCurrTurnMaker()) {
					score += piece.getPowLvl();
				} else {
					if (hasPieceBounty(piece))
						score -= bountyMap.get(piece.getPieceId());
					else
						score -= rankEvaluateBias;
				}

			}
		}

		return score;
	}

	public void placeBounty(Move move, boolean isAiTurn) {
		if (move != null && gog.hasUndo() && gog.isPlaying()) {
			// Place bounty on AGGRESSIVE_LOSE
			if (move.getMoveType().getValue() == 3 && isAiTurn) {

				int elimPiecePowLvl = move.getSrcPieceOrigin().getPowLvl();
				int winPieceId = move.getTgtPieceOrigin().getPieceId();
				int predictedRank;

				// Assume winning piece 2 ranks above
				if (elimPiecePowLvl == 999) predictedRank = 2;
				else if (elimPiecePowLvl + rankPredictionWeight > BoardUtils.GENERAL_FIVE_POW) predictedRank = 999;
				else predictedRank = elimPiecePowLvl + rankPredictionWeight;

				// Only put bounty if not already or predicted rank is higher than existing
				if (bountyMap.containsKey(winPieceId)) {
					if (bountyMap.get(winPieceId) < predictedRank)
						bountyMap.put(winPieceId, predictedRank);
				} else
					bountyMap.put(winPieceId, predictedRank);

				// Place bounty on AGGRESSIVE_WIN
			} else if (move.getMoveType().getValue() == 2 && !isAiTurn) {

				int elimPiecePowLvl = move.getTgtPieceOrigin().getPowLvl();
				int elimPieceId = move.getTgtPieceOrigin().getPieceId();
				int winPieceId = move.getSrcPieceOrigin().getPieceId();
				int predictedRank;

				// Assume winning piece has upper rank by rankPredictionWeight amount
				if (elimPiecePowLvl == 999) predictedRank = 2;
				else if (elimPiecePowLvl + rankPredictionWeight > BoardUtils.GENERAL_FIVE_POW) predictedRank = 999;
				else predictedRank = elimPiecePowLvl + rankPredictionWeight;

				// Only put bounty if not already or predicted rank is higher than existing
				if (bountyMap.containsKey(winPieceId)) {
					if (bountyMap.get(winPieceId) < predictedRank)
						bountyMap.put(winPieceId, predictedRank);
				} else
					bountyMap.put(winPieceId, predictedRank);

				// Remove from bounty map if piece(s) eliminated
				if (bountyMap.containsKey(elimPieceId))
					bountyMap.remove(elimPieceId);

				// Remove bounty on DRAW
			} else if (move.getMoveType().getValue() == 0) {
				int srcPieceId = move.getSrcPieceOrigin().getPieceId();
				int tgtPieceId = move.getTgtPieceOrigin().getPieceId();
				// Remove from bounty map if piece(s) eliminated
				if (bountyMap.containsKey(srcPieceId))
					bountyMap.remove(srcPieceId);
				if (bountyMap.containsKey(tgtPieceId))
					bountyMap.remove(tgtPieceId);
			}
			// Gdx.app.log(this.getClass().getName(), "AFTER: Evaluate Score " + gog.getCurrTurnMaker() + ": " + evaluateBoard());
			// System.out.println("");

			// TODO: Delete Me //
			// // Print pieces with bounty
			// Gdx.app.log(this.getClass().getName(), "Bounty Count: " + bountyMap.size());
			// Iterator<Tile> iter = gog.getBoard().getAllTiles().iterator();
			// while (iter.hasNext()) {
			//   Tile tile = iter.next();
			//   if (tile.isTileOccupied()) {
			//     Piece piece = tile.getPiece();
			//     if (bountyMap.containsKey(piece.getPieceId()))
			//       Gdx.app.log(this.getClass().getName(), piece.getPieceId() + " " + piece.getAlliance() + " " + piece.getRank() + " has a bounty of " + bountyMap.get(piece.getPieceId()));
			//   }
			// }
		}
	}

	public boolean hasPieceBounty(Piece piece) {
		if (bountyMap.containsKey(piece.getPieceId()))
			return true;

		return false;
	}

	public Move minimaxRoot(Gog gog, GameScreen gs, int depth, boolean isMaximizing) {
		this.nodeCount = 0;
		int bestScore = Integer.MIN_VALUE;
		Move bestMove = null;

		List<Move> legalMoves = gog.getBoard().getLegalMoves();

		for (int i = 0; i < legalMoves.size(); i++) {
			this.nodeCount++;
			Move nextMove = legalMoves.get(i);
			makeHypotheticalMove(nextMove, gs);

			System.out.println("");
			Gdx.app.log(this.getClass().getName(), "depth: " + depth);
			Gdx.app.log(this.getClass().getName(), "move: " + nextMove);
			Gdx.app.log(this.getClass().getName(), "score_before: " + bestScore);

			int value = minimax(gog, gs, depth - 1, isMaximizing);

			if (value > bestScore) {
				bestScore = value;
				bestMove = nextMove;
			}

			Gdx.app.log(this.getClass().getName(), "score_after: " + bestScore);
			gs.moveManager.undoLastMove(false);
		}

		Gdx.app.log(this.getClass().getName(), "bestScore: " + bestScore);
		return bestMove;
	}

	public int minimax(Gog gog, GameScreen gs, int depth, boolean isMaximizing) {
		float delay = 0.1f;
		Timer.schedule(new Task() {
			@Override
			public void run() {}
		}, delay);


		if (depth == 0)
			return evaluateBoard();

		List<Move> legalMoves = gog.getBoard().getLegalMoves();

		if (isMaximizing) {
			int max = Integer.MIN_VALUE;
			for (int i = 0; i < legalMoves.size(); i++) {
				this.nodeCount++;
				Move nextMove = legalMoves.get(i);
				makeHypotheticalMove(nextMove, gs);

				System.out.println("");
				Gdx.app.log(this.getClass().getName(), "depth: " + depth);
				Gdx.app.log(this.getClass().getName(), "move: " + nextMove);
				Gdx.app.log(this.getClass().getName(), "max-before: " + max);

				if (gog.isPlaying()) {
					max = Math.max(max, minimax(gog, gs, depth - 1, !isMaximizing));
				}

				Gdx.app.log(this.getClass().getName(), "max-after: " + max);

				gs.moveManager.undoLastMove(false);
			}
			return max;
		} else {
			int min = Integer.MAX_VALUE;
			for (int i = 0; i < legalMoves.size(); i++) {
				this.nodeCount++;
				Move nextMove = legalMoves.get(i);
				makeHypotheticalMove(nextMove, gs);

				System.out.println("");
				Gdx.app.log(this.getClass().getName(), "depth: " + depth);
				Gdx.app.log(this.getClass().getName(), "move: " + nextMove);
				Gdx.app.log(this.getClass().getName(), "min-before: " + min);

				if (gog.isPlaying()) {
					min = Math.min(min, minimax(gog, gs, depth - 1, isMaximizing));
				}

				Gdx.app.log(this.getClass().getName(), "min-after: " + min);

				gs.moveManager.undoLastMove(false);
			}
			return min;
		}
	}

	public void setDisposition(Disposition disposition) {
		if (disposition != null)
			this.disposition = disposition;
	}

	public Disposition getDisposition() {
		if (this.disposition != null)
			return this.disposition;

		return null;
	}

	public void makeHypotheticalMove(Move move, GameScreen gs) {
		// Gdx.app.log(this.getClass().getName(), move.toString());

		// Use AI disposition to force piece engagement result
		if (move.getMoveType().getValue() >= 2) {

			// Detect if has bounty to set bias accordingly
			if (!hasPieceBounty(move.getTgtPieceOrigin())) {
				if (this.disposition == Disposition.CONSERVATIVE)
					move.setBias(MoveType.AGGRESSIVE_LOSE);
				else if (this.disposition == Disposition.MODERATE)
					move.setBias(MoveType.DRAW);
				else if (this.disposition == Disposition.AGGRESSIVE)
					move.setBias(MoveType.AGGRESSIVE_WIN);
			} else {
				int tgtBountyPowLvl = bountyMap.get(move.getTgtPieceOrigin().getPieceId());
				if (tgtBountyPowLvl > move.getSrcPieceOrigin().getPowLvl())
					move.setBias(MoveType.AGGRESSIVE_WIN);
				else if (tgtBountyPowLvl == move.getSrcPieceOrigin().getPowLvl())
					move.setBias(MoveType.DRAW);
				else
					move.setBias(MoveType.AGGRESSIVE_LOSE);
			}

		}

		gs.moveManager.makeMove(move.getSrcTileId(), move.getTgtTileId(), false, false, false);
	}

	@Override
	public Move generateMove() {
		placeBounty(gog.getMoveHistory().get(gog.getCurrTurn() - 1), false);
		Move generatedMove = minimaxRoot(gog, gameScreen, this.depth, true);
		Gdx.app.log(this.getClass().getName(), generatedMove.toString());
		placeBounty(generatedMove, true);

		Gdx.app.log(this.getClass().getName(), "Node Count: " + this.nodeCount);
		return generatedMove;
	}
}
