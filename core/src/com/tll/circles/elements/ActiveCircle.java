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
    private int mSpeed = 50;
    private int mRotationAngleSpeed = 3;
    private Color mColor = Color.BLACK;
    private float mTimeout;
    private boolean endCircle;

    public boolean isEndCircle() {
        return endCircle;
    }

    public void setEndCircle(boolean endCircle) {
        this.endCircle = endCircle;
    }

    public ActiveCircle(Size size,Vector3 position,Color color,int speed){
        mColor = color;
        mSpeed = speed;
        mSprite = new Sprite(AssetManager.blackCircle);
        mSprite.setSize(size.width, size.height);
        mSprite.setPosition(position.x, position.y);
        mSprite.setOriginCenter();
    }
    public float getX(){
        return mSprite.getX();
    }
    public float getY(){
        return mSprite.getY();
    }
    public int getSpeed(){
        return mSpeed;
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
    public float getOriginX(){
        return mSprite.getOriginX();
    }
    public float getOriginY(){
        return mSprite.getOriginY();
    }
    public ActiveCircle(Size size,Vector3 position,int speed){
        this(size,position,Color.BLACK,speed);
    }
    public ActiveCircle(Vector3 position,int speed){
        this(new Size(250,250),position,speed);
    }

    @Override
    public void render(SpriteBatch sb) {
        mSprite.draw(sb);
    }

    @Override
    public void update(float dt) {
    }
}
