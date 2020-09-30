package com.markl.game.engine.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.markl.game.engine.board.pieces.Flag;
import com.markl.game.engine.board.pieces.Piece;
import com.markl.game.engine.board.pieces.Spy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardTest {

    private Board board;
    private BoardBuilder builder;

    @BeforeEach
    void setUp() {
        this.board = new Board();
        this.builder = new BoardBuilder(board);
    }

    @Test
    @DisplayName("Test Board.getAllTiles()")
    void getAllTilesTest() {
        assertNotNull(this.board.getAllTiles());
    }

    @Test
    @DisplayName("Test Board.addTile()")
    void addTileTest() {
        assertEquals(this.board.getAllTiles().size(), 0);

        this.board.addTile(0, null);

        assertNotNull(this.board.getTile(0));
        assertEquals(this.board.getAllTiles().size(), 1);
    }

    @Test
    @DisplayName("Test Board.insertPiece()")
    void insertPieceTest() {
        this.board.addTile(0, null);
        Piece flag = new Flag(null, null);

        assertTrue(this.board.insertPiece(0, flag));
        assertFalse(this.board.insertPiece(0, flag));
        assertTrue(this.board.getTile(0).isTileOccupied());
    }

    @Test
    @DisplayName("Test Board.deletePiece()")
    void deletePieceTest() {
        this.board.addTile(0, null);
        Piece flag = new Flag(null, null);
        this.board.insertPiece(0, flag);

        assertTrue(this.board.deletePiece(0));
        assertFalse(this.board.deletePiece(0));
        assertTrue(this.board.getTile(0).isTileEmpty());
    }

    @Test
    @DisplayName("Test Board.replacePiece")
    void replacePieceTest() {
        this.board.addTile(0, null);
        this.board.addTile(1, null);
        Piece flag = new Flag(null, null);
        Piece spy = new Spy(null, null);
        this.board.insertPiece(0, flag);

        // Valid piece replacement
        assertTrue(this.board.getTile(0).getPiece().getRank().equals("Flag"));
        assertTrue(this.board.replacePiece(0, spy));
        assertTrue(this.board.getTile(0).getPiece().getRank().equals("Spy"));

        // Invalid piece replacement
        assertFalse(this.board.replacePiece(1, spy));
    }

    @Test
    @DisplayName("Test Board.movePiece()")
    void movePieceTest() {
        this.board.addTile(0, null);
        this.board.addTile(1, null);
        Piece flag = new Flag(null, null);
        this.board.insertPiece(0, flag);

        // Valid move piece
        assertTrue(this.board.getTile(0).getPiece().getRank().equals("Flag"));
        assertTrue(this.board.getTile(1).isTileEmpty());
        assertTrue(this.board.movePiece(0, 1));
        assertTrue(this.board.getTile(0).isTileEmpty());
        assertTrue(this.board.getTile(1).getPiece().getRank().equals("Flag"));

        // Invalid move piece
        assertFalse(this.board.movePiece(0, 1));
    }

    @Test
    @DisplayName("Test Board.swapPiece()")
    void swapPieceTest() {
        this.board.addTile(0, null);
        this.board.addTile(1, null);
        this.board.addTile(2, null);
        Piece flag = new Flag(null, null);
        Piece spy = new Spy(null, null);
        this.board.insertPiece(0, flag);
        this.board.insertPiece(1, spy);

        // Occupied Tile piece swap
        assertTrue(this.board.getTile(0).getPiece().getRank().equals("Flag"));
        assertTrue(this.board.getTile(1).getPiece().getRank().equals("Spy"));

        assertTrue(this.board.swapPiece(0, 1));

        assertTrue(this.board.getTile(0).getPiece().getRank().equals("Spy"));
        assertTrue(this.board.getTile(1).getPiece().getRank().equals("Flag"));

        // Empty Tile piece swap
        assertFalse(this.board.swapPiece(1, 2));
    }

    @Test
    @DisplayName("Test Board.discardPieces()")
    void discardPiecesTest() {
        this.builder.createDemoBoardBuild();
        this.builder.build();

        assertTrue(this.board.getTile(0).isTileOccupied());
        this.board.discardPieces();

        for (int i = 0; i < BoardUtils.TOTAL_BOARD_TILES; i++) {
            assertTrue(this.board.getTile(i).isTileEmpty());
        }
    }

}
