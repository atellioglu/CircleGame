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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.nio.IntBuffer;

/**
 * Created by burhanboz on 12/07/2017.
 */
public class MenuState extends InputAdapter implements Screen {
    private MyGdxGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Sprite startGameButton,levelPickButton,soundButton;
    private Sprite starIcon;
    BitmapFont starTextFont;
    private String starText;
    public MenuState(MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new ExtendViewport(MyGdxGame.WIDTH,MyGdxGame.HEIGHT,camera);
        Gdx.input.setInputProcessor(this);
        startGameButton = new Sprite(AssetManager.menuStartButton);
        startGameButton.setSize(200,200);
        startGameButton.setPosition(MyGdxGame.WIDTH/2 - 100,MyGdxGame.HEIGHT/2 - 100);
        levelPickButton = new Sprite(AssetManager.menuLevelButton);
        levelPickButton.setSize(100,100);
        levelPickButton.setPosition(startGameButton.getX()+startGameButton.getWidth()-20,startGameButton.getY()-100);
        soundButton = new Sprite(AssetManager.menuSoundOnButton);
        soundButton.setSize(100,100);
        soundButton.setPosition(startGameButton.getX()-80,levelPickButton.getY());
        starIcon = new Sprite(AssetManager.menuStarIcon);
        starIcon.setSize(40,40);
        starIcon.setPosition(30,MyGdxGame.HEIGHT - starIcon.getHeight() - 30);
        starIcon.setAlpha(starAlpha);
        starText = String.valueOf(PreferenceHandler.getInt(PreferenceHandler.STAR_KEY,0));
        starTextFont = AssetManager.raviaBlue32;
        IntBuffer buf = BufferUtils.newIntBuffer(16);
        Gdx.gl.glGetIntegerv(GL20.GL_MAX_TEXTURE_SIZE, buf);
        int maxSize = buf.get(0);
        Gdx.app.log("Max Size ",String.valueOf(maxSize));
    }
    private boolean starFadeIn = true;
    private float starAlpha = 0f;
    private float alpha = 1;
    private float minAlpha = 0.25f;
    @Override
    public void show() {

    }
    private SpriteBatch backgroundBatch = new SpriteBatch();
    @Override
    public void render(float delta) {
        update(delta);
        SpriteBatch sb = game.batch;
        Gdx.gl.glClearColor(46f/255,46f/255,46f/255,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        backgroundBatch.begin();
        backgroundBatch.draw(AssetManager.backgroundMenu,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        backgroundBatch.end();
        /*
        sb.begin();
        startGameButton.draw(sb);
        levelPickButton.draw(sb);
        soundButton.draw(sb);
        starIcon.draw(sb);
        starTextFont.getData().setScale(0.50f);
        starTextFont.draw(sb,starText,starIcon.getX()+starIcon.getWidth()+10,starIcon.getY()+starIcon.getHeight()-15);
        starTextFont.getData().setScale(1f);
        sb.end();*/
    }
    private void update(float dt){
        if(touchedDownSprite !=null){
            alpha -= dt;
            if(alpha <= minAlpha){
                alpha = minAlpha;
            }
        }else{
            alpha = 1;
        }
        soundButton.setAlpha(alpha);
        startGameButton.setAlpha(alpha);
        levelPickButton.setAlpha(alpha);
        if(touchedDownSprite!=null){
            touchedDownSprite.setAlpha(1);
        }
        if(starFadeIn){
            starAlpha += dt;
            if(starAlpha >= 1){
                starAlpha = 1;
                starFadeIn = false;
            }
        }else{
            starAlpha -= dt;
            if(starAlpha <= 0){
                starAlpha = 0;
                starFadeIn = true;
            }
        }
        starIcon.setAlpha(starAlpha);
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

    Vector3 touchPoint = new Vector3();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        camera.unproject(touchPoint.set(screenX,screenY,0));
        if(startGameButton.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
           touchedDownSprite = startGameButton;
        }else if( levelPickButton.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
            touchedDownSprite = levelPickButton;
        }else if( soundButton.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
            touchedDownSprite = soundButton;
        }
        return false;
    }
    private Sprite touchedDownSprite;
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        camera.unproject(touchPoint.set(screenX,screenY,0));
        if(touchedDownSprite!=null && touchedDownSprite == startGameButton && startGameButton.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
            this.game.setScreen(new GameState(this.game,PreferenceHandler.getCurrentLevel()));
        }else if(touchedDownSprite != null && touchedDownSprite == levelPickButton && levelPickButton.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
            this.game.setScreen(new LevelPickState(this.game));
        }else if(touchedDownSprite !=null && touchedDownSprite == soundButton && soundButton.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
            if(MyGdxGame.SOUND){
                MyGdxGame.SOUND = false;
                soundButton.setTexture(AssetManager.menuSoundOffButton);
            }else{
                MyGdxGame.SOUND = true;
                soundButton.setTexture(AssetManager.menuSoundOnButton);
            }

        }
        touchedDownSprite = null;
        alpha = 1;
        return false;
    }

}
