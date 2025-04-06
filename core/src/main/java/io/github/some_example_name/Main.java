package com.mygdx.templerun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Main extends ApplicationAdapter {
    private Texture playerTexture, backgroundTexture, obstacleTexture;
    private Batch batch;
    private Rectangle player;
    private Array<Rectangle> obstacles;
    private float gravity = -800f, velocityY = 0;
    private long lastObstacleTime;
    private boolean isJumping = false;
    private float backgroundX = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Load Textures
        playerTexture = new Texture("player1.png");
        backgroundTexture = new Texture("41524.jpg");
        obstacleTexture = new Texture("neon_rectangle.jpg");

        // Player stays at fixed horizontal position
        player = new Rectangle(100, 100, 50, 50);

        // Obstacles
        obstacles = new Array<>();
        spawnObstacle();
    }

    private void spawnObstacle() {
        Rectangle obstacle = new Rectangle(Gdx.graphics.getWidth(), 100, 50, 50);
        obstacles.add(obstacle);
        lastObstacleTime = TimeUtils.nanoTime();
    }

    @Override
    public void render() {
        // Clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle jump input
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !isJumping) {
            velocityY = 400;
            isJumping = true;
        }

        // Apply gravity
        velocityY += gravity * Gdx.graphics.getDeltaTime();
        player.y += velocityY * Gdx.graphics.getDeltaTime();

        // Prevent falling below ground
        if (player.y < 100) {
            player.y = 100;
            isJumping = false;
        }

        // Scroll background
        backgroundX -= 100 * Gdx.graphics.getDeltaTime();
        if (backgroundX <= -Gdx.graphics.getWidth()) {
            backgroundX = 0;
        }

        // Spawn obstacles periodically
        if (TimeUtils.nanoTime() - lastObstacleTime > 1000000000) {
            spawnObstacle();
        }

        // Move obstacles left
        for (Iterator<Rectangle> iter = obstacles.iterator(); iter.hasNext(); ) {
            Rectangle obstacle = iter.next();
            obstacle.x -= 300 * Gdx.graphics.getDeltaTime();
            if (obstacle.x + obstacle.width < 0) iter.remove();

            // Collision detection
            if (obstacle.overlaps(player)) {
                System.out.println("Game Over!");
                dispose();
                return;
            }
        }

        // Draw everything
        batch.begin();

        // Draw scrolling background
        batch.draw(backgroundTexture, backgroundX, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(backgroundTexture, backgroundX + Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw player and obstacles
        batch.draw(playerTexture, player.x, player.y, player.width, player.height);
        for (Rectangle obstacle : obstacles) {
            batch.draw(obstacleTexture, obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
        backgroundTexture.dispose();
        obstacleTexture.dispose();
    }
}
