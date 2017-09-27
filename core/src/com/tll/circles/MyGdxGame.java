package com.tll.circles;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final String TITLE = "Circle Game";
	private AdListener adListener;
	public void setAdListener(AdListener adListener){
		this.adListener = adListener;
	}
	public AdListener getAdListener(){
		return adListener;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		Gdx.input.setCatchBackKey(true);
		setScreen(new MenuState(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
	}
}
