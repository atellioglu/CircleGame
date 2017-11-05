package com.tll.circles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by abdullahtellioglu on 4.11.2017.
 */

public class LevelPickState extends InputAdapter implements Screen {
    private static final int LEVEL_COUNT = 50;
    private static final int HORIZONTAL_BUTTON_COUNT = 5;
    private static final int BUTTON_SIZE = 100;
    private MyGdxGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private BitmapFont font;
    private ArrayList<LevelSprite> levelSpriteArrayList;
    public LevelPickState(MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        levelSpriteArrayList =  new ArrayList<>();
        viewport = new ScalingViewport(Scaling.fillX,MyGdxGame.WIDTH,MyGdxGame.HEIGHT,camera);
        font = AssetManager.raviaBlue32;
        for(int i =0;i<LEVEL_COUNT;i++){
            LevelSprite levelSprite =new LevelSprite(i+1);
            levelSprite.sprite.setPosition(i % HORIZONTAL_BUTTON_COUNT * 95 ,MyGdxGame.HEIGHT  - 250 -  ( (i / HORIZONTAL_BUTTON_COUNT )  * 100));
            levelSprite.sprite.setSize(BUTTON_SIZE-10,BUTTON_SIZE-10);
            levelSpriteArrayList.add(levelSprite);
        }
        Gdx.input.setInputProcessor(this);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        SpriteBatch sb = game.batch;
        Gdx.gl.glClearColor(46f/255,46f/255,46f/255,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(AssetManager.backgroundMenu,0,0,MyGdxGame.WIDTH,MyGdxGame.HEIGHT);
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
                }
                touchedDownLevelSprite.selected = true;
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
                sprite = new Sprite(AssetManager.blueEmptyCircle);
            }else{
                sprite = new Sprite(AssetManager.grayEmptyCircle);
            }
            defaultFontColor = new Color(font.getColor());
        }
        private Color defaultFontColor;
        public void render(SpriteBatch sb){
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
            font.draw(sb,txt,sprite.getX(),sprite.getY() + sprite.getHeight() * 2 / 3 ,sprite.getWidth(), Align.center,true);
            font.setColor(defaultFontColor);
        }
    }
}
