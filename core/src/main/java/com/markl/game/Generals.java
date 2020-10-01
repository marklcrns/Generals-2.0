package com.markl.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.BoardBuilder;
import com.markl.game.engine.board.Move;

public class Generals extends ApplicationAdapter {

    public static void main(String[] args) {
        Game game = new Game();
        Board board = new Board(game);
        BoardBuilder builder = new BoardBuilder(board);
        builder.createDemoBoardBuild();
        builder.build();
        game.start();
        System.out.println(builder.toString());
        System.out.println(board.toString());

        // First move
        Move moveOne = new Move(null, board, 39, 30);
        moveOne.evaluate();

        System.out.println();

        moveOne.execute();
        System.out.println(board.toString());
        System.out.println(moveOne.toString());

        // Second move
        Move moveTwo = new Move(null, board, 28, 37);
        moveTwo.evaluate();

        System.out.println();

        moveTwo.execute();
        System.out.println(board.toString());
        System.out.println(moveTwo.toString());

        // Third move
        Move moveThree = new Move(null, board, 38, 29);
        moveThree.evaluate();

        System.out.println();

        moveThree.execute();
        System.out.println(board.toString());
        System.out.println(moveThree.toString());
    }

    @Override
    public void create () {
    }

    @Override
    public void render () {
    }

    @Override
    public void dispose () {
    }
}
