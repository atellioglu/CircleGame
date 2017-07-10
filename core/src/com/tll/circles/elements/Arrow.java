package com.tll.circles.elements;

import com.badlogic.gdx.Gdx;
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
    public boolean isAttached(){
        if(mActiveCircle ==null)
            return false;
        else
            return true;
    }
    public void attach(ActiveCircle activeCircle){
        mActiveCircle = activeCircle;
        mSprite.setOriginCenter();
    }
    public void detach(){
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
            // TODO: 10/07/17 Tiklandiginda ayni dogrultuda gitmesi gerekir.
            Vector2 current = new Vector2(mSprite.getX(),mSprite.getY());
            float rotationAngle = mSprite.getRotation();
            float xx =  current.x + (float)Math.cos(rotationAngle)*velocity.x*dt;
            float yy =  current.y+ (float)Math.sin(rotationAngle) *velocity.y * dt;
            Gdx.app.log("Position ",xx + " "+yy);
            mSprite.setPosition(xx,yy);
            //duz ilerleme!
        }else{

            if(clockwise){
                mSprite.rotate(mActiveCircle.getRotationAngleSpeed());
            }else{
                mSprite.rotate(-mActiveCircle.getRotationAngleSpeed());
            }
            float rotationAngle = mSprite.getRotation();
            Gdx.app.log("Rotation",String.valueOf(rotationAngle%360));
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
