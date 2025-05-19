package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;


public class Main extends Game {
    public SpriteBatch batch;
    public OrthographicCamera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setScreen(new HomeScreen(this)); // start with home page
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
