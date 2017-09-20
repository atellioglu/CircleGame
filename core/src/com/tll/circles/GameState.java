package com.tll.circles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.tll.circles.elements.ActiveCircle;
import com.tll.circles.elements.Arrow;
import com.tll.circles.elements.Barrier;
import com.tll.circles.elements.Element;
import com.tll.circles.elements.EndCircle;
import com.tll.circles.elements.SafeActiveCircle;
import com.tll.circles.elements.Star;
import com.tll.circles.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public class GameState extends InputAdapter implements Screen {
    private static final int MENU_ITEM_SIZE =48;
    private static final int MENU_ITEM_SIZE_MARGIN = 15;
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
    private SpriteBatch menuItemBatch = new SpriteBatch();
    // TODO: 18/09/17 Arka planda hata var tum ekrani kaplamiyor!!
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private MyGdxGame game;
    private Sprite retrySprite;
    private Sprite menuSprite;
    public GameState(MyGdxGame game,int levelIndex){
        this.game = game;
        this.levelIndex = levelIndex;
        camera = new OrthographicCamera();
        viewport = new ScalingViewport(Scaling.fillX,MyGdxGame.WIDTH,MyGdxGame.HEIGHT,camera);
        //viewport = new FitViewport(MyGdxGame.WIDTH,MyGdxGame.HEIGHT,camera);
        loader = new TmxMapLoader();
        tiledMap = loader.load(String.format("level%d.tmx",levelIndex));
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        elements = new ArrayList<Element>();
        barriers = new Barrier();
        loadMap();
        shapeRenderer = new ShapeRenderer();
        Gdx.input.setInputProcessor(this);
        createHud();
    }
    private void createHud(){
        retrySprite = new Sprite(AssetManager.retry);
        retrySprite.setSize(MENU_ITEM_SIZE,MENU_ITEM_SIZE);
        retrySprite.setPosition(MyGdxGame.WIDTH-MENU_ITEM_SIZE-MENU_ITEM_SIZE_MARGIN,MENU_ITEM_SIZE_MARGIN);
        retrySprite.setOriginCenter();
        menuSprite = new Sprite(AssetManager.menu);
        menuSprite.setSize(MENU_ITEM_SIZE,MENU_ITEM_SIZE);
        menuSprite.setPosition(MENU_ITEM_SIZE_MARGIN,MENU_ITEM_SIZE_MARGIN);
        menuSprite.setOriginCenter();

    }
    @Override
    public void show() {

    }
    @Override
    public void render(float delta) {
        update(delta);
        SpriteBatch sb = game.batch;
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.765f,0.886f,0.922f,0.7f);
        for(int i =0;i<MyGdxGame.WIDTH;i+=120){
            shapeRenderer.line(i,0,i,MyGdxGame.HEIGHT);
        }
        for(int i =0;i<MyGdxGame.HEIGHT;i+=120){
            shapeRenderer.line(0,i,MyGdxGame.WIDTH,i);
        }
        shapeRenderer.end();

        tiledMapRenderer.render();

        sb.begin();
        for(int i =0;i<elements.size();i++){
            if(!(elements.get(i) instanceof Arrow)){
                elements.get(i).render(sb);
            }
        }
        userArrow.render(sb);
        sb.end();
        barriers.render(sb);
        menuItemBatch.begin();
        retrySprite.draw(menuItemBatch);
        menuSprite.draw(menuItemBatch);
        menuItemBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        barriers.getRenderer().setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
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
        if(dt==0)
            return;
        tiledMapRenderer.setView(camera);
        if(barriers.checkCollision(userArrow.getSprite())){
            Gdx.app.log("GG","EZ");
            game.setScreen(new GameState(game,this.levelIndex));
            return;
        }
        ActiveCircle activeCircle = checkAttach();
        if(activeCircle != null){
            if(activeCircle instanceof EndCircle){
                nextLevel();
                return;
            }
            userArrow.attach(activeCircle);
            activeCircle.attach(userArrow);
        }

        for(int i =0;i<elements.size();i++){
            elements.get(i).update(dt);
        }
    }

    Vector3 touchPoint = new Vector3();
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(!paused){
            if(lastTouchedDownSprite == retrySprite){
                camera.unproject(touchPoint.set(screenX,screenY,0));
                if(retrySprite.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
                    game.setScreen(new GameState(game,this.levelIndex));
                    return false;
                }else{
                    lastTouchedDownSprite = null;
                }
            }
        }

        return false;
    }
    private Sprite lastTouchedDownSprite = null;
    private long lastDetachTime =0;
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if(!paused){
            camera.unproject(touchPoint.set(screenX,screenY,0));
            if(retrySprite.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
                lastTouchedDownSprite = retrySprite;
                return false;
            }

            ActiveCircle activeCircle = userArrow.getAttached();
            if(activeCircle!=null){
                activeCircle.detach();
            }
            userArrow.detach();
            lastDetachTime = System.currentTimeMillis();
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
                int type = 0;
                if(object.getProperties().get("type")!= null){
                    type = Integer.parseInt(String.valueOf(object.getProperties().get("type")));
                }

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
            Star star = new Star(((RectangleMapObject)object).getRectangle());
            elements.add(star);
            //Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
            //barriers.addRectangle(rectangle);
        }
        //engelleri olustur
        for(MapObject object : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
            barriers.addRectangle(rectangle);

        }
        float barrierThreshold = -15;
        barriers.addRectangle(new Rectangle(barrierThreshold,0,1,MyGdxGame.HEIGHT));
        barriers.addRectangle(new Rectangle(MyGdxGame.WIDTH-barrierThreshold,0,1,MyGdxGame.HEIGHT));
        barriers.addRectangle(new Rectangle(0,barrierThreshold,MyGdxGame.WIDTH,1));
        barriers.addRectangle(new Rectangle(0,MyGdxGame.HEIGHT-barrierThreshold,MyGdxGame.WIDTH,1));
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
    public void nextLevel(){
        game.setScreen(new GameState(game,this.levelIndex+1));
    }
    @Override
    public void dispose() {

    }
}
