package com.tll.circles.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.tll.circles.AssetManager;
import com.tll.circles.Size;

/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public class ActiveCircle extends Element {
    public static final String TAG = "ActiveCircle";
    private float mRotationAngleSpeed = 5;
    private float mTimeout = 5;
    protected Arrow attachedArrow;
    private  float ALPHA_THRESHOLD = 0.5f;
    private float lastAlphaUpdateTime;
    private float currentUpdateTime;
    private float alpha = 1.0f;
    public ActiveCircle (Size size,Vector3 position){
        mSprite = new Sprite(AssetManager.blackCircle);
        mSprite.setSize(size.width, size.height);
        mSprite.setPosition(position.x, position.y);
        mSprite.setOriginCenter();
    }
    public void setRotationAngleSpeed(float rotationAngleSpeed){
        mRotationAngleSpeed  = rotationAngleSpeed;
    }
    public void setTimeOut(float timeOut){

        ALPHA_THRESHOLD = timeOut/10;
        mTimeout = timeOut;
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
    public float getRotationAngleSpeed(){
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
        if(attachedArrow!=null){
            mSprite.rotate(-getRotationAngleSpeed());
            if(!(this instanceof SafeActiveCircle)){
                currentUpdateTime += dt;
                Gdx.app.log("TEST",String.valueOf(currentUpdateTime));
                if(currentUpdateTime - lastAlphaUpdateTime > 0.5f){
                    alpha -= 0.1f;
                    lastAlphaUpdateTime = currentUpdateTime;
                    mSprite.setAlpha(alpha);
                    if(alpha <= 0){
                        Gdx.app.log("TEST","YANDI");
                    }
                }
            }
        }

    }
}
