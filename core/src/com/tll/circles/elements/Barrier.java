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
        rectangles = new ArrayList<>();
    }

    public void addRectangle(Rectangle rectangle){

        rectangles.add(rectangle);
    }
    public ShapeRenderer getRenderer(){
        return renderer;
    }

    @Override
    public void render(SpriteBatch sb) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0.09f,0.89f,0.933f,1f);
        for(int i =0;i<rectangles.size();i++){
            Rectangle rec = rectangles.get(i);
            if(rec.getWidth() > rec.getHeight()){
                for(int j=0;j<rec.getWidth();j+=25){
                    // TODO: 18/09/17 Burada 25 her zaman cizdirilmeyecek. Eger x +25 buyukse genislikten daha kisa cizdir. Yukseklik icin de ayni!
                    renderer.rect(rec.getX()+j,rec.getY(),20,rec.getHeight());
                }
            }else{
                for(int j=0;j<rec.getHeight();j+=25){
                    renderer.rect(rec.getX(),rec.getY()+j,rec.getWidth(),20);
                }
            }

        }

        renderer.end();
    }
    public boolean checkCollision(Sprite arrow){
        for(int i =0;i<rectangles.size();i++){
            Rectangle rectangle = arrow.getBoundingRectangle();
            rectangle.setX(rectangle.getX()+5);
            rectangle.setY(rectangle.getY()+5);
            rectangle.setWidth(rectangle.getWidth()-10);
            rectangle.setHeight(rectangle.getHeight()-10);
            boolean overLaps = rectangles.get(i).overlaps(rectangle);
            if(overLaps)
                return true;
        }
        return false;
    }
    @Override
    public void update(float dt) {

    }
}
