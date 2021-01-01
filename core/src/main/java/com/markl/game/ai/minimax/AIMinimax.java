package com.markl.game.ai.minimax;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.Move;
import com.markl.game.engine.board.Tile;

/**
 * Single player AI using minimax algorithm
 *
 * @author Mark Lucernas
 * Created on 11/25/2020.
 */
public class AIMinimax extends AI {

  private int depth;
  private Tree tree;

  public AIMinimax(int depth) {
    this.depth = depth;
  }

  public Map<Integer, Move> evaluateBoard(Board board) {
    Map<Integer, Move> boardValues = new HashMap<Integer, Move>();
    LinkedList<Tile> tiles         = board.getAllTiles();
    ListIterator<Tile> iterator    = tiles.listIterator();
    Tile currTile;

    while(iterator.hasNext()) {
      currTile = iterator.next();
    }

    return boardValues;
  }

  public Move minimax(LeafNode node, boolean isMax) {
    // TODO Implement
    return null;
  }

  @Override
  public Move generateMove() {
    // TODO Implement
    return null;
  }
}
