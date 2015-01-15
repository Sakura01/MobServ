package com.kawtar.mainUI;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.kawtar.myapplication.R;

public class SplashActivity extends Activity {

	// SpalshActivity screen timer

	private static int			SPLASH_TIME_OUT	= 3000;

	public static final boolean	CONDITION		= true;

	private Intent				mIntent;

	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_splash);

		new Handler().postDelayed(new Runnable() {


			@Override
			public void run() {

				mIntent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(mIntent);

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}
