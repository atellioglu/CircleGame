package com.tll.circles.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.tll.circles.AssetManager;
import com.tll.circles.Theme;
import com.tll.circles.ThemeFactory;

import java.util.Random;

/**
 * Created by abdullahtellioglu on 23/07/17.
 */
public class Star extends Element {
    private int value = 1;
    private boolean visible = true;
    private Arrow mArrow;
    private StarAnimation starAnimation;
    public Star(Rectangle rectangle){
        mSprite = new Sprite(AssetManager.star);
        mSprite.setSize(rectangle.getWidth(), rectangle.getHeight());
        mSprite.setPosition(rectangle.getX(),rectangle.getY());
        mSprite.setOriginCenter();
    }
    public Star(Rectangle rectangle,int value){
        this(rectangle);
        setValue(value);
    }
    public Star(Rectangle rectangle,int value,Arrow arrow){
        this(rectangle,value);
        setArrow(arrow);
    }
    public void setValue(int value){
        this.value =value;
    }
    public int getValue(){
        return value;
    }
    @Override
    public void render(SpriteBatch sb) {
        if(visible){
            mSprite.draw(sb);
        }
        if(starAnimation!=null && !starAnimation.isFinished()){
            starAnimation.render(sb);
        }
    }
    public void setArrow(Arrow arrow){
        mArrow = arrow;
    }
    private StarHitListener starHitListener;
    public void setStarHitListener(StarHitListener starHitListener){
        this.starHitListener = starHitListener;
    }
    @Override
    public void update(float dt) {
        if(starAnimation!=null && !starAnimation.isFinished()){
            starAnimation.update(dt);
        }
        if(mArrow!=null){
            if(visible &&  mArrow.getSprite().getBoundingRectangle().contains(mSprite.getX()+mSprite.getWidth()/2,mSprite.getY()+mSprite.getHeight()/2)){
                starHitListener.onHit(value);
                starAnimation = new StarAnimation(mSprite.getX()+mSprite.getWidth()/2,mSprite.getY()+mSprite.getHeight()/2);

                visible = false;
            }
        }
    }

    public interface StarHitListener{
        void onHit(int value);
    }

    private class StarAnimation{
        private final int ARRAY_LENGTH = 50;
        private Sprite[] sprites;
        private Vector3[] velocities;
        private float[] alphas;
        private float interpolTime;
        public StarAnimation(float x,float y){
            sprites  = new Sprite[ARRAY_LENGTH];
            Random rnd = new Random();
            velocities = new Vector3[ARRAY_LENGTH];
            alphas = new float[ARRAY_LENGTH];
            for(int i =0;i<sprites.length;i++){
                alphas[i] = 1;
                sprites[i] = new Sprite(AssetManager.star);
                sprites[i].setPosition(x,y);
                sprites[i].setSize(15,15);
                velocities[i] = new Vector3(rnd.nextInt(250)-125,rnd.nextInt(250)-125,0);
            }
        }
        public boolean isFinished(){
            if(interpolTime >= 1){
                return true;
            }
            return false;
        }
        public void update(float dt){
            interpolTime += dt;
            if(interpolTime >=1){
                Gdx.app.log("StarAnimation","Timeout for animation!");
                return;
            }
            for(int i =0;i<ARRAY_LENGTH;i++){
                sprites[i].setPosition(velocities[i].x * dt +sprites[i].getX(),sprites[i].getY() + velocities[i].y * dt);
                alphas[i] -= dt;
                sprites[i].setAlpha(alphas[i]);

            }
        }
        public void render(SpriteBatch sb){
            for(int i =0;i<sprites.length;i++){
                sprites[i].draw(sb);
            }
        }
    }
}
