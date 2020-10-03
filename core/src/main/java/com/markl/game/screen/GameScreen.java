package com.markl.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Mark Lucernas
 * Date: Oct 03, 2020
 */
public class GameScreen implements Screen {

    final Generals game;

    private Texture pieceImg;
    private Rectangle piece;
    protected OrthographicCamera camera;

    public GameScreen(final Generals game) {
        this.game = game;

        // Load image as Texture
        pieceImg = new Texture(Gdx.files.internal("pieces/original/black/BLACK_Captain.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        piece = new Rectangle();
        piece.x = 800 / 2 - 64 / 2; // center the piece horizontally
        piece.y = 20; // bottom left corner
        piece.width = 64;
        piece.height = 64;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen

        // Tell camera to update its matrices
        this.camera.update();
        // Tell SpriteBatch to render in the coordinate system specified by the
        // camera
        this.game.batch.setProjectionMatrix(camera.combined);

        this.game.batch.begin();
        this.game.font.draw(this.game.batch, "Generals", 0, 480);
        this.game.batch.draw(pieceImg, piece.x, piece.y, piece.width, piece.height);
        this.game.batch.end();

        // process user input
        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            piece.x = touchPos.x - 64 / 2;
            piece.y = touchPos.y - 64 / 2;
        }
        if(Gdx.input.isKeyPressed(Keys.LEFT)) piece.x -= 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Keys.RIGHT)) piece.x += 200 * Gdx.graphics.getDeltaTime();

        // make sure the bucket stays within the screen bounds
        if(piece.x < 0) piece.x = 0;
        if(piece.x > 800 - 64) piece.x = 800 - 64;

        if(piece.y < 0) piece.y = 0;
        if(piece.y > 400 - 64) piece.y = 400 - 64;
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
        pieceImg.dispose();
    }
}
