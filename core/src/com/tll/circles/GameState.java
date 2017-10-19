package com.tll.circles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tll.circles.elements.ActiveCircle;
import com.tll.circles.elements.Arrow;
import com.tll.circles.elements.Barrier;
import com.tll.circles.elements.Element;
import com.tll.circles.elements.EndCircle;
import com.tll.circles.elements.FadeActiveCircle;
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
    private int collectedStars = 0;
    private TmxMapLoader loader;

    private TiledMap tiledMap;
    //test icin
    private ShapeRenderer shapeRenderer;
    private SpriteBatch menuItemBatch = new SpriteBatch();
    // TODO: 18/09/17 Arka planda hata var tum ekrani kaplamiyor!!
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private MyGdxGame game;
    private boolean showFailDialog = false,showSuccessDialog = false;
    private Theme theme;
    public GameState(MyGdxGame game,int levelIndex){
        this.game = game;
        theme = ThemeFactory.getInstance().getTheme();
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
        userArrow.setShapeRenderer(shapeRenderer);
        userArrow.setAnimationListener(new Arrow.AnimationListener() {
            @Override
            public void onAnimationStart(boolean success) {

            }

            @Override
            public void onAnimationFinish(boolean success) {
                if(success){
                    showSuccessDialog = true;
                }else{
                    showFailDialog = true;
                }
            }
        });
        Gdx.input.setInputProcessor(this);
        createHud();
        PreferenceHandler.saveCurrentLevel(levelIndex);
    }
    @Override
    public void render(float delta) {
        update(delta);
        SpriteBatch sb = game.batch;
        Color bg = ThemeFactory.getInstance().getTheme().backgroundColor;
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Color line = ThemeFactory.getInstance().getTheme().lineColor;
        shapeRenderer.setColor(line.r,line.g,line.b,line.a);
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
        renderHud();
    }


    private ActiveCircle checkAttach(){
        if(userArrow.isAttached()){
            return null;
        }
        for(int i =0;i<elements.size();i++){
            if(elements.get(i).hasInteraction() && elements.get(i) instanceof ActiveCircle){
                ActiveCircle activeCircle = (ActiveCircle)elements.get(i);
                if(userArrow.getLastAttachedCircle()!=null && activeCircle != userArrow.getLastAttachedCircle()){
                    boolean collided = Util.isCollided(activeCircle.getSprite(),userArrow.getSprite());
                    if(collided)
                        return activeCircle;
                }
            }
        }
        return null;
    }
    public void update(float dt) {
        if(dt==0)
            return;
        if(userArrow.isDead()){
            return;
        }
        tiledMapRenderer.setView(camera);
        if(barriers.checkCollision(userArrow.getSprite())){
            userArrow.die();
            userArrow.update(dt);
            return;
        }

        for(int i =0;i<elements.size();i++){
            elements.get(i).update(dt);
        }

        ActiveCircle activeCircle = checkAttach();
        if(activeCircle != null){
            if(activeCircle instanceof EndCircle){
                PreferenceHandler.saveInt(PreferenceHandler.STAR_KEY,PreferenceHandler.getInt(PreferenceHandler.STAR_KEY,0) + collectedStars);
                nextLevel();
                return;
            }
            userArrow.attach(activeCircle);
            activeCircle.attach(userArrow);
        }

    }

    Vector3 touchPoint = new Vector3();
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        camera.unproject(touchPoint.set(screenX,screenY,0));
        if(!paused){
            if(lastTouchedDownSprite == retrySprite){
                if(retrySprite.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
                    game.setScreen(new GameState(game,this.levelIndex));
                    return false;
                }else{
                    lastTouchedDownSprite = null;
                }
            }
            if(lastTouchedDownSprite == menuSprite){
                if(menuSprite.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
                    showMenu();
                }
            }
        }

        return false;
    }
    private Sprite lastTouchedDownSprite = null;
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(!paused){
            camera.unproject(touchPoint.set(screenX,screenY,0));
            if(retrySprite.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
                lastTouchedDownSprite = retrySprite;
                return false;
            }
            if(menuSprite.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
                lastTouchedDownSprite = menuSprite;
                return false;
            }
            ActiveCircle activeCircle = userArrow.getAttached();
            if(activeCircle!=null){
                activeCircle.detach();
            }
            userArrow.detach();
        }
        return false;
    }
    private void showMenu(){
        game.setScreen(new MenuState(game));
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
                        activeCircle.setTimeOutListener(new ActiveCircle.TimeoutListener() {
                            @Override
                            public void onTimeout() {
                                userArrow.die();
                                //game.setScreen(new GameState(game,GameState.this.levelIndex));
                            }
                        });
                        break;
                    case 2:
                        activeCircle = new FadeActiveCircle(new Size((int)rectangle.getWidth(),(int)rectangle.getHeight()),new Vector3(rectangle.x,rectangle.y,0));
                        FadeActiveCircle fadeActiveCircle = (FadeActiveCircle)activeCircle;
                        fadeActiveCircle.setFadeTime(Float.parseFloat(String.valueOf(object.getProperties().get("fadeTime"))));
                        fadeActiveCircle.setWaitTime(Float.parseFloat(String.valueOf(object.getProperties().get("waitTime"))));
                        activeCircle.setTimeOutListener(new ActiveCircle.TimeoutListener() {
                            @Override
                            public void onTimeout() {
                                userArrow.die();
                            }
                        });
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
            Star star = new Star(((RectangleMapObject)object).getRectangle(),1,userArrow);
            star.setStarHitListener(new Star.StarHitListener() {
                @Override
                public void onHit(int value) {
                    collectedStars += value;
                    Gdx.app.log("CollectedStars",String.valueOf(collectedStars));
                }
            });
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

    public void nextLevel(){
        game.setScreen(new GameState(game,this.levelIndex+1));
    }
    @Override
    public void dispose() {

    }
    //HUD
    private Sprite retrySprite;
    private Sprite menuSprite;
    private Sprite levelTextSprite;
    private Sprite circleSpriteBelowLevelText;

    private void createHud(){
        retrySprite = new Sprite(theme.retry);
        retrySprite.setSize(MENU_ITEM_SIZE,MENU_ITEM_SIZE);
        retrySprite.setPosition(MyGdxGame.WIDTH-MENU_ITEM_SIZE-MENU_ITEM_SIZE_MARGIN,MENU_ITEM_SIZE_MARGIN);
        retrySprite.setOriginCenter();
        menuSprite = new Sprite(theme.menu);
        menuSprite.setSize(MENU_ITEM_SIZE,MENU_ITEM_SIZE);
        menuSprite.setPosition(MENU_ITEM_SIZE_MARGIN,MENU_ITEM_SIZE_MARGIN);
        menuSprite.setOriginCenter();
        levelTextSprite = new Sprite(theme.level);
        levelTextSprite.setSize(75,15);
        levelTextSprite.setPosition(MENU_ITEM_SIZE_MARGIN,MyGdxGame.HEIGHT - levelTextSprite.getHeight() - MENU_ITEM_SIZE_MARGIN);
        circleSpriteBelowLevelText = new Sprite(theme.circle);
        circleSpriteBelowLevelText.setSize(75,75);
        circleSpriteBelowLevelText.setPosition(levelTextSprite.getX(),levelTextSprite.getY() - circleSpriteBelowLevelText.getHeight() - MENU_ITEM_SIZE_MARGIN);
    }

    private void renderHud(){
        menuItemBatch.setProjectionMatrix(camera.combined);
        menuItemBatch.begin();
        retrySprite.draw(menuItemBatch);
        menuSprite.draw(menuItemBatch);
        levelTextSprite.draw(menuItemBatch);
        circleSpriteBelowLevelText.draw(menuItemBatch);
        BitmapFont font = AssetManager.raviaBlue32;
        font.draw(menuItemBatch,String.valueOf(this.levelIndex),circleSpriteBelowLevelText.getX()+circleSpriteBelowLevelText.getWidth()/2-16,circleSpriteBelowLevelText.getY()+circleSpriteBelowLevelText.getHeight()-20);
        menuItemBatch.end();
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
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        barriers.getRenderer().setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
    }

    @Override
    public void show() {

    }
}
