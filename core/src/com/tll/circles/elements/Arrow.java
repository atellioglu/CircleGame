package com.tll.circles.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public class Arrow extends Element{
    private static final float SHADOW_ANIMATION_DELTA = 0.20f;
    private static int VELOCITYX = 250,VELOCITYY = 250;
    private static final int WIDTH = 48,HEIGHT = 60;
    //okun uzerinde bulundugu yuvarlak!
    private ActiveCircle mActiveCircle;
    public Vector3 velocity;
    private ActiveCircle mLastAttachedCircle;
    private ShapeRenderer shapeRenderer;
    private float alpha = 1;
    private List<ArrowShadow> shadowAnimation;
    private State state;
    public Sprite getSprite(){
        return mSprite;
    }

    public Arrow(ActiveCircle activeCircle){
        mActiveCircle = activeCircle;
        mSprite = new Sprite(com.tll.circles.AssetManager.defaultArrow);
        mSprite.setSize(WIDTH, HEIGHT);
        //mSprite.setPosition(activeCircle.getX() + activeCircle.getWidth() / 2 - mSprite.getWidth() / 2, activeCircle.getY() + mSprite.getHeight() / 2);
        mSprite.setPosition(activeCircle.getX() + activeCircle.getWidth() - mSprite.getWidth() / 2, activeCircle.getY() + activeCircle.getHeight()/2 - mSprite.getHeight() / 2);
        mActiveCircle = activeCircle;
        velocity = new Vector3(VELOCITYX,VELOCITYY,0);
        mSprite.setOriginCenter();
        shadowAnimation = new ArrayList<>(10);
        state = State.ON_CIRCLE;
        shapeRenderer = new ShapeRenderer();
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
    public void die(){
        state = State.DYING;
    }
    private float lastAnimationUpdateDelta = 0.0f;
    @Override
    public void render(SpriteBatch sb) {
        mSprite.draw(sb);
        int i =0;
        while(i < shadowAnimation.size()){
            ArrowShadow arrowShadow = shadowAnimation.get(i);
            arrowShadow.render(sb);
            i++;
        }
        /*shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.CYAN);
        float[] vertices = mSprite.getVertices();
        shapeRenderer.triangle(
                vertices[SpriteBatch.X2],vertices[SpriteBatch.Y2],
                vertices[SpriteBatch.X3],vertices[SpriteBatch.Y3],
                (vertices[SpriteBatch.X1] + vertices[SpriteBatch.X4])/2, (vertices[SpriteBatch.Y1] + vertices[SpriteBatch.Y4])/2
                );
        shapeRenderer.rect(mSprite.getBoundingRectangle().x,mSprite.getBoundingRectangle().y,mSprite.getBoundingRectangle().width,mSprite.getBoundingRectangle().height);
        shapeRenderer.end();
        Gdx.app.log("Arrow", Arrays.toString(mSprite.getVertices()));*/
    }
    @Override
    public void update(float dt) {
        //arkasindaki animasyonlar her kosulda devam etmeli.
        updateArrowShadowAnimation(dt);
        if(state == State.DYING){
            updateDyingAnimation(dt);
        }
        if(state == State.DEAD || state == State.DYING){
            //Eger olduyse devamini yapma!
            return;
        }

        float xx,yy;
        if(mActiveCircle == null){
            state = State.STRAIGHT;
            Vector2 current = new Vector2(mSprite.getX(),mSprite.getY());
            float rotationAngle = (mSprite.getRotation()%360)+360+270;
            xx =  current.x + (float)Math.cos(Math.toRadians(rotationAngle)) *velocity.x * dt;
            yy =  current.y + (float)Math.sin(Math.toRadians(rotationAngle)) *velocity.y * dt;
            mSprite.setPosition(xx,yy);

            //Duz ilerlerken arkasinda cikan oklari olusturma
            lastAnimationUpdateDelta += dt;
            if(lastAnimationUpdateDelta >= SHADOW_ANIMATION_DELTA){
                //yeni bir tane cizilecek!
                shadowAnimation.add(new ArrowShadow());
                lastAnimationUpdateDelta = 0;
            }
            //duz ilerleme!
        }else{
            state = State.ON_CIRCLE;
            shadowAnimation.clear();
            lastAnimationUpdateDelta = 0;
            mSprite.rotate(-mActiveCircle.getRotationAngleSpeed());
            float rotationAngle = mSprite.getRotation();
            // TODO: 18/09/17  mActiveCircle.getWidth()/2-5  yerine daha duzgun bir cozum bul!
            Vector2 vec2 = calculateOrbit(
                    rotationAngle,
                    mActiveCircle.getWidth()/2 * 92 / 100 - 5,
                    new Vector2(mActiveCircle.getX() + mActiveCircle.getWidth() / 2,
                    mActiveCircle.getY() + mActiveCircle.getHeight() / 2));

            mSprite.setPosition(vec2.x-mSprite.getWidth()/2,vec2.y-mSprite.getHeight()/2);

        }
    }
    private void updateDyingAnimation(float dt){
        alpha -= dt/ 0.50f;
        if(alpha <= 0){
            state = State.DEAD;
            mSprite.setAlpha(0);
        }else{
            mSprite.setAlpha(alpha);
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
    public State getState(){
        return state;
    }
    private void updateArrowShadowAnimation(float dt){
        int i =0;
        while(i < shadowAnimation.size()){
            ArrowShadow arrowShadow = shadowAnimation.get(i);
            if(arrowShadow.alpha <= 0){
                shadowAnimation.remove(arrowShadow);
            }else{
                arrowShadow.update(dt);
            }
            i++;
        }
    }
    public enum State{
        ON_CIRCLE,
        STRAIGHT,
        DYING,
        DEAD,
        FINISHING,
        FINISHED
    }
    private class ArrowShadow extends Element{
        private float alpha = 0.8f;
        private static final float ANIMATION_FINISH_TIME = 0.5f;
        public ArrowShadow() {
            mSprite = new Sprite(Arrow.this.mSprite);
            mSprite.setAlpha(alpha);
        }

        @Override
        public void render(SpriteBatch sb) {
            mSprite.draw(sb);
        }

        @Override
        public void update(float dt) {
            alpha -= dt/ANIMATION_FINISH_TIME;
            if(alpha <= 0)
                alpha = 0;
            float scale = mSprite.getScaleX();
            scale -= dt/ ANIMATION_FINISH_TIME;
            mSprite.setScale(scale);
            Gdx.app.log(ArrowShadow.class.getSimpleName(),String.valueOf(mSprite.getScaleX()));
            mSprite.setAlpha(alpha);
        }

        @Override
        public String toString() {
            return "ArrowShadow{" +
                    "alpha=" + alpha +
                    "position =" +mSprite.getX() +" , "+mSprite.getY()+
                    '}';
        }
    }
}
