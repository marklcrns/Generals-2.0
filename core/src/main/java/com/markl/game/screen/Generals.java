package com.markl.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Generals extends Game {

    protected SpriteBatch batch;
    protected BitmapFont font;

    @Override
    public void create() {

        batch = new SpriteBatch();
        // Use LibGDX's default Arial font.
        font = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));

    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        batch.dispose();
        font.dispose();
    }

    // public static void main(String[] args) {
    //     Game game = new Game();
    //     Board board = new Board(game);
    //     BoardBuilder builder = new BoardBuilder(board);
    //     builder.createDemoBoardBuild();
    //     builder.build();
    //     game.start();
    //     System.out.println(builder.toString());
    //     System.out.println(board.toString());
    // }
}
