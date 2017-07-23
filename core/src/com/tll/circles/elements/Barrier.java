package com.tll.circles.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdullahtellioglu on 23/07/17.
 */
public class Barrier extends Element {
    private List<Rectangle> rectangles;
    private ShapeRenderer renderer;
    public Barrier(){
        rectangles = new ArrayList<Rectangle>();
        renderer = new ShapeRenderer();
    }
    public void addRectangle(Rectangle rectangle){
        rectangles.add(rectangle);
    }
    public List<Rectangle> getRectangles(){
        return rectangles;
    }
    @Override
    public void render(SpriteBatch sb) {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        for(int i =0;i<rectangles.size();i++){
            Rectangle rec = rectangles.get(i);
            renderer.rect(rec.getX(),rec.getY(),rec.getWidth(),rec.getHeight());
        }
        renderer.end();
    }
    public boolean checkCollision(Sprite arrow){
        for(int i =0;i<rectangles.size();i++){
            boolean overLaps = rectangles.get(i).overlaps(arrow.getBoundingRectangle());
            if(overLaps)
                return true;
        }
        return false;
    }
    @Override
    public void update(float dt) {

    }
}
