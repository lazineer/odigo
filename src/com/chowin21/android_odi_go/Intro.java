package com.chowin21.android_odi_go;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

public class Intro extends Activity {

	private Handler h;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_0_intro);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		h = new Handler();
		h.postDelayed(irun, 1500); // 인트로 딜레이
		
		
	} // oncreate

	Runnable irun = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent i = new Intent(Intro.this, Activity_MainActivity.class);
			//Main.putExtra("userurl", url);
			
			startActivity(i);
			finish();

			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);

		}// run
	};

	public void onBackPressed() { // 인트로중뒤로가기 눌렀을때 방지
		super.onBackPressed();
		h.removeCallbacks(irun);

	}// onback

}// Intro
