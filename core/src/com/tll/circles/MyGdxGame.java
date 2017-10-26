package com.tll.circles;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
		for(int i =0;i<AssetManager.circles.length;i++){
			AssetManager.circles[i].setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
		}
		AssetManager.star.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
		setScreen(new GameState(this,29));
		//setScreen(new GameState(this,PreferenceHandler.getCurrentLevel()));
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
