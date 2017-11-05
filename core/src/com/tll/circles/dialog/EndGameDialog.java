package com.tll.circles.dialog;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tll.circles.MyGdxGame;

/**
 * Created by abdullahtellioglu on 23.10.2017.
 */

public class EndGameDialog extends Dialog {
    private final int BUTTON_SIZE = 48;
    private final int MARGIN = 10;
    private final String[] possibleText = {};
    private Sprite homeButton;
    private Sprite continueButton;
    private Sprite snailButton,noBarrierButton,lastCircleButton;
    private String headText;
    private Sprite[] sprites;
    public EndGameDialog(){


        int index = 0;
        sprites = new Sprite[5];
        sprites[index++] = homeButton;
        sprites[index++] = continueButton;
        sprites[index++] = lastCircleButton;
        sprites[index++] = snailButton;
        sprites[index++] = noBarrierButton;
    }
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

    }
}
