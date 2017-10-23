package com.tll.circles.dialog;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tll.circles.GameState;

/**
 * Created by abdullahtellioglu on 23.10.2017.
 */

public class EndGameDialog extends Dialog {

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void update(float dt) {

    }
    private Sprite lastTouchedSprite;
    @Override
    public void touchDown(float x, float y) {

    }

    @Override
    public void touchUp(float x, float y) {
         /*if(lastTouchedDownSprite == retrySprite){
            if(retrySprite.getBoundingRectangle().contains(touchPoint.x,touchPoint.y)){
                game.setScreen(new GameState(game,this.levelIndex));
                return false;
            }else{
                lastTouchedDownSprite = null;
            }
        }*/
    }
}
