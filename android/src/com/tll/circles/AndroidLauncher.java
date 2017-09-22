package com.tll.circles;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tll.circles.MyGdxGame;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class AndroidLauncher extends AndroidApplication implements AdListener{
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		/*RelativeLayout relativeLayout = new RelativeLayout(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		MyGdxGame myGdxGame = new MyGdxGame();
		myGdxGame.setAdListener(this);
		View gameView = initializeForView(new MyGdxGame(),config);
		relativeLayout.addView(gameView);*/
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGdxGame(),config);

	}

	@Override
	public void showAd(int type) {
		if(type == 0){
			//banner
		}else if(type == 1){
			//inte
		}
	}

	@Override
	public void hideAd() {

	}
}
