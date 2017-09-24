package com.tll.circles.elements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tll.circles.ThemeFactory;


/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public class Arrow extends Element{

    private static final int VELOCITYX = 250,VELOCITYY = 250;
    private static final int WIDTH = 48,HEIGHT = 48;
    //okun uzerinde bulundugu yuvarlak!
    private ActiveCircle mActiveCircle;
    public Vector3 velocity;
    private ActiveCircle mLastAttachedCircle;
    private ShapeRenderer shapeRenderer;
    private boolean dead = false;
    private boolean visibility = true;
    private float alpha = 1;
    public Sprite getSprite(){
        return mSprite;
    }
    public Arrow(ActiveCircle activeCircle){
        mActiveCircle = activeCircle;
        mSprite = new Sprite(ThemeFactory.getInstance().getTheme().arrow);
        mSprite.setSize(WIDTH, HEIGHT);
        //mSprite.setPosition(activeCircle.getX() + activeCircle.getWidth() / 2 - mSprite.getWidth() / 2, activeCircle.getY() + mSprite.getHeight() / 2);
        mSprite.setPosition(activeCircle.getX() + activeCircle.getWidth() - mSprite.getWidth() / 2, activeCircle.getY() + activeCircle.getHeight()/2 - mSprite.getHeight() / 2);
        mActiveCircle = activeCircle;
        velocity = new Vector3(VELOCITYX,VELOCITYY,0);
        mSprite.setOriginCenter();
    }
    public ActiveCircle getAttached(){
        return mActiveCircle;
    }
    public boolean isAttached(){
        if(mActiveCircle ==null)
            return false;
        else
            return true;
    }
    public void attach(ActiveCircle activeCircle){
        float angle = (float)Math.atan2(mSprite.getY()+mSprite.getHeight()/2 - activeCircle.getCenterY(),mSprite.getX()+mSprite.getWidth()/2 - activeCircle.getCenterX())* MathUtils.radiansToDegrees;
        mSprite.setRotation(angle);
        mActiveCircle = activeCircle;

    }
    public ActiveCircle getLastAttachedCircle(){
        return mLastAttachedCircle;
    }
    public void detach() {
        mLastAttachedCircle = mActiveCircle;
        mActiveCircle = null;
    }
    public void setShapeRenderer(ShapeRenderer shapeRenderer){
        this.shapeRenderer = shapeRenderer;
    }
    public void setFinished(EndCircle endCircle){

    }
    private AnimationListener animationListener;
    public void setAnimationListener(AnimationListener listener){
        this.animationListener = listener;
    }
    public void die(){
        visibility = false;
    }
    public boolean isDead(){
        return dead;
    }
    @Override
    public void render(SpriteBatch sb) {

        mSprite.draw(sb);
    }

    @Override
    public void update(float dt) {
        if(dead)
            return;
        if(!visibility){
            alpha -= dt/ 0.50f;
            if(alpha <= 0){
                dead = true;
            }else{
                mSprite.setAlpha(alpha);
            }
            return;
        }

        float xx,yy;
        if(mActiveCircle == null){
            Vector2 current = new Vector2(mSprite.getX(),mSprite.getY());
            float rotationAngle = (mSprite.getRotation()%360)+360+270;
            xx =  current.x + (float)Math.cos(Math.toRadians(rotationAngle)) *velocity.x * dt;
            yy =  current.y + (float)Math.sin(Math.toRadians(rotationAngle)) *velocity.y * dt;
            mSprite.setPosition(xx,yy);
            //duz ilerleme!
        }else{
            mSprite.rotate(-mActiveCircle.getRotationAngleSpeed());
            float rotationAngle = mSprite.getRotation();
            // TODO: 18/09/17  mActiveCircle.getWidth()/2-5  yerine daha duzgun bir cozum bul!
            Vector2 vec2 = calculateOrbit(
                    rotationAngle,
                    mActiveCircle.getWidth()/2-5 ,
                    new Vector2(mActiveCircle.getX() + mActiveCircle.getWidth() / 2,
                    mActiveCircle.getY() + mActiveCircle.getHeight() / 2));

            mSprite.setPosition(vec2.x-mSprite.getWidth()/2,vec2.y-mSprite.getHeight()/2);
        }
    }

    public Vector2 calculateOrbit(float currentOrbitDegrees, float distanceFromCenterPoint, Vector2 centerPoint) {
        float radians = (float)Math.toRadians(currentOrbitDegrees);

        float x = (float)(Math.cos(radians) * distanceFromCenterPoint) + centerPoint.x;
        float y = (float)(Math.sin(radians) * distanceFromCenterPoint) + centerPoint.y;

        return new Vector2(x, y);
    }
    public interface AnimationListener{
        void onAnimationStart(boolean success);
        void onAnimationFinish(boolean success);
    }
    private class FinishAnimation{
        private float x,y;
        public FinishAnimation(float x,float y){
            this.x = x;
            this.y = y;
        }
        public void update(float dt){

        }
        public void render(SpriteBatch sb){

        }
    }
}
