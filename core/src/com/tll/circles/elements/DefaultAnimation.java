package com.tll.circles.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;
import java.util.Random;

/**
 * Created by abdullahtellioglu on 06/08/17.
 */
public class DefaultAnimation {
    private int x,y;
    private float timeOut;
    private Color color;

    public DefaultAnimation(int x, int y,Color color){
        this.x = x;
        this.y = y;
        this.color = color;
        create();
    }
    private void create(){
        for(int i =0;i<new Random().nextInt(30)+50;i++){

        }
    }

    public void setTimeOut(float timeOut){
        this.timeOut = timeOut;
    }
    public void render(SpriteBatch sb){

    }
    public void update(float df){

    }
    public class CrashElement{
        private int x,y;
        private float rotation;
        private float alpha;
    }
    public AnimationListener animationListener;
    public void setAnimationListener(AnimationListener animationListener){
        this.animationListener = animationListener;
    }
    public interface AnimationListener{
        void onFinished();
    }
}
