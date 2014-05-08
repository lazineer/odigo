package com.chowin21.android_odi_go;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class activity_3_input_additional extends ImageSelectHelperActivity {

	ImageButton imageButton2_additional_submit_btn;
	ImageButton imagebutton_page2_input_additional_pic;

	// boolean flag=false;
	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Log.i("test", "1");
		setContentView(R.layout.activity_3_input_additional);
		Log.i("test", "2");
		imageButton2_additional_submit_btn = (ImageButton) findViewById(R.id.activity_3_input_addtional_completed_imgbtn);
		Log.i("test", "4");
		imageButton2_additional_submit_btn

		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				
				Intent intentHome = new Intent(
						activity_3_input_additional.this,
						Activity_MainActivity.class);
				intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intentHome.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intentHome);
				finish();
				// Intent p2i = new Intent(Page2_input_additional.this,
				// NMapViewer.class);
				// startActivity(p2i);
				Toast.makeText(activity_3_input_additional.this, "입력완료",
						Toast.LENGTH_LONG).show();

				// finish();
				// flag = true; // 홈으로 가기위함ㄴ

			}
		}); // additional_btn

		findViewById(R.id.activity_3_input_addtional_pic_imgbtn)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// setImageSizeBoundary(400); // optional. default is
						// 500.
						// setCropOption(1, 1); // optional. default is no crop.
						// setCustomButtons(btnGallery, btnCamera, btnCancel);
						// // you can set these buttons.
						startSelectImage();
					}
				});

		getSelectedImageFile(); // extract selected & saved image file.

	}// onCreate

	
}// Activity class

