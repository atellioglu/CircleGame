package com.tll.circles.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.sun.glass.ui.Size;
import com.tll.circles.AssetManager;

/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public class ActiveCircle extends Element {
    public static final String TAG = "ActiveCircle";
    private int mRotationAngleSpeed = 5;
    private float mTimeout;
    protected Arrow attachedArrow;

    public ActiveCircle(Size size,Vector3 position){
        mSprite = new Sprite(AssetManager.blackCircle);
        mSprite.setSize(size.width, size.height);
        mSprite.setPosition(position.x, position.y);
    }
    public void attach(Arrow arrow){
        attachedArrow = arrow;
    }
    public void detach(){
        attachedArrow = null;
    }
    public Sprite getSprite(){
        return mSprite;
    }
    public float getCenterX(){
        return getX() + getWidth()/2;
    }
    public float getCenterY(){
        return getY() + getHeight()/2;
    }
    public float getX(){
        return mSprite.getX();
    }
    public float getY(){
        return mSprite.getY();
    }
    public int getRotationAngleSpeed(){
        return mRotationAngleSpeed;
    }
    public float getWidth(){
        return mSprite.getWidth();
    }
    public float getHeight(){
        return mSprite.getHeight();
    }

    @Override
    public void render(SpriteBatch sb) {
        mSprite.draw(sb);
    }

    @Override
    public void update(float dt) {
    }
}
