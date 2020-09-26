package com.markl.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Board;

public class Generals extends ApplicationAdapter {

    public static void main(String[] args) {
        Board board = new Board();
        board.addTile(0, Alliance.BLACK);
        System.out.println(board.getTile(0).toString());
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
