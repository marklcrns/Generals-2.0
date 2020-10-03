package com.markl.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * @author Mark Lucernas
 * Date: Oct 03, 2020
 */
public class MainMenuScreen implements Screen {

    final Generals game;

    private OrthographicCamera camera;

    public MainMenuScreen(final Generals game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 400);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen

        this.camera.update();
        this.game.batch.setProjectionMatrix(camera.combined);


        this.game.batch.begin();
        this.game.font.draw(game.batch, "Welcome to Game of Generals!!!", 100, 150);
        this.game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        this.game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }
}
