package com.tll.circles.dialog;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.tll.circles.AssetManager;
import com.tll.circles.MyGdxGame;

/**
 * Created by abdullahtellioglu on 23.10.2017.
 */

public class PauseDialog extends Dialog  {

    private Sprite homeButton;
    private Sprite continueButton;
    private Sprite snailButton,noBarrierButton,lastCircleButton;
    BitmapFont font;
    public PauseDialog(){
        font = AssetManager.raviaBlue48;
    }
    public void render(SpriteBatch sb){
        sb.begin();
        sb.draw(AssetManager.overlay,0,0, MyGdxGame.WIDTH,MyGdxGame.HEIGHT);
        font.draw(sb,"Press to start",0,MyGdxGame.WIDTH/2,MyGdxGame.WIDTH, Align.center,true);
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
