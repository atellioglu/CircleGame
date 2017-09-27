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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by burhanboz on 12/07/2017.
 */
public class MenuState extends InputAdapter implements Screen {
    private static final int MARGIN_TOP = 25;
    private static final int TEXT_WIDTH = 180;
    private static final int ICON_MARGIN_LEFT = 20;
    private static final int RECTANGLE_HEIGHT = 30;
    private static final int RECTANGLE_WIDTH = 280;
    private static final int RECTANGLE_LEFT_SPACE = 130;
    private MyGdxGame game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Rectangle startRect,levelRect,soundRect,themeRect;
    public MenuState(MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new ScalingViewport(Scaling.fillX,MyGdxGame.WIDTH,MyGdxGame.HEIGHT,camera);
        Gdx.input.setInputProcessor(this);
        startRect = new Rectangle(RECTANGLE_LEFT_SPACE,450,RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
        levelRect = new Rectangle(RECTANGLE_LEFT_SPACE,startRect.y-startRect.height-MARGIN_TOP,RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
        soundRect = new Rectangle(RECTANGLE_LEFT_SPACE,levelRect.y-levelRect.height-MARGIN_TOP,RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
        themeRect = new Rectangle(RECTANGLE_LEFT_SPACE,soundRect.y-soundRect.height-MARGIN_TOP,RECTANGLE_WIDTH,RECTANGLE_HEIGHT);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Theme theme = ThemeFactory.getInstance().getTheme();
        SpriteBatch sb = game.batch;
        Color bg = theme.menuBackgroundColor;
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(theme.menuIcon,92,634,289,111);
        sb.draw(theme.menuCircle,0,123,480,480);

        sb.draw(theme.menuStartText,startRect.x,startRect.y,TEXT_WIDTH,startRect.height);
        sb.draw(AssetManager.menuStartIcon,startRect.x + TEXT_WIDTH + ICON_MARGIN_LEFT ,startRect.y,startRect.height,startRect.height);

        sb.draw(theme.menuLevelText,levelRect.x,levelRect.y,TEXT_WIDTH,levelRect.height);
        sb.draw(AssetManager.menuLevelIcon,levelRect.x + TEXT_WIDTH + ICON_MARGIN_LEFT ,levelRect.y,levelRect.height,levelRect.height);

        sb.draw(theme.menuVoiceText,soundRect.x,soundRect.y,TEXT_WIDTH,soundRect.height);
        sb.draw(AssetManager.menuSoundOnIcon,soundRect.x + TEXT_WIDTH + ICON_MARGIN_LEFT ,soundRect.y,soundRect.height,soundRect.height);

        sb.draw(theme.menuThemeText,themeRect.x,themeRect.y,TEXT_WIDTH,themeRect.height);
        sb.draw(theme.menuThemeSwitch,themeRect.x + TEXT_WIDTH + ICON_MARGIN_LEFT ,themeRect.y+themeRect.height/4,themeRect.height,themeRect.height/2);



        sb.end();
    }
    private void update(float dt){

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

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        camera.unproject(touchPoint.set(screenX,screenY,0));
        if(startRect.contains(touchPoint.x,touchPoint.y)){
            game.setScreen(new GameState(game,PreferenceHandler.getCurrentLevel()));
        }else if(levelRect.contains(touchPoint.x,touchPoint.y)){

        }else if(soundRect.contains(touchPoint.x,touchPoint.y)){

        }else if(themeRect.contains(touchPoint.x,touchPoint.y)){

        }
        return false;
    }

}
