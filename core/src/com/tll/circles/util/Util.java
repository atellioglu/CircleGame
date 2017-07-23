package com.tll.circles.util;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by abdullahtellioglu on 23/07/17.
 */
public class Util {
    /**
     * Kare ile cemberin carpismasini hesaplayan metod
     * @param circle
     * @param rectangle
     * @return
     */
    public static boolean isCollided(Sprite circle,Sprite rectangle){
        float circleRadius = circle.getWidth()/2;
        float circleCenterX = circle.getX()+circle.getWidth()/2;
        float circleCenterY = circle.getY()+circle.getHeight()/2;
        float distX = Math.abs(circleCenterX - rectangle.getX() - rectangle.getWidth()/2);
        float distY = Math.abs(circleCenterY - rectangle.getY() - rectangle.getHeight()/2);
        if(distX > (rectangle.getWidth()/2 + circleRadius)){
            return false;
        }
        if(distY >(rectangle.getHeight()/2 + circleRadius)){
            return false;
        }
        if(distX <= rectangle.getWidth()/2){
            return true;
        }
        if(distY <= rectangle.getHeight()/2){
            return true;
        }
        float dx = distX - rectangle.getWidth()/2;
        float dy = distY - rectangle.getHeight()/2;
        if(dx*dx + dy*dy <= (circleRadius * circleRadius)){
            return true;
        }
        return false;
    }
}
