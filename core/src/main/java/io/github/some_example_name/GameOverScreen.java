package io.github.some_example_name;

import io.github.some_example_name.Main;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class GameOverScreen implements Screen {
    final Main game;
    SpriteBatch batch;
    BitmapFont font;
    int score;

    public GameOverScreen(Main game, int score) {
        this.game = game;
        this.score = score;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font.draw(batch, "GAME OVER", 200, 400);
        font.draw(batch, "Score: " + score, 200, 300);
        font.draw(batch, "Press 1 to Return to Menu", 200, 250);
        font.draw(batch, "Press 2 to Exit", 200, 200);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            game.setScreen(new HomeScreen(game));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            Gdx.app.exit();
        }
    }

    @Override public void dispose() { batch.dispose(); font.dispose(); }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void show() {}
    @Override public void hide() {}
}
