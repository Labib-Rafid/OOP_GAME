package com.mygdx.templerun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Main extends ApplicationAdapter {
    private Texture runningManSheet, backgroundTexture, obstacleTexture;
    private Batch batch;
    private Rectangle player;
    private Array<Rectangle> obstacles;
    private float gravity = -800f, velocityY = 0;
    private long lastObstacleTime;
    private boolean isJumping = false;
    private boolean isCrouching = false;
    private float backgroundX = 0;

    private float normalHeight = 100;
    private float crouchHeight = 60;

    private Animation<TextureRegion> runAnimation;
    private float stateTime = 0f;

    private BitmapFont font;
    private int score = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();

        runningManSheet = new Texture("running_man.jpg");
        backgroundTexture = new Texture("white_back.png");
        obstacleTexture = new Texture("neon_rectangle.jpg");

        font = new BitmapFont(); // default font
        font.getData().setScale(2f); // increase size

        int frameCols = 3;
        int frameRows = 2;

        int frameWidth = runningManSheet.getWidth() / frameCols;
        int frameHeight = runningManSheet.getHeight() / frameRows;

        TextureRegion[][] tmp = TextureRegion.split(runningManSheet, frameWidth, frameHeight);
        TextureRegion[] runFrames = new TextureRegion[frameCols * frameRows];

        int index = 0;
        for (int row = 0; row < frameRows; row++) {
            for (int col = 0; col < frameCols; col++) {
                runFrames[index++] = tmp[row][col];
            }
        }

        runAnimation = new Animation<TextureRegion>(0.1f, runFrames);
        stateTime = 0f;

        player = new Rectangle(100, 100, 60, normalHeight);
        obstacles = new Array<>();
        spawnObstacle();
    }

    private void spawnObstacle() {
        Rectangle obstacle;
        if (Math.random() < 0.5) {
            obstacle = new Rectangle(Gdx.graphics.getWidth(), 100, 50, 50); // ground
        } else {
            obstacle = new Rectangle(Gdx.graphics.getWidth(), 180, 50, 50); // head-height
        }
        obstacles.add(obstacle);
        lastObstacleTime = TimeUtils.nanoTime();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !isJumping && !isCrouching) {
            velocityY = 400;
            isJumping = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !isJumping) {
            if (!isCrouching) {
                isCrouching = true;
                player.height = crouchHeight;
            }
        } else {
            if (isCrouching) {
                isCrouching = false;
                player.height = normalHeight;
            }
        }

        velocityY += gravity * Gdx.graphics.getDeltaTime();
        player.y += velocityY * Gdx.graphics.getDeltaTime();

        if (player.y < 100) {
            player.y = 100;
            isJumping = false;
        }

        backgroundX -= 100 * Gdx.graphics.getDeltaTime();
        if (backgroundX <= -Gdx.graphics.getWidth()) {
            backgroundX = 0;
        }

        if (TimeUtils.nanoTime() - lastObstacleTime > 1000000000) {
            spawnObstacle();
        }

        for (Iterator<Rectangle> iter = obstacles.iterator(); iter.hasNext(); ) {
            Rectangle obstacle = iter.next();
            obstacle.x -= 300 * Gdx.graphics.getDeltaTime();
            if (obstacle.x + obstacle.width < 0) {
                iter.remove();
                score++; // Score increases when player avoids an obstacle
            }

            if (obstacle.overlaps(player)) {
                System.out.println("Game Over! Final Score: " + score);
                dispose();
                return;
            }
        }

        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = runAnimation.getKeyFrame(stateTime, true);

        batch.begin();

        batch.draw(backgroundTexture, backgroundX, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(backgroundTexture, backgroundX + Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.draw(currentFrame, player.x, player.y, player.width, player.height);

        for (Rectangle obstacle : obstacles) {
            batch.draw(obstacleTexture, obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        font.draw(batch, "Score: " + score, 10, Gdx.graphics.getHeight() - 10); // Top-left corner

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        runningManSheet.dispose();
        backgroundTexture.dispose();
        obstacleTexture.dispose();
        font.dispose();
    }
}
