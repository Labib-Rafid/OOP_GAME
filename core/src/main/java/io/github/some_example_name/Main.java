package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new HomeScreen(this)); // start with home page
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
