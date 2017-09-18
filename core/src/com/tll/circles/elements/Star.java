package com.tll.circles.elements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tll.circles.AssetManager;

/**
 * Created by abdullahtellioglu on 23/07/17.
 */
public class Star extends Element {
    private int value = 1;
    private boolean visible = true;
    public Star(Rectangle rectangle){
        mSprite = new Sprite(AssetManager.star);
        mSprite.setSize(rectangle.getWidth(), rectangle.getHeight());
        mSprite.setPosition(rectangle.getX(),rectangle.getY());
        mSprite.setOriginCenter();
    }
    @Override
    public void render(SpriteBatch sb) {
        if(visible){
            mSprite.draw(sb);
        }

    }

    @Override
    public void update(float dt) {

    }
}
