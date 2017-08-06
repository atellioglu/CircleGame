package com.tll.circles.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.tll.circles.AssetManager;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdullahtellioglu on 23/07/17.
 */
public class Barrier extends Element {
    private ShapeRenderer renderer;
    private List<Rectangle> rectangles;
    public Barrier(){
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        rectangles = new ArrayList<Rectangle>();
    }
    public void addRectangle(Rectangle rectangle){

        rectangles.add(rectangle);
    }
    public List<Rectangle> getRectangles(){
        return rectangles;
    }

    @Override
    public void render(SpriteBatch sb) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.DARK_GRAY);
        for(int i =0;i<rectangles.size();i++){
            Rectangle rec = rectangles.get(i);
            renderer.rect(rec.getX(),rec.getY(),rec.getWidth(),rec.getHeight());
        }
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        for(int i =0;i<rectangles.size();i++){
            Rectangle rec = rectangles.get(i);
            for(int j =(int)rec.x;j<rec.x+rec.width-10;j+=20){
                renderer.line(j,rec.getY(),j+5,rec.getY()+rec.getHeight());
            }
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
