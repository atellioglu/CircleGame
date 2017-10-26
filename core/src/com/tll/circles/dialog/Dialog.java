package com.tll.circles.dialog;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by abdullahtellioglu on 23.10.2017.
 */

public abstract class Dialog  {
    public abstract void render(SpriteBatch sb);
    public abstract void update(float dt);
    public void dispose(){

    }
    public abstract void touchDown(float x,float y);
    public abstract void touchUp(float x,float y);
}
