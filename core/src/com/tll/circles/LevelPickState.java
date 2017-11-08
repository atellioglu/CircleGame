package com.tll.circles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by abdullahtellioglu on 4.11.2017.
 */

public class LevelPickState extends InputAdapter implements Screen {
    private static final int LEVEL_COUNT = 50;
    private static final int HORIZONTAL_BUTTON_COUNT = 4;
    private static final int BUTTON_SIZE = 100;
    private MyGdxGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private BitmapFont font;
    private ArrayList<LevelSprite> levelSpriteArrayList;

    private TmxMapLoader loader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private List<Element> elements;
    private Barrier barriers;

    public LevelPickState(MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        levelSpriteArrayList =  new ArrayList<>();
        viewport = new ExtendViewport(MyGdxGame.WIDTH,MyGdxGame.HEIGHT,camera);
        font = AssetManager.raviaBlue32;
        for(int i =0;i<LEVEL_COUNT;i++){
            LevelSprite levelSprite =new LevelSprite(i+1);
            levelSprite.sprite.setPosition(i % HORIZONTAL_BUTTON_COUNT * 120 + 15 ,MyGdxGame.HEIGHT  - 250 -  ( (i / HORIZONTAL_BUTTON_COUNT )  * 120));
            levelSprite.sprite.setSize(BUTTON_SIZE-10,BUTTON_SIZE-10);
            levelSpriteArrayList.add(levelSprite);
        }
        Gdx.input.setInputProcessor(this);

        loader = new TmxMapLoader();

        elements = new ArrayList<>();
        barriers = new Barrier();
        game.getAdListener().showAd(0);
    }
    @Override
    public void show() {

    }
    SpriteBatch backgroundBatch = new SpriteBatch();
    @Override
    public void render(float delta) {
        update(delta);
        SpriteBatch sb = game.batch;
        Gdx.gl.glClearColor(46f/255,46f/255,46f/255,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        backgroundBatch.begin();
        backgroundBatch.draw(AssetManager.backgroundMenu,0,0,MyGdxGame.WIDTH,MyGdxGame.HEIGHT);
        backgroundBatch.end();
        if(tiledMapRenderer!=null){
            tiledMapRenderer.render();
        }
        sb.begin();
        for(int i =0;i<elements.size();i++){
            elements.get(i).render(sb);
        }
        for(int i =0;i<levelSpriteArrayList.size();i++){
            levelSpriteArrayList.get(i).render(sb);
        }
        font.draw(sb,"Choose Level",0,MyGdxGame.HEIGHT - 75,MyGdxGame.WIDTH,Align.center,true);
        sb.end();

    }
    private void update(float dt){

    }

    Vector3 touchDownPoint = new Vector3();
    Vector3 touchPoint = new Vector3();
    private LevelSprite touchedDownLevelSprite;
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        camera.unproject(touchDownPoint.set(screenX,screenY,0));
        int i =0;
        while(i<levelSpriteArrayList.size() && !levelSpriteArrayList.get(i).sprite.getBoundingRectangle().contains(touchDownPoint.x,touchDownPoint.y)){
            i++;
        }
        if(i != levelSpriteArrayList.size()){
            if(levelSpriteArrayList.get(i).available){
                touchedDownLevelSprite = levelSpriteArrayList.get(i);
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        camera.unproject(touchPoint.set(screenX,screenY,0));
        if(touchedDownLevelSprite !=null && touchedDownLevelSprite.sprite.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
            if(touchedDownLevelSprite.selected){
                this.game.setScreen(new GameState(this.game,touchedDownLevelSprite.level));
            }else{
                for(int i =0;i<levelSpriteArrayList.size();i++){
                    levelSpriteArrayList.get(i).selected = false;
                    levelSpriteArrayList.get(i).alpha = 0.05f;
                }
                touchedDownLevelSprite.selected = true;
                touchedDownLevelSprite.alpha = 1f;
                tiledMap = loader.load(String.format(Locale.ENGLISH,"levels/level%d.tmx",touchedDownLevelSprite.level));
                tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
                elements.clear();
                loadMap();
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        camera.unproject(touchDownPoint.set(screenX,screenY,0));
        //en sonuncu

        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
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

    private class LevelSprite{
        private Sprite sprite;
        private int level;
        private float alpha = 1f;
        private boolean available;
        private boolean selected;
        public LevelSprite(int level){
            this.level = level;
            int currentLevel = 8;//PreferenceHandler.getCurrentLevel();
            if(level > currentLevel){
                available = false;
            }else{
                available = true;
            }
            if(available){
                sprite = new Sprite(AssetManager.circles[0]);
            }else{
                sprite = new Sprite(AssetManager.circles[1]);
            }
            defaultFontColor = new Color(font.getColor());
        }
        private Color defaultFontColor;
        public void render(SpriteBatch sb){
            sprite.setAlpha(alpha);
            sprite.draw(sb);
            if(!available){
                font.setColor(Color.GRAY);
            }

            String txt ;
            if(!selected){
                txt = String.valueOf(level);
            }else{
                txt = ">";
            }
            if(alpha!=1){
                font.getColor().a = 0.2f;
            }
            font.draw(sb,txt,sprite.getX(),sprite.getY() + sprite.getHeight() * 2 / 3 ,sprite.getWidth(), Align.center,true);
            font.setColor(defaultFontColor);
        }
    }
    private void loadMap(){
        //cemberleri olustur
        float alpha = 0.2f;
        for(MapObject object : tiledMap.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
            ActiveCircle activeCircle = null;
            if(object.getName()!=null && object.getName().equals("start")){
                activeCircle = new SafeActiveCircle(AssetManager.circles[0],new Size((int)rectangle.getWidth(),(int)rectangle.getHeight()),new Vector3(rectangle.x,rectangle.y,0));
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
                        break;
                    case 2:
                        activeCircle = new FadeActiveCircle(selectedTypeTexture,new Size((int)rectangle.getWidth(),(int)rectangle.getHeight()),new Vector3(rectangle.x,rectangle.y,0));
                        break;
                }
            }
            if(object.getProperties().get("angle")!=null){
                float angle = Float.parseFloat(String.valueOf(object.getProperties().get("angle")));
            }
            activeCircle.getSprite().setAlpha(alpha);
            elements.add(activeCircle);
        }
        //yildizlari olustur
        for(MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Star star = new Star(((RectangleMapObject)object).getRectangle(),1);
            star.getSprite().setAlpha(alpha);
            elements.add(star);
        }
        //engelleri olustur
        barriers.setAlpha(alpha);
        float barrierThreshold = -15;
        barriers.addRectangle(new Rectangle(barrierThreshold,0,1,MyGdxGame.HEIGHT));
        barriers.addRectangle(new Rectangle(MyGdxGame.WIDTH-barrierThreshold,0,1,MyGdxGame.HEIGHT));
        barriers.addRectangle(new Rectangle(0,barrierThreshold,MyGdxGame.WIDTH,1));
        barriers.addRectangle(new Rectangle(0,MyGdxGame.HEIGHT-barrierThreshold,MyGdxGame.WIDTH,1));
    }
}
