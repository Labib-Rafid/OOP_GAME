package io.github.some_example_name;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class HomeScreen implements Screen {
    final Main game;
    SpriteBatch batch;
    BitmapFont font;

    public HomeScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font.draw(batch, "TEMPLE RUN", 200, 400);
        font.draw(batch, "Press 1 to Play", 200, 300);
        font.draw(batch, "Press 2 for Instructions", 200, 250);
        font.draw(batch, "Press 3 to Exit", 200, 200);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            game.setScreen(new GameScreen(game));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            System.out.println("Show instructions here!");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
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
