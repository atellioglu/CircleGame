package com.tll.circles.elements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public abstract class Element {
    protected boolean interaction = true;

    public boolean hasInteraction() {
        return interaction;
    }

    protected Sprite mSprite;
    public abstract void render(SpriteBatch sb);
    public abstract void update(float dt);
}
