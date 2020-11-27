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

  private Tree tree;

  public AIMinimax() {}

  public Map<Integer, Move> evalueateBoard(Board board) {
    Map<Integer, Move> boardValues = new HashMap<Integer, Move>();
    LinkedList<Tile> tiles         = board.getAllTiles();
    ListIterator<Tile> iterator    = tiles.listIterator();
    Tile currTile;

    while(iterator.hasNext()) {
      currTile = iterator.next();
    }


    return boardValues;
  }

  @Override
  public Move generateMove() {
    // TODO Auto-generated method stub
    return null;
  }
}
