package com.tll.circles.elements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tll.circles.AssetManager;

/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public class Arrow extends Element{
    //okun uzerinde bulundugu yuvarlak!
    private ActiveCircle mActiveCircle;
    private boolean clockwise;
    public Vector3 velocity;
    public Arrow(){

    }
    //oyuna baslarken kullanilacak olan constructor
    public Arrow(ActiveCircle activeCircle){
        mActiveCircle = activeCircle;
        mSprite = new Sprite(AssetManager.defaultArrow);
        mSprite.setSize(50,50);
        mSprite.setPosition(activeCircle.getX() + activeCircle.getWidth() / 2 - mSprite.getWidth() / 2, activeCircle.getY() + mSprite.getHeight() / 2);
        attach(activeCircle);
        velocity = new Vector3(activeCircle.getSpeed(),activeCircle.getSpeed(),0);
    }

    public void attach(ActiveCircle activeCircle){
        mActiveCircle = activeCircle;
        mSprite.setOriginCenter();
    }
    public void detach(ActiveCircle activeCircle){
        mActiveCircle = null;
    }

    public boolean isClockwise() {
        return clockwise;
    }

    public void setClockwise(boolean clockwise) {
        this.clockwise = clockwise;
    }

    @Override
    public void render(SpriteBatch sb) {
        mSprite.draw(sb);
    }

    @Override
    public void update(float dt) {
        if(mActiveCircle == null){
            mSprite.setPosition(velocity.x*dt,velocity.y * dt);
            //duz ilerleme!
        }else{

            if(clockwise){
                mSprite.rotate(mActiveCircle.getSpeed());
            }else{
                mSprite.rotate(-mActiveCircle.getSpeed());
            }
            float rotationAngle = mSprite.getRotation();

            Vector2 vec2= calculateOrbit(rotationAngle,mActiveCircle.getWidth()/2-mSprite.getHeight()/2,new Vector2(mActiveCircle.getX()+mActiveCircle.getWidth()/2,mActiveCircle.getY()+mActiveCircle.getHeight()/2));
            mSprite.setPosition(vec2.x-mSprite.getWidth()/2,vec2.y-mSprite.getHeight()/2);
            //rotasyon
        }
    }
    public Vector2 calculateOrbit(float currentOrbitDegrees, float distanceFromCenterPoint, Vector2 centerPoint) {
        float radians = (float)Math.toRadians(currentOrbitDegrees);

        float x = (float)(Math.cos(radians) * distanceFromCenterPoint) + centerPoint.x;
        float y = (float)(Math.sin(radians) * distanceFromCenterPoint) + centerPoint.y;

        return new Vector2(x, y);
    }
}
