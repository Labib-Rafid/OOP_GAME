package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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

abstract class Entity{
    private Rectangle rect;

    public Entity(float x, float y, float width, float height){
        rect = new Rectangle(x, y, width, height);
    }

    public abstract void update(float delta);
    public abstract void draw(Batch batch);

    public Rectangle getRectangle(){
        return rect;
    }
}

class Player extends Entity{
    private float gravity = -800f, velocityY = 0;
    private boolean isJumping = false, isCrouching = false;
    private float normalHeight = 100, crouchHeight = 60;
    private Animation<TextureRegion> runAnimation;
    private float stateTime = 0f;

    public Player(Animation<TextureRegion> animation){
        super(100, 200, 60, 100);
        this.runAnimation = animation;
    }

    public void jump(){
        if (!isJumping && !isCrouching){
            velocityY = 400;
            isJumping = true;
        }
    }

    public void crouch(boolean down){
        if (!isJumping) {
            if (down && !isCrouching){
                isCrouching = true;
                getRectangle().height = crouchHeight;
            } else if (!down && isCrouching){
                isCrouching = false;
                getRectangle().height = normalHeight;
            }
        }
    }

    @Override
    public void update(float delta){
        velocityY += gravity * delta;
        getRectangle().y += velocityY * delta;

        if (getRectangle().y <= 200){
            getRectangle().y = 200;
            velocityY = 0;
            isJumping = false;
        }

        stateTime += delta;
    }

    @Override
    public void draw(Batch batch){
        TextureRegion frame = runAnimation.getKeyFrame(stateTime, true);
        batch.draw(frame, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
    }
}

class Obstacle extends Entity{
    private Texture texture;

    public Obstacle(float x, float y, float width, float height, Texture texture){
        super(x, y, width, height);
        this.texture = texture;
    }

    @Override
    public void update(float delta){
        getRectangle().x -= 300 * delta;
    }

    @Override
    public void draw(Batch batch){
        batch.draw(texture, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
    }

    public boolean isOffScreen(){
        return getRectangle().x + getRectangle().width < 0;
    }
}

public class GameScreen implements Screen{
    private SpriteBatch batch;
    private Player player;
    private Array<Obstacle> obstacles;
    private Texture obstacleTexture;
    private Texture backgroundTexture;
    private float backgroundX = 0;
    private TextureRegion[] runFrames;
    private Animation<TextureRegion> runAnimation;
    private BitmapFont font;
    private long lastObstacleTime;
    private static int score;
    private float scoreTimer = 0f;

    final Main game;

    public GameScreen(Main game){
        this.game = game;
    }

    @Override
    public void show(){
        batch = new SpriteBatch();

        Texture runSheet = new Texture("running_boy_green.png");
        TextureRegion[][] tmp = TextureRegion.split(runSheet, runSheet.getWidth() / 3, runSheet.getHeight() / 2);
        runFrames = new TextureRegion[6];
        int index = 0;
        for (TextureRegion[] row : tmp)
            for (TextureRegion frame : row)
                runFrames[index++] = frame;

        runAnimation = new Animation<>(0.1f, runFrames);

        player = new Player(runAnimation);
        obstacles = new Array<>();

        backgroundTexture = new Texture("Greenery_Back.png");
        obstacleTexture = new Texture("neon_rectangle.jpg");

        spawnObstacle();

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(3);
        score = 0;
    }

    private void spawnObstacle(){
        float y = Math.random() < 0.5 ? 200 : 280;
        Obstacle obstacle = new Obstacle(Gdx.graphics.getWidth(), y, 50, 50, obstacleTexture);
        obstacles.add(obstacle);
        lastObstacleTime = TimeUtils.nanoTime();
    }

    private void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.jump();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player.crouch(true);
        } else{
            player.crouch(false);
        }
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput();
        backgroundX -= 100 * delta;
        if (backgroundX <= -Gdx.graphics.getWidth()){
            backgroundX = 0;
        }

        player.update(delta);

        if (TimeUtils.nanoTime() - lastObstacleTime > 1000000000){
            spawnObstacle();
        }

        Iterator<Obstacle> iter = obstacles.iterator();
        while (iter.hasNext()){
            Obstacle obs = iter.next();
            obs.update(delta);
            if (obs.isOffScreen()){
                iter.remove();
                score++;
            }
            if (obs.getRectangle().overlaps(player.getRectangle())){
                game.setScreen(new GameOverScreen(game, score));
                dispose();
                return;
            }
        }

        scoreTimer += delta;
        while (scoreTimer >= 1.0f){
            //this.score += 10;
            scoreTimer -= 1.0f;
        }

        batch.begin();
        batch.draw(backgroundTexture, backgroundX, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(backgroundTexture, backgroundX + Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        player.draw(batch);
        for (Obstacle obs : obstacles){
            obs.draw(batch);
        }

        font.draw(batch, "Score: " + this.score, 20, Gdx.graphics.getHeight() - 20);
        batch.end();
    }

    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose(){
        batch.dispose();
        obstacleTexture.dispose();
        backgroundTexture.dispose();
        font.dispose();
    }
}
