package com.tll.circles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by burhanboz on 12/07/2017.
 */
public class MenuState extends State {
    private Texture background;
    private Texture playBtn;

    public MenuState (GameStateManager gsm){
        super(gsm);
        background = new Texture("MenuBackground.png");
        playBtn = new Texture("PlayBtn.png");

    }
    @Override
    public void init() {

    }

    @Override
    public void update(float dt) {
        init();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        sb.draw(playBtn,(MyGdxGame.WIDTH/2) - (playBtn.getWidth()/2), (MyGdxGame.HEIGHT/2) - (playBtn.getHeight()/2));
        sb.end();

    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
    }
}
