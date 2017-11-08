package com.tll.circles;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.tll.circles.MyGdxGame;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.BackgroundPriorityRunnable;

public class AndroidLauncher extends AndroidApplication implements AdListener{
	private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
	AdView adView;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		//add
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		RelativeLayout.LayoutParams params =new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		adView.setLayoutParams(params);
		adView.setAdUnitId(AD_UNIT_ID);
		adView.setVisibility(View.GONE);

		AdRequest.Builder builder = new AdRequest.Builder();

		//game
		RelativeLayout relativeLayout = new RelativeLayout(this);
		MyGdxGame game = new MyGdxGame();
		game.setAdListener(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.numSamples = 2;
		View gameView = initializeForView(game,config);
		gameView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		relativeLayout.addView(gameView);
		relativeLayout.addView(adView);

		adView.loadAd(builder.build());
		adView.setAdListener(new com.google.android.gms.ads.AdListener() {
			@Override
			public void onAdOpened() {
				Log.e("AndroidLauncher","Ad loaded");
			}
		});
		setContentView(relativeLayout);
	}

	@Override
	public void showAd(int type) {
		handler.post(new ShowAdImp(type));
	}
	private class ShowAdImp implements Runnable{
		int type;
		public ShowAdImp(int type){
			this.type = type;
		}
		@Override
		public void run() {

			if(type == 0){
				//banner
				adView.setVisibility(View.VISIBLE);
			}else if(type == 1){
				//inte
			}
		}
	}
	private final Handler handler = new Handler(Looper.getMainLooper());
	@Override
	public void hideAd() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(View.INVISIBLE);
			}
		});
	}
}
