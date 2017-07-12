package com.tll.circles;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.sun.glass.ui.Size;
import com.tll.circles.elements.ActiveCircle;
import com.tll.circles.elements.Arrow;
import com.tll.circles.elements.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public class GameState extends State {
    private List<Element> elements;
    private Arrow userArrow;
    private boolean paused = false,started=true;
    private int levelIndex;
    public GameState(GameStateManager gsm,int levelIndex) {
        super(gsm);
        this.levelIndex = levelIndex;
        init();
    }

    private TmxMapLoader loader;

    private TiledMap tiledMap;

    private OrthogonalTiledMapRenderer tiledMapRenderer;

    @Override
    protected void init() {
        // TODO: 10/07/17 Harita burada olusturulacak

        loader = new TmxMapLoader();
        tiledMap = loader.load(String.format("level%d.tmx",levelIndex));
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);


        elements = new ArrayList<Element>();
        int count = tiledMap.getLayers().getCount();
        for(MapObject object : tiledMap.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
            ActiveCircle activeCircle = new ActiveCircle(new Size((int)rectangle.getWidth()*2,(int)rectangle.getHeight()*2),new Vector3(rectangle.x,rectangle.y,0),100);
            elements.add(activeCircle);
            if(object.getName().equals("start")){
                userArrow = new Arrow(activeCircle);
                elements.add(userArrow);
            }else if(object.getName().equals("end")){
                activeCircle.setEndCircle(true);
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        tiledMapRenderer.render();
        sb.begin();
        for(int i =0;i<elements.size();i++){
            elements.get(i).render(sb);
        }
        sb.end();
    }

    @Override
    public void dispose() {

    }

    private ActiveCircle checkAttach(){
        if(System.currentTimeMillis() - lastDetachTime <=500){
            return null;
        }
        if(userArrow.isAttached()){
            return null;
        }
        float arrowCenterX = userArrow.getSprite().getX()+userArrow.getSprite().getWidth()/2;
        float arrowCenterY = userArrow.getSprite().getY()+userArrow.getSprite().getHeight()/2;
        for(int i =0;i<elements.size();i++){
            if(elements.get(i) instanceof ActiveCircle){
                ActiveCircle activeCircle = (ActiveCircle)elements.get(i);
                float centerX = activeCircle.getX()+activeCircle.getWidth()/2;
                float centerY = activeCircle.getY()+activeCircle.getHeight()/2;
                float distance = (float)Math.sqrt(Math.pow(centerX-arrowCenterX,2)+Math.pow(centerY-arrowCenterY,2));
                if(distance<=activeCircle.getWidth()/2){
                    return activeCircle;
                }
            }
        }
        return null;
    }
    @Override
    public void update(float dt) {
        tiledMapRenderer.setView(camera);
        ActiveCircle activeCircle = checkAttach();
        if(activeCircle != null){
            userArrow.attach(activeCircle);

        }
        for(int i =0;i<elements.size();i++){
            elements.get(i).update(dt);
        }
    }

    private long lastDetachTime =0;
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(!paused){
            userArrow.detach();
            lastDetachTime = System.currentTimeMillis();
        }
        if(screenX <= 100 ){
            gsm.set(new GameState(gsm,levelIndex));
        }
        return false;
    }
}
