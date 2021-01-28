package com.markl.game.ai.minimax;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.markl.game.engine.board.Move;
import com.markl.game.engine.board.Move.MoveType;
import com.markl.game.engine.board.Tile;
import com.markl.game.util.Utils;

/**
 * Dumb single player AI that make random moves
 *
 * @author Mark Lucernas
 * Created on 11/26/2020.
 */
public class AIDumb extends AI {

	public AIDumb() {}

	public Move createRandomMove() {
		LinkedList<Tile> tiles = gog.getBoard().getAllTiles();
		List<Integer> myPiecesCoords = new ArrayList<Integer>();

		// Get all pieces owned
		tiles.forEach((tile) -> {
			if (tile.isTileOccupied()) {
				if (tile.getPiece().getAlliance() == aiAlliance) {
					myPiecesCoords.add(tile.getTileId());
				}
			}
		});

		int randomMyPieceIdx;
		Map<Integer, Move> candidateMoves;
		List<Integer> legalMoves = new ArrayList<Integer>();
		int legalMovesSize = legalMoves.size();

		do {
			// Get random piece to move
			randomMyPieceIdx = Utils.getRandomInt(0, myPiecesCoords.size() - 1);
			candidateMoves = tiles.get(myPiecesCoords.get(randomMyPieceIdx)).getPiece().evaluateMoves();

			// Store all random legal moves
			candidateMoves.forEach((direction, move) -> {
				if (move.getMoveType() != MoveType.INVALID)
					legalMoves.add(direction);
			});
		} while (legalMovesSize == 0);

		// Pick random legal move and return
		int randomMoveIdx = Utils.getRandomInt(0, legalMoves.size() - 1);
		return candidateMoves.get(legalMoves.get(randomMoveIdx));
	}

	@Override
	public Move generateMove() {
		return createRandomMove();
	}
}
