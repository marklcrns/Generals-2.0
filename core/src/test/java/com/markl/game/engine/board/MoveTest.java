package com.markl.game.engine.board;

import com.markl.game.Game;

import org.junit.jupiter.api.BeforeEach;

class MoveTest {

    private Game game;
    private Board board;

    @BeforeEach
    void setUp() {
        this.game = new Game();
        this.board = new Board(game);
        BoardBuilder builder = new BoardBuilder(board);
        builder.createDemoBoardBuild();
        builder.build();
    }

}
