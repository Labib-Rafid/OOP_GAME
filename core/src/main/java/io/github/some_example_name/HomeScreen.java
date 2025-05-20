package io.github.some_example_name;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;

public class HomeScreen implements Screen {
    final Main game;
    SpriteBatch batch;
    BitmapFont font, fontTitle;
    Texture back;
    Texture buttonTexture;

    Button playButton, instructionsButton, exitButton;

    public HomeScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);

        fontTitle = new BitmapFont();
        fontTitle.setColor(Color.BLACK);
        fontTitle.getData().setScale(4);


        back = new Texture(Gdx.files.internal("HomeBack.png"));
        buttonTexture = new Texture(Gdx.files.internal("white_back.png"));

        float bw = 300, bh = 60;
        float bx = (Gdx.graphics.getWidth() - bw) / 2 - 300;
        float by = 250;

        playButton = new Button("PLAY", bx, by, bw, bh, buttonTexture, font);
        instructionsButton = new Button("INSTRUCTIONS", bx, by - 80, bw, bh, buttonTexture, font);
        exitButton = new Button("EXIT", bx, by - 160, bw, bh, buttonTexture, font);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(back, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fontTitle.draw(batch, "ENDLESS RUNNER", 100, 400);

        playButton.render(batch);
        instructionsButton.render(batch);
        exitButton.render(batch);
        batch.end();

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(touchPos);

            if (playButton.isClicked(touchPos)) {
                game.setScreen(new GameScreen(game));
            } else if (instructionsButton.isClicked(touchPos)) {
                //System.out.println("Show instructions here!");
                game.setScreen(new Instructions(game));
            } else if (exitButton.isClicked(touchPos)) {
                Gdx.app.exit();
            }
        }
    }

        @Override public void resize(int width, int height) {}
        @Override public void pause() {}
        @Override public void resume() {}
        @Override public void show() {}
        @Override public void hide() {}

        @Override public void dispose() {
            batch.dispose();
            font.dispose();
            fontTitle.dispose();
            back.dispose();
            buttonTexture.dispose();
        }
}
