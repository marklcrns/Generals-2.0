package com.markl.game.ai.minimax;

/**
 * Tree data structure for minimax algorithm. Depends on {@link LeafNode} class
 * for storing tree values.
 *
 * @author Mark Lucernas
 * Created on 11/25/2020.
 */
public class Tree {

  private LeafNode node;
  private int branchingFactor;
  private int depth;
  private int leafNodesCount;

  public Tree(int value) {
    node = new LeafNode(value);
  }

  public LeafNode insert(LeafNode node, int value){
    if(node == null){
      return new LeafNode(value);
    }
    // Move to the left if passed value is less than the current node
    if(value < node.value){
      node.left = insert(node.left, value);
    }
    // Move to the right if passed value is greater than the current node
    else if(value > node.value){
      node.right = insert(node.right, value);
    }
    return node;
  }

  // For traversing in order
  public void inOrder(LeafNode node){
    if(node != null){
      inOrder(node.left);
      System.out.print(node.value + " ");
      inOrder(node.right);
    }
  }

  public void inOrderDesc(LeafNode node){
    if(node != null){
      inOrderDesc(node.right);
      System.out.print(node.value + " ");
      inOrderDesc(node.left);
    }
  }
}
