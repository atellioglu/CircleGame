package com.tll.circles.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.tll.circles.AssetManager;

import org.w3c.dom.css.Rect;

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
    private float alpha =1f;
    public void setAlpha(float alpha){
        this.alpha = alpha;
    }
    public ShapeRenderer getRenderer(){
        return renderer;
    }

    @Override
    public void render(SpriteBatch sb) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0.09f,0.89f,0.933f,alpha);
        for(int i =0;i<rectangles.size();i++){
            Rectangle rec = rectangles.get(i);
            if(rec.getWidth() > rec.getHeight()){
                /*
                for(int j=0;j<rec.getWidth();j+=25){
                    // TODO: 18/09/17 Burada 25 her zaman cizdirilmeyecek. Eger x +25 buyukse genislikten daha kisa cizdir. Yukseklik icin de ayni!
                    renderer.rect(rec.getX()+j,rec.getY(),20,rec.getHeight());
                }*/
                renderer.rect(rec.getX(),rec.getY(),rec.getWidth(),rec.getHeight());
            }else{
                /*
                for(int j=0;j<rec.getHeight();j+=25){
                    renderer.rect(rec.getX(),rec.getY()+j,rec.getWidth(),20);
                }*/
                renderer.rect(rec.getX(),rec.getY(),rec.getWidth(),rec.getHeight());
            }

        }

        renderer.end();
    }
    public boolean checkCollision(Sprite arrow){

        for(int i =0;i<rectangles.size();i++){
            Rectangle rect = rectangles.get(i);
            Vector2 rectP1 = new Vector2(rect.x,rect.y);
            Vector2 rectP2;
            if(rect.getWidth() > rect.getHeight()){
                rectP2 = new Vector2(rect.x+rect.getWidth(),rect.y);
            }else{
                rectP2 = new Vector2(rect.x, rect.y + rect.getHeight());
            }
            Vector2 arrowP1 = new Vector2( arrow.getVertices()[SpriteBatch.X2],arrow.getVertices()[SpriteBatch.Y2]);
            Vector2 arrowMiddle = new Vector2((arrow.getVertices()[SpriteBatch.X1] + arrow.getVertices()[SpriteBatch.X4])/2, (arrow.getVertices()[SpriteBatch.Y1] + arrow.getVertices()[SpriteBatch.Y4])/2);
            Vector2 arrowP2 = new Vector2(arrow.getVertices()[SpriteBatch.X3],arrow.getVertices()[SpriteBatch.Y3]);

            Vector2 int1 = checklinescollide(arrowP1.x,arrowP1.y,arrowMiddle.x,arrowMiddle.y,rectP1.x,rectP1.y,rectP2.x,rectP2.y);

            if(int1 == null){
                Vector2 int2 = checklinescollide(arrowP2.x,arrowP2.y,arrowMiddle.x,arrowMiddle.y,rectP1.x,rectP1.y,rectP2.x,rectP2.y);
                if(int2 != null){

                    Gdx.app.log("Collision 2 : ",int2.toString() + "Line P1 : "+rectP1.toString()+" Line P2 : "+rectP2.toString());
                    return true;
                }
            }else{
                Gdx.app.log("Collision 1 : ",int1.toString());
                return true;
            }

        }
        return false;
    }

    Vector2 checklinescollide(float x1, float y1, float x2, float y2,
                            float x3, float y3, float x4, float y4){
        float A1 = y2-y1;
        float B1 = x1-x2;
        float C1 = A1*x1 + B1*y1;
        float A2 = y4-y3;
        float B2 = x3-x4;
        float C2 = A2*x3 + B2*y3;
        float det = A1*B2-A2*B1;
        if(det != 0){
            float x = (B2*C1 - B1*C2)/det;
            float y = (A1*C2 - A2*C1)/det;
            if(x >= Math.min(x1, x2) && x <= Math.max(x1, x2)
                    && x >= Math.min(x3, x4) && x <= Math.max(x3, x4)
                    && y >= Math.min(y1, y2) && y <= Math.max(y1, y2)
                    && y >= Math.min(y3, y4) && y <= Math.max(y3, y4))
                return new Vector2(x, y);
        }
        return null;
    }
    public static int orientation(Vector2 p, Vector2 q, Vector2 r) {
        double val = (q.y - p.y) * (r.x - q.x)
                - (q.x - p.x) * (r.y - q.y);

        if (val == 0.0)
            return 0; // colinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    public static boolean intersect(Vector2 p1, Vector2 q1, Vector2 p2, Vector2 q2) {

        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4)
            return true;

        return false;
    }

    @Override
    public void update(float dt) {

    }
}
