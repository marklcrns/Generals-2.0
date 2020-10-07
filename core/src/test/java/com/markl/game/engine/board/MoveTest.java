package com.markl.game.engine.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.markl.game.GameState;
import com.markl.game.engine.board.Move.MoveType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoveTest {

  private GameState game;
  private Board board;
  private Move invalidFriendlyFireMove;
  private Move invalidDiagonalMove;
  private Move invalidOutOfBoundsMove;
  private Move drawMove;
  private Move normalMove;
  private Move normalFlagWinMove;
  private Move aggressiveWinMove;
  private Move aggressiveDefeatMove;
  private Move aggressiveFlagDefeatMove;

  @BeforeEach
  void setUp() {
    this.game = new GameState();
    this.board = new Board(game);
    this.game.start();
    BoardBuilder builder = new BoardBuilder(board);
    builder.createDemoBoardBuild();
    builder.build(false);
  }

  @Test
  @DisplayName("Test invalid friendly fire move")
  void friendlyFireMoveTest() {
    // evaluate() test
    invalidFriendlyFireMove = new Move(null, this.board, 0, 9);
    this.invalidFriendlyFireMove.evaluate();
    assertEquals(invalidFriendlyFireMove.getMoveType(), MoveType.INVALID,
        "evaluate friendly fire to INVALID moveType test");

    // execute() test
    assertFalse(this.invalidFriendlyFireMove.execute(), "execute() test");
    assertFalse(this.invalidFriendlyFireMove.isMoveExecuted());
    assertEquals(this.game.getCurrTurn(), 1);
  }

  @Test
  @DisplayName("Test diagonal move")
  void diagonalMoveTest() {
    // evaluate() test
    invalidDiagonalMove = new Move(null, this.board, 9, 19);
    this.invalidDiagonalMove.evaluate();
    assertEquals(this.invalidDiagonalMove.getMoveType(), MoveType.INVALID,
        "evaluate diagonal movement to INVALID moveType test");

    // execute() test
    assertFalse(this.invalidDiagonalMove.execute(), "execute() test");
    assertFalse(this.invalidDiagonalMove.isMoveExecuted());
    assertEquals(this.game.getCurrTurn(), 1);
  }

  @Test
  @DisplayName("Test out of bounds move")
  void outOfBoundsMoveTest() {
    // evaluate() test
    invalidOutOfBoundsMove = new Move(null, this.board, 0, -1);
    this.invalidOutOfBoundsMove.evaluate();
    assertEquals(this.invalidOutOfBoundsMove.getMoveType(), MoveType.INVALID,
        "evaluate out of bounds to INVALID moveType test");

    // execute() test
    assertFalse(this.invalidOutOfBoundsMove.execute(), "execute() test");
    assertFalse(this.invalidOutOfBoundsMove.isMoveExecuted());
    assertEquals(this.game.getCurrTurn(), 1);
  }

  @Test
  @DisplayName("Test draw move")
  void drawMoveTest() {
    // evaluate() test
    drawMove = new Move(null, this.board, 37, 28);
    this.drawMove.evaluate();
    assertEquals(this.drawMove.getMoveType(), MoveType.DRAW,
        "evaluate move to DRAW moveType test");

    // execute() test
    assertTrue(this.drawMove.execute(), "execute() test");
    assertTrue(this.drawMove.isMoveExecuted());
    assertTrue(this.drawMove.getSrcTile().isTileEmpty(), "test source tile if empty");
    assertTrue(this.drawMove.getTgtTile().isTileEmpty(), "test target tile if empty");
    assertEquals(this.game.getCurrTurn(), 2);
  }

  @Test
  @DisplayName("Test normal move")
  void normalMoveTest() {
    // evaluate() test
    normalMove = new Move(null, this.board, 26, 35);
    this.normalMove.evaluate();
    assertEquals(this.normalMove.getMoveType(), MoveType.NORMAL,
        "evaluate move to NORMAL moveType test");

    // execute() test
    assertTrue(this.normalMove.execute(), "execute() test");
    assertTrue(this.normalMove.isMoveExecuted());
    assertTrue(this.normalMove.getSrcTile().isTileEmpty(), "test source tile if empty");
    assertTrue(BoardUtils.areTwoPiecesEqual(
          this.normalMove.getSrcPieceOrigin(),
          this.normalMove.getTgtTile().getPiece()),
        "test if source piece moved into target tile");
    assertEquals(this.game.getCurrTurn(), 2);
  }

  @Test
  @DisplayName("Test normal flag winning move")
  void normalFlagWinMoveTest() {
    // evaluate() test
    normalFlagWinMove = new Move(null, this.board, 62, 71);
    this.normalFlagWinMove.evaluate();
    assertEquals(this.normalFlagWinMove.getMoveType(), MoveType.NORMAL,
        "evaluate flag succeeded move to NORMAL moveType test");

    // execute() test
    assertTrue(this.normalFlagWinMove.execute(), "execute() test");
    assertTrue(this.normalFlagWinMove.isMoveExecuted());
    assertTrue(this.normalFlagWinMove.getSrcTile().isTileEmpty(), "test source tile if empty");
    assertTrue(BoardUtils.areTwoPiecesEqual(
          this.normalFlagWinMove.getSrcPieceOrigin(),
          this.normalFlagWinMove.getTgtTile().getPiece()),
        "test if source piece moved into target tile");
    assertEquals(this.game.getPlayerWinner(),
        this.normalFlagWinMove.getSrcPieceOrigin().getPieceOwner(),
        "test if source piece owner wins the game");
    assertFalse(this.game.isRunning(), "test if game no longer running");
    assertEquals(this.game.getCurrTurn(), 2);
  }

  @Test
  @DisplayName("Test aggressive dominate move")
  void aggressiveDominateMove() {
    // evaluate() test
    aggressiveWinMove = new Move(null, this.board, 29, 38);
    this.aggressiveWinMove.evaluate();
    assertEquals(this.aggressiveWinMove.getMoveType(), MoveType.AGGRESSIVE,
        "evaluate aggressive winner move to AGGRESSIVE moveType test");

    // execute() test
    assertTrue(this.aggressiveWinMove.execute(), "execute() test");
    assertTrue(this.aggressiveWinMove.getSrcTile().isTileEmpty(), "test source tile if empty");
    assertTrue(BoardUtils.areTwoPiecesEqual(
          this.aggressiveWinMove.getSrcPieceOrigin(),
          this.aggressiveWinMove.getTgtTile().getPiece()),
        "test if source piece moved into target tile");
    assertTrue(BoardUtils.areTwoPiecesEqual(
          this.aggressiveWinMove.getTgtPieceOrigin(),
          this.aggressiveWinMove.getEliminatedPiece()),
        "test if target piece eliminated");
    assertEquals(this.game.getCurrTurn(), 2);
  }

  @Test
  @DisplayName("Test aggressive defeated move")
  void aggressiveDefeatedMoveTest() {
    // evaluate() test
    aggressiveDefeatMove = new Move(null, this.board, 39, 30);
    this.aggressiveDefeatMove.evaluate();
    assertEquals(this.aggressiveDefeatMove.getMoveType(), MoveType.AGGRESSIVE,
        "evaluate aggressive defeated move to AGGRESSIVE moveType test");

    // execute() test
    // Aggressive defeat move execution test
    assertTrue(this.aggressiveDefeatMove.execute(), "execute() test");
    assertTrue(this.aggressiveDefeatMove.getSrcTile().isTileEmpty(),
        "test source tile if empty");
    assertTrue(BoardUtils.areTwoPiecesEqual(
          this.aggressiveDefeatMove.getTgtPieceOrigin(),
          this.aggressiveDefeatMove.getTgtTile().getPiece()),
        "test if target piece remains");
    assertTrue(BoardUtils.areTwoPiecesEqual(
          this.aggressiveDefeatMove.getSrcPieceOrigin(),
          this.aggressiveDefeatMove.getEliminatedPiece()),
        "test if source piece is eliminated");
    assertEquals(this.game.getCurrTurn(), 2);
  }

  @Test
  @DisplayName("Test aggressive flag defeated move")
  void aggressiveFlagDefeatMoveTest() {
    // Aggressive flag defeat move execution test
    aggressiveFlagDefeatMove = new Move(null, this.board, 40, 31);
    this.aggressiveFlagDefeatMove.evaluate();
    assertEquals(this.aggressiveFlagDefeatMove.getMoveType(), MoveType.AGGRESSIVE,
        "evaluate aggressive flag defeated move to AGGRESSIVE moveType test");

    assertTrue(this.aggressiveFlagDefeatMove.execute(), "execute() test");
    assertTrue(this.aggressiveFlagDefeatMove.getSrcTile().isTileEmpty(),
        "test if source tile empty");
    assertTrue(BoardUtils.areTwoPiecesEqual(
          this.aggressiveFlagDefeatMove.getTgtPieceOrigin(),
          this.aggressiveFlagDefeatMove.getTgtTile().getPiece()),
        "test if target piece remains");
    assertTrue(BoardUtils.areTwoPiecesEqual(
          this.aggressiveFlagDefeatMove.getSrcPieceOrigin(),
          this.aggressiveFlagDefeatMove.getEliminatedPiece()),
        "test if source piece is eliminated");
    assertEquals(this.game.getPlayerWinner(),
        this.aggressiveFlagDefeatMove.getTgtPieceOrigin().getPieceOwner(),
        "test if target piece owner wins the game");
    assertFalse(this.game.isRunning(), "test if game no longer running");
    assertEquals(this.game.getCurrTurn(), 2);
  }
}
