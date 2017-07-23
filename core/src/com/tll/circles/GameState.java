package com.tll.circles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.glass.ui.Size;
import com.tll.circles.elements.ActiveCircle;
import com.tll.circles.elements.Arrow;
import com.tll.circles.elements.Barrier;
import com.tll.circles.elements.Element;
import com.tll.circles.elements.EndCircle;
import com.tll.circles.elements.SafeActiveCircle;
import com.tll.circles.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public class GameState extends InputAdapter implements Screen {
    private List<Element> elements;
    private Barrier barriers;
    private Arrow userArrow;
    private boolean paused = false,started=true;
    private int levelIndex;
    private OrthographicCamera camera;
    private Viewport viewport;

    private TmxMapLoader loader;

    private TiledMap tiledMap;
    //test icin
    private ShapeRenderer shapeRenderer;

    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private MyGdxGame game;
    public GameState(MyGdxGame game,int levelIndex){
        this.game = game;
        this.levelIndex = levelIndex;
        camera = new OrthographicCamera();
        viewport = new ScalingViewport(Scaling.fillX,MyGdxGame.WIDTH,MyGdxGame.HEIGHT,camera);
        loader = new TmxMapLoader();
        tiledMap = loader.load(String.format("level%d.tmx",levelIndex));
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        elements = new ArrayList<Element>();
        barriers = new Barrier();
        loadMap();
        shapeRenderer = new ShapeRenderer();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        SpriteBatch sb = game.batch;
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        tiledMapRenderer.render();
        sb.begin();

        for(int i =0;i<elements.size();i++){
            if(!(elements.get(i) instanceof Arrow)){
                elements.get(i).render(sb);
            }
        }
        userArrow.render(sb);
        sb.end();
        // TODO: 23/07/17 TEST ICIN YAZILDI !! ILERIDE SIL
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        for(int i =0;i<elements.size();i++){
            if(elements.get(i) instanceof ActiveCircle){
                ActiveCircle activeCircle = (ActiveCircle)elements.get(i);
                shapeRenderer.rect(activeCircle.getX(),activeCircle.getY(),activeCircle.getWidth(),activeCircle.getHeight());
            }else if(elements.get(i) instanceof Arrow){
                Arrow arrow = (Arrow)elements.get(i);
                shapeRenderer.rect(arrow.getSprite().getX(),arrow.getSprite().getY(),arrow.getSprite().getWidth(),arrow.getSprite().getHeight());
            }
        }
        shapeRenderer.end();
        barriers.render(sb);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
    }


    private ActiveCircle checkAttach(){
        // TODO: 23/07/17 Burayi duzelt!
        if(System.currentTimeMillis() - lastDetachTime <=500){
            return null;
        }
        if(userArrow.isAttached()){
            return null;
        }
      
        for(int i =0;i<elements.size();i++){
            if(elements.get(i) instanceof ActiveCircle){
                ActiveCircle activeCircle = (ActiveCircle)elements.get(i);
                boolean collided = Util.isCollided(activeCircle.getSprite(),userArrow.getSprite());
                if(collided)
                    return activeCircle;
            }
        }
        return null;
    }
    public void update(float dt) {
        tiledMapRenderer.setView(camera);
        if(barriers.checkCollision(userArrow.getSprite())){
            Gdx.app.log("GG","EZ");
            game.setScreen(new GameState(game,this.levelIndex));
            return;
        }
        ActiveCircle activeCircle = checkAttach();
        if(activeCircle != null){
            userArrow.attach(activeCircle);
            activeCircle.attach(userArrow);
        }

        for(int i =0;i<elements.size();i++){
            elements.get(i).update(dt);
        }
    }

    private long lastDetachTime =0;
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(!paused){
            ActiveCircle activeCircle = userArrow.getAttached();
            if(activeCircle!=null){
                activeCircle.detach();
            }
            userArrow.detach();
            lastDetachTime = System.currentTimeMillis();
        }
        if(screenX <= 100 ){
            game.setScreen(new GameState(game,this.levelIndex));
        }
        return false;
    }
    private void loadMap(){
        //cemberleri olustur
        for(MapObject object : tiledMap.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
            ActiveCircle activeCircle = null;

            if(object.getName()!=null && object.getName().equals("start")){
                activeCircle = new SafeActiveCircle(new Size((int)rectangle.getWidth(),(int)rectangle.getHeight()),new Vector3(rectangle.x,rectangle.y,0));
                userArrow = new Arrow(activeCircle);
                activeCircle.attach(userArrow);
                elements.add(userArrow);
            }else if(object.getName()!= null && object.getName().equals("end")){
                activeCircle = new EndCircle(new Size((int)rectangle.getWidth(),(int)rectangle.getHeight()),new Vector3(rectangle.x,rectangle.y,0));
            }else{
                int type = Integer.parseInt(String.valueOf(object.getProperties().get("type")));
                switch (type){
                    case 0:
                        //safe olanlar
                        activeCircle = new SafeActiveCircle(new Size((int)rectangle.getWidth(),(int)rectangle.getHeight()),new Vector3(rectangle.x,rectangle.y,0));
                        break;
                    case 1:
                        //safe olmayanlar
                        activeCircle = new ActiveCircle(new Size((int)rectangle.getWidth(),(int)rectangle.getHeight()),new Vector3(rectangle.x,rectangle.y,0));
                        break;

                }
            }
            if(object.getProperties().get("angle")!=null){
                float angle = Float.parseFloat(String.valueOf(object.getProperties().get("angle")));
                activeCircle.setRotationAngleSpeed(angle);
            }
            elements.add(activeCircle);
        }
        //yildizlari olustur
        for(MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){

        }
        //engelleri olustur
        for(MapObject object : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
            barriers.addRectangle(rectangle);

        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
