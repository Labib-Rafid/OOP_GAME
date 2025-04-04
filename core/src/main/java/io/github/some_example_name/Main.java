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
    private Texture playerTexture, groundTexture, obstacleTexture;
    private Batch batch;
    private Rectangle player;
    private Array<Rectangle> obstacles;
    private float gravity = -800f, velocityY = 0;
    private long lastObstacleTime;
    private boolean isJumping = false;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Load Textures
        playerTexture = new Texture("508.jpg");
        groundTexture = new Texture("508.jpg");
        obstacleTexture = new Texture("508.jpg");

        // Player properties
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

        // Handle Input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.x > 0) {
            player.x -= 200 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.x < Gdx.graphics.getWidth() - player.width) {
            player.x += 200 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !isJumping) {
            velocityY = 400;
            isJumping = true;
        }

        // Apply Gravity
        velocityY += gravity * Gdx.graphics.getDeltaTime();
        player.y += velocityY * Gdx.graphics.getDeltaTime();

        // Prevent falling below ground
        if (player.y < 100) {
            player.y = 100;
            isJumping = false;
        }

        // Spawn Obstacles
        if (TimeUtils.nanoTime() - lastObstacleTime > 1000000000) {
            spawnObstacle();
        }

        // Move Obstacles
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
        batch.draw(groundTexture, 0, 0, Gdx.graphics.getWidth(), 100);
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
        groundTexture.dispose();
        obstacleTexture.dispose();
    }
}
