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
        System.out.println(builder.toString());
        System.out.println(board.toString());

        System.out.println();

        Move move = new Move(null, board, 39, 30);
        move.evaluateMove();

        System.out.println(move.toString());
        System.out.println();

        move.execute();
        System.out.println(board.toString());
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
