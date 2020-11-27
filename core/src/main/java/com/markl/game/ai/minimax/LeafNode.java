package com.markl.game.ai.minimax;

/**
 * Leaf node for minimax algorithm branching.
 *
 * @author Mark Lucernas
 * Created on 11/25/2020.
 */
public class LeafNode {
  protected int value;
  protected LeafNode left;
  protected LeafNode right;

  public LeafNode(int value){
    this.value = value;
    this.left = null;
    this.right = null;
  }
}
