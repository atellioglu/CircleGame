package com.tll.circles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tll.circles.dialog.Dialog;
import com.tll.circles.dialog.EndGameDialog;
import com.tll.circles.dialog.PauseDialog;
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
import java.util.Locale;

/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public class GameState extends InputAdapter implements Screen {
    private static final int MENU_ITEM_SIZE =48;
    private static final int MENU_ITEM_SIZE_MARGIN = 15;
    private List<Element> elements;
    private Barrier barriers;
    private Arrow userArrow;
    private boolean paused = false;
    private int levelIndex;
    private OrthographicCamera camera;
    private Viewport viewport;
    private int collectedStars = 0;
    private TmxMapLoader loader;
    private TiledMap tiledMap;
    //test icin
    private ShapeRenderer shapeRenderer;
    private SpriteBatch menuItemBatch = new SpriteBatch();
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private MyGdxGame game;
    private State state;
    private Dialog dialog;
    private static Skill mSkill = Skill.NONE;
    private SpriteBatch backgroundBatch;
    public static Skill getSkill(){
        return mSkill;
    }
    public GameState(MyGdxGame game,int levelIndex){
        this(game,levelIndex,Skill.NONE);
    }
    public GameState(MyGdxGame game,int levelIndex,Skill skill){
        mSkill = skill;
        this.game = game;
        this.levelIndex = levelIndex;
        //float aspectRatio = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(MyGdxGame.WIDTH,MyGdxGame.HEIGHT,camera);
        backgroundBatch = new SpriteBatch();
        loader = new TmxMapLoader();
        tiledMap = loader.load(String.format(Locale.ENGLISH,"levels/level%d.tmx",levelIndex));
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        elements = new ArrayList<>();
        barriers = new Barrier();
        loadMap();
        shapeRenderer = new ShapeRenderer();
        userArrow.setShapeRenderer(shapeRenderer);
        Gdx.input.setInputProcessor(this);
        createHud();
        PreferenceHandler.saveCurrentLevel(levelIndex);
        state = State.RUNNING;
        game.getAdListener().hideAd();
    }
    @Override
    public void render(float delta) {
        update(delta);
        SpriteBatch sb = game.batch;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(46f/255,46f/255,46f/255,1f);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT| GL20.GL_DEPTH_BUFFER_BIT |(Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        sb.setProjectionMatrix(camera.combined);
        backgroundBatch.begin();
        backgroundBatch.draw(AssetManager.background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        backgroundBatch.end();

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

        if(userArrow.getState() == Arrow.State.DEAD){
            dialog = new EndGameDialog();
            dialog.render(sb);
        }
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
        if(state == State.PAUSED){
            if(dialog !=null){
                dialog.update(dt);
            }
            //pause oldugunda oyunu durdur.
            return;
        }
        tiledMapRenderer.setView(camera);
        if(barriers.checkCollision(userArrow.getSprite())){
            userArrow.die();
            userArrow.update(dt);
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
        if(userArrow.getState() == Arrow.State.DEAD){
            //dialog cikart.

        }
    }

    Vector3 touchPoint = new Vector3();
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        camera.unproject(touchPoint.set(screenX,screenY,0));
        if(dialog!=null){
            dialog.touchUp(touchPoint.x,touchPoint.y);
        }
        if(!paused){
            if(lastTouchedDownSprite == retrySprite){
                if(retrySprite.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
                    game.setScreen(new GameState(game,this.levelIndex,mSkill));
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
        camera.unproject(touchPoint.set(screenX,screenY,0));
        if(dialog!=null){
            dialog.touchDown(touchPoint.x,touchPoint.y);
        }
        if(!paused){
            if(retrySprite.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
                lastTouchedDownSprite = retrySprite;
                return false;
            }
            if(userArrow.getState() != Arrow.State.DYING || userArrow.getState() != Arrow.State.DEAD){
                ActiveCircle activeCircle = userArrow.getAttached();
                if(activeCircle!=null){
                    activeCircle.detach();
                }
                userArrow.detach();
            }
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
                activeCircle = new SafeActiveCircle(AssetManager.circles[0],new Size((int)rectangle.getWidth(),(int)rectangle.getHeight()),new Vector3(rectangle.x,rectangle.y,0));
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
                Texture selectedTypeTexture = AssetManager.circles[type];
                switch (type){
                    case 0:
                        //safe olanlar
                        activeCircle = new SafeActiveCircle(selectedTypeTexture,new Size((int)rectangle.getWidth(),(int)rectangle.getHeight()),new Vector3(rectangle.x,rectangle.y,0));
                        break;
                    case 1:
                        //safe olmayanlar
                        activeCircle = new ActiveCircle(selectedTypeTexture,new Size((int)rectangle.getWidth(),(int)rectangle.getHeight()),new Vector3(rectangle.x,rectangle.y,0));
                        activeCircle.setTimeOutListener(new ActiveCircle.TimeoutListener() {
                            @Override
                            public void onTimeout() {
                                userArrow.die();
                                Gdx.app.log(GameState.class.getSimpleName(),"Arrow timeout");
                                //game.setScreen(new GameState(game,GameState.this.levelIndex));
                            }
                        });
                        try{
                            activeCircle.setTimeOut(Float.parseFloat(String.valueOf(object.getProperties().get("timeOut")))/mSkill.getValue());
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        break;
                    case 2:
                        activeCircle = new FadeActiveCircle(selectedTypeTexture,new Size((int)rectangle.getWidth(),(int)rectangle.getHeight()),new Vector3(rectangle.x,rectangle.y,0));
                        FadeActiveCircle fadeActiveCircle = (FadeActiveCircle)activeCircle;
                        fadeActiveCircle.setFadeTime(Float.parseFloat(String.valueOf(object.getProperties().get("fadeTime"))) / mSkill.getValue());
                        fadeActiveCircle.setVisibleTime(Float.parseFloat(String.valueOf(object.getProperties().get("visibleTime")))/ mSkill.getValue());
                        fadeActiveCircle.setInvisibleTime(Float.parseFloat(String.valueOf(object.getProperties().get("invisibleTime")))/ mSkill.getValue());
                        activeCircle.setTimeOutListener(new ActiveCircle.TimeoutListener() {
                            @Override
                            public void onTimeout() {
                                userArrow.die();
                                Gdx.app.log(GameState.class.getSimpleName(),"Arrow timeout");
                            }
                        });
                        break;
                }
            }
            if(object.getProperties().get("angle")!=null){
                float angle = Float.parseFloat(String.valueOf(object.getProperties().get("angle")));
                if(mSkill == Skill.SLOW_GAME){
                    activeCircle.setRotationAngleSpeed(angle * mSkill.getValue());
                }
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
        }
        //engelleri olustur
        if(mSkill != Skill.BARRIER_REMOVE){
            for(MapObject object : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
                barriers.addRectangle(rectangle);
            }
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
        retrySprite = new Sprite(AssetManager.retryIcon);
        retrySprite.setSize(MENU_ITEM_SIZE,MENU_ITEM_SIZE);
        retrySprite.setPosition(MyGdxGame.WIDTH-MENU_ITEM_SIZE-MENU_ITEM_SIZE_MARGIN,MENU_ITEM_SIZE_MARGIN);
        retrySprite.setOriginCenter();

        menuSprite = new Sprite(AssetManager.retryIcon);
        menuSprite.setSize(MENU_ITEM_SIZE,MENU_ITEM_SIZE);
        menuSprite.setPosition(MENU_ITEM_SIZE_MARGIN,MENU_ITEM_SIZE_MARGIN);
        menuSprite.setOriginCenter();
        levelTextSprite = new Sprite(AssetManager.levelText);
        levelTextSprite.setSize(75,15);
        levelTextSprite.setPosition(MENU_ITEM_SIZE_MARGIN,MyGdxGame.HEIGHT - levelTextSprite.getHeight() - MENU_ITEM_SIZE_MARGIN);
        circleSpriteBelowLevelText = new Sprite(AssetManager.retryIcon);
        circleSpriteBelowLevelText.setSize(75,75);
        circleSpriteBelowLevelText.setPosition(levelTextSprite.getX(),levelTextSprite.getY() - circleSpriteBelowLevelText.getHeight() - MENU_ITEM_SIZE_MARGIN);
    }

    private void renderHud(){
        menuItemBatch.setProjectionMatrix(camera.combined);
        menuItemBatch.begin();
        retrySprite.draw(menuItemBatch);
        //menuSprite.draw(menuItemBatch);
        levelTextSprite.draw(menuItemBatch);
        //circleSpriteBelowLevelText.draw(menuItemBatch);
        BitmapFont font = AssetManager.raviaBlue32;
        font.draw(menuItemBatch,String.valueOf(this.levelIndex),circleSpriteBelowLevelText.getX()+circleSpriteBelowLevelText.getWidth()/2-16,circleSpriteBelowLevelText.getY()+circleSpriteBelowLevelText.getHeight()-20);
        menuItemBatch.end();
    }



    @Override
    public void pause() {
        state = State.PAUSED;
        dialog = new PauseDialog();
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

    public enum State{
        RUNNING,
        PAUSED
    }
}
