package com.tll.circles.elements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.tll.circles.AssetManager;
import com.tll.circles.Size;
import com.tll.circles.ThemeFactory;

/**
 * Created by abdullahtellioglu on 23/07/17.
 */
public class EndCircle extends ActiveCircle {
    public EndCircle(Size size, Vector3 position) {
        super(size, position);
        mSprite = new Sprite(ThemeFactory.getInstance().getTheme().endCircle);
        mSprite.setSize(size.width, size.height);
        mSprite.setPosition(position.x, position.y);
        mSprite.setOriginCenter();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        mSprite.setRotation(mSprite.getRotation()-5);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

    }
}
