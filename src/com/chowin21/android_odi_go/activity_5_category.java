package com.chowin21.android_odi_go;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class activity_5_category extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 회전막기
		setContentView(R.layout.activity_5_category);

		ImageButton logo = (ImageButton) findViewById(R.id.logo);
		logo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(activity_5_category.this,
						activity_5_category_logo.class);
				startActivity(i);

			}
		});

	}
}
