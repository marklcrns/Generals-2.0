package com.markl.game.engine.board;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark Lucernas
 * Date: Sep 19, 2020
 */
public class Board {

    private List<Tile> tiles;       // List of all Tiles containing data of each piece
    private Player playerBlack;     // Player instance that all contains all infos on black pieces
    private Player playerWhite;     // Player instance that all contains all infos on white pieces
    private String playerBlackName; // Black player's name assigned when game initialized
    private String playerWhiteName; // White player's name assigned when game initialized

    public Board() {
        init();
    }

    private void init() {
        this.tiles = new ArrayList<Tile>();
    }

    /**
     * Method that adds Tile into gameBoard field.
     * @param tileId    tile id.
     * @param territory tile territory Alliance.
     * @param occupied  is tile occupied by a piece.
     */
    public final void addTile(final int tileId, final Alliance territory) {
        this.tiles.add(new Tile(tileId, territory));
    }

    /**
     * Gets specific tile from gameBoard field.
     * @param tileId tile number.
     * @return Tile from gameBoard field List.
     */
    public Tile getTile(int tileNum) {
        return tiles.get(tileNum);
    }

    /**
     * Method that empties board Tiles pieces.
     */
    private void emptyBoard() {
        tiles = new ArrayList<Tile>();
        // Add new empty Tiles in board
        for (int i = 0; i < BoardUtils.TOTAL_BOARD_TILES; i++) {
            // Set Tile territory
            if (i < BoardUtils.TOTAL_BOARD_TILES / 2)
                this.addTile(i, Alliance.BLACK);
            else
                this.addTile(i, Alliance.WHITE);
        }
    }

    /**
     * Gets specific Player currently registered in this Board based on the
     * alliance.
     * @param alliance Alliance of the Player.
     * @return Player based on the alliance param.
     */
    public Player getPlayer(final Alliance alliance) {
        if (alliance == Alliance.BLACK)
            return playerBlack;
        else
            return playerWhite;
    }

}
