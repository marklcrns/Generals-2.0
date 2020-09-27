package com.markl.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.markl.game.engine.board.Board;
import com.markl.game.engine.board.BoardBuilder;

public class Generals extends ApplicationAdapter {

    public static void main(String[] args) {
        Board board = new Board();
        BoardBuilder builder = new BoardBuilder(board);
        builder.createDemoBoardBuild();
        System.out.println(builder.toString());
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
