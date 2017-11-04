package com.tll.circles.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.tll.circles.Size;

/**
 * Created by abdullahtellioglu on 23/07/17.
 */
public class SafeActiveCircle extends ActiveCircle {

    private Sprite shieldNotActive;
    private Sprite shieldActive;


    public SafeActiveCircle(Texture texture, Size size, Vector3 position) {
        super(texture, size, position);
    }

    public SafeActiveCircle(Size size, Vector3 position) {
        super(size, position);
        /*shieldNotActive = new Sprite(ThemeFactory.getInstance().getTheme().shieldNotActive);
        shieldNotActive.setSize(size.width/3,size.height/3);
        shieldNotActive.setPosition(getCenterX() - shieldNotActive.getWidth() / 2, getCenterY() - shieldNotActive.getHeight() / 2);
        shieldActive = new Sprite(ThemeFactory.getInstance().getTheme().shieldActive);
        shieldActive.setSize(size.width/3,size.height/3);
        shieldActive.setPosition(getCenterX() - shieldActive.getWidth() / 2, getCenterY() - shieldActive.getHeight() / 2);
        */
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if(attachedArrow == null){
            if(shieldNotActive!=null)
                shieldNotActive.draw(sb);
        }else{
            if(shieldActive!=null)
                shieldActive.draw(sb);
        }
    }

    @Override
    public void update(float dt) {
        if(attachedArrow!=null){
            mSprite.rotate(-getRotationAngleSpeed());
        }
    }
}
