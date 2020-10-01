package com.markl.game.engine.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.markl.game.Game;
import com.markl.game.engine.board.Move.MoveType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @Test
    @DisplayName("Test Move.evaluate()")
    void evaluateTest() {
        // INVALID friendly fire Move test
        Move invalidMove1 = new Move(null, this.board, 0, 9);
        invalidMove1.evaluate();
        assertEquals(invalidMove1.getMoveType(), MoveType.INVALID,
                "evaluate friendly fire to INVALID moveType test");

        // INVALID diagonal move test
        Move invalidMove2 = new Move(null, this.board, 9, 19);
        invalidMove2.evaluate();
        assertEquals(invalidMove2.getMoveType(), MoveType.INVALID,
                "evaluate diagonal movement to INVALID moveType test");

        // INVALID out of bounds move test
        Move invalidMove3 = new Move(null, this.board, 0, -1);
        invalidMove3.evaluate();
        assertEquals(invalidMove3.getMoveType(), MoveType.INVALID,
                "evaluate out of bounds to INVALID moveType test");

        // DRAW move test
        Move drawMove = new Move(null, this.board, 37, 28);
        drawMove.evaluate();
        assertEquals(drawMove.getMoveType(), MoveType.DRAW,
                "evaluate move to DRAW moveType test");

        // NORMAL move test
        Move normalMove = new Move(null, this.board, 22, 31);
        normalMove.evaluate();
        assertEquals(normalMove.getMoveType(), MoveType.NORMAL,
                "evaluate move to NORMAL moveType test");

        // AGGRESSIVE move test
        Move aggressiveMove = new Move(null, this.board, 38, 29);
        aggressiveMove.evaluate();
        assertEquals(aggressiveMove.getMoveType(), MoveType.AGGRESSIVE,
                "evaluate move to AGGRESSIVE moveType test");
    }
}
