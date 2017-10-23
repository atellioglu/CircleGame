package com.tll.circles.dialog;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tll.circles.AssetManager;
import com.tll.circles.MyGdxGame;

/**
 * Created by abdullahtellioglu on 23.10.2017.
 */

public class PauseDialog extends Dialog  {

    public void render(SpriteBatch sb){
        sb.begin();
        sb.draw(AssetManager.overlay,0,0, MyGdxGame.WIDTH,MyGdxGame.HEIGHT);
        sb.end();
    }
    public void update(float dt){

    }

    @Override
    public void touchDown(float x, float y) {

    }

    @Override
    public void touchUp(float  x, float y) {

    }
}
