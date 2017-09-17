package com.tll.circles.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.tll.circles.AssetManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by abdullahtellioglu on 06/08/17.
 */
public class DefaultAnimation {
    private int x,y;
    private float timeOut;
    private long startedAnimationTime;
    private List<AnimationElement> list;
    public DefaultAnimation(int x, int y,AnimationColor color){
        this.x = x;
        this.y = y;
        list = new ArrayList<AnimationElement>();
        switch (color){
            case YELLOW:
                for(int i =0;i<new Random().nextInt(30)+20;i++){
                    AnimationElement element = new AnimationElement();
                    Sprite sprite = new Sprite(AssetManager.yellowRectangle);
                    sprite.setSize(30,30);
                    sprite.setOriginCenter();
                    sprite.setX(this.x);
                    sprite.setY(this.y);
                    element.sprite = sprite;
                    list.add(element);
                }
                break;
            case BLACK:
                break;
        }
    }

    public void setTimeOut(float timeOut){
        this.timeOut = timeOut;
    }
    public void render(SpriteBatch sb){

    }
    public void notifyAnimate(){
        for(int i =0;i<list.size();i++){
            Sprite sprite = list.get(i).sprite;
            sprite.setX(this.x);
            sprite.setY(this.y);
            sprite.setAlpha(1);

            sprite.setRotation(0);
        }
    }
    public void start(){
        startedAnimationTime = System.currentTimeMillis();
        startedAnimationTime += timeOut*1000;
    }
    public void update(float df){
        for(int i =0;i<list.size();i++){
            AnimationElement element = list.get(i);

        }
    }
    private class AnimationElement{
        Sprite sprite;
        Vector2 velocity;
    }
    public AnimationListener animationListener;
    public void setAnimationListener(AnimationListener animationListener){
        this.animationListener = animationListener;
    }
    public interface AnimationListener{
        void onFinished();
    }
    public enum AnimationColor{
        YELLOW,
        BLACK
    }
}
