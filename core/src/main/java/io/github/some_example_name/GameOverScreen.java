package io.github.some_example_name;

import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.Main;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class GameOverScreen implements Screen {
    final Main game;
    SpriteBatch batch;
    BitmapFont font, fontButton;
    int score;

    Button Exit, Return;
    Texture buttonTexture;

    public GameOverScreen(Main game, int score) {
        this.game = game;
        this.score = score;

        batch = new SpriteBatch();

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(3);

        fontButton = new BitmapFont();
        fontButton.setColor(Color.BLACK);
        fontButton.getData().setScale(2);


        float bw = 300, bh = 60;
        float bx = (Gdx.graphics.getWidth() - bw) / 2 - 300;
        float by = 250;

        buttonTexture = new Texture(Gdx.files.internal("white_back.png"));
        Return = new Button("Return To Home", bx + 100, by + 100, bw, bh, buttonTexture, fontButton);
        Exit = new Button("Exit Game", bx + 100, by + 20, bw, bh, buttonTexture, fontButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font.draw(batch, "GAME OVER", 200, 600);
        font.draw(batch, "Score: " + score, 200, 520);
        Return.render(batch);
        Exit.render(batch);
//        font.draw(batch, "Press 1 to Return to Menu", 200, 250);
//        font.draw(batch, "Press 2 to Exit", 200, 200);
        batch.end();

//        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
//            game.setScreen(new HomeScreen(game));
//        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
//            Gdx.app.exit();
//        }

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(touchPos);

            if (Return.isClicked(touchPos)) {
                game.setScreen(new HomeScreen(game));
            } else if (Exit.isClicked(touchPos)) {
                Gdx.app.exit();
            }
        }

    }

    @Override public void dispose() { batch.dispose(); font.dispose(); }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void show() {}
    @Override public void hide() {}
}
