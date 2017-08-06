package com.tll.circles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;

/**
 * Created by abdullahtellioglu on 06/08/17.
 */
public class AfterGameState extends InputAdapter implements Screen{
    private MyGdxGame game;
    public AfterGameState(MyGdxGame game){
        this.game = game;
    }
    @Override
    public void show() {
        if(game.getAdListener()!=null){
            int count = PreferenceHandler.getInt("dead_counter",0);
            if(count %3 == 0){
                game.getAdListener().showAd(1);
            }else{
                game.getAdListener().hideAd();
            }
            PreferenceHandler.saveInt("dead_counter",count+1);

        }

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return super.touchDown(screenX, screenY, pointer, button);
    }
}
