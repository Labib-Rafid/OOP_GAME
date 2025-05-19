package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Button {
    public Rectangle bounds;
    public String label;
    public Texture texture;
    public BitmapFont font;

    public Button(String label, float x, float y, float width, float height, Texture texture, BitmapFont font) {
        this.label = label;
        this.texture = texture;
        this.font = font;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        float textWidth = font.getRegion().getRegionWidth();
        float textX = bounds.x + (bounds.width - textWidth) / 2 + 50;
        float textY = bounds.y + bounds.height / 2 + 20;
        font.draw(batch, label, textX, textY);
    }

    public boolean isClicked(Vector3 touchPos) {
        return bounds.contains(touchPos.x, touchPos.y);
    }
}
