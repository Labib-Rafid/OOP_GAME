package io.github.some_example_name;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;

public class Instructions implements Screen {
    final Main game;
    SpriteBatch batch;
    BitmapFont fontTitle, fontText;
    Texture back, buttonTexture;
    Button backButton;

    public Instructions(Main game) {
        this.game = game;
        batch = new SpriteBatch();

        fontTitle = new BitmapFont();
        fontText = new BitmapFont();
        fontTitle.setColor(Color.BLACK);
        fontText.setColor(Color.BLACK);
        fontTitle.getData().setScale(3);
        fontText.getData().setScale(2);

        back = new Texture(Gdx.files.internal("HomeBack.png"));
        buttonTexture = new Texture(Gdx.files.internal("white_back.png"));

        float bw = 200, bh = 60;
        float bx = 50, by = 50;
        backButton = new Button("BACK", bx, by, bw, bh, buttonTexture, fontText);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(back, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Title
        fontTitle.draw(batch, "INSTRUCTIONS", 100, 650);

        // Point-by-point instructions
        fontText.draw(batch, "1. Press PLAY to start the game.", 100, 550);
        fontText.draw(batch, "2. Use arrow keys or tap to control the character.", 100, 500);
        fontText.draw(batch, "3. Avoid obstacles and collect coins.", 100, 450);
        fontText.draw(batch, "4. The longer you run, the higher your score.", 100, 400);
        fontText.draw(batch, "5. If you hit an obstacle, the game ends.", 100, 350);
        fontText.draw(batch, "6. Press EXIT to quit anytime.", 100, 300);

        backButton.render(batch);
        batch.end();

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(touchPos);

            if (backButton.isClicked(touchPos)) {
                game.setScreen(new HomeScreen(game));
            }
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        batch.dispose();
        fontTitle.dispose();
        fontText.dispose();
        back.dispose();
        buttonTexture.dispose();
    }
}
