package com.markl.game.engine.board;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        BoardBuilder builder = new BoardBuilder(board);
    }

    @Test
    @DisplayName("Test Board.getAllTiles() not null")
    void getAllTilesTest() {
        assertNotNull(this.board.getAllTiles());
    }

    @Test
    @DisplayName("Test Board.swapPiece()")
    void swapPieceTest() {

    }

}
