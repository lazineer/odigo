package com.chowin21.android_odi_go;

import java.net.URL;
import java.net.URLEncoder;

import org.xml.sax.InputSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class activity_3_input extends Activity {
	private static final String LOG_TAG = "OdigoInput";
	BackgroundTask task;
	ImageButton additional_btn;
	ImageButton imageButton3_page2_input_sumit;
	static final int DIALOG_PROGRESS1 = 6;
	static final int DIALOG_SUCCESS = 5;
	static final int DIALOG_FAIL = 4;
	boolean flag = false;
	EditText et1;
	EditText et2;
	EditText et3;
	EditText et4;
	TextView mTextView;
	
	class BackgroundTask extends AsyncTask<String, Void, Void> {
		InputSource is;
	    protected Void doInBackground(String... urls) {
	        try {
	            URL url= new URL(urls[0]);
	            is = new InputSource(url.openStream());
	        } catch (Exception e) {
	        	Log.e(LOG_TAG, "doInBackground Error", e);
	        }
			return null;
	    }
	   
	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			showDialog(DIALOG_SUCCESS);
			// begin 입력값 확인 // 
			et1.setHint("\"" + et1.getText().toString()+"\""+"추가됨");
			et1.setText("");
			et2.setHint("\"" + et2.getText().toString()+"\""+"추가됨");
			et2.setText("");
			et3.setHint("\"" + et3.getText().toString()+"\""+"추가됨");
			et3.setText("");
			et4.setHint("\"" + et4.getText().toString()+"\""+"추가됨");
			et4.setText("");
			
			additional_btn.setEnabled(true);
			imageButton3_page2_input_sumit.setEnabled(true);
			// end 입력값 확인 // 
		}
	
	
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_3_input);

		//Begin facebookLogin
		//if()
		//End facebookLogin
		
		
		mTextView = (TextView) findViewById(R.id.textView1);
		
		et1 = (EditText) findViewById(R.id.editText1_menu_name);
		et2 = (EditText) findViewById(R.id.editText2_price);
		et3 = (EditText) findViewById(R.id.editText3_shop_name);
		et4 = (EditText) findViewById(R.id.editText4_phone);
		
	
		additional_btn = (ImageButton) findViewById(R.id.imageButton2_additional_btn);
		additional_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Log.i("test", "input button");
				Intent p2i = new Intent(activity_3_input.this,
						activity_3_input_additional.class);
				//Log.i("test", "input button");
				startActivity(p2i);
				//Log.i("test", "input button");
			}
		}); // additional_btn

		imageButton3_page2_input_sumit = (ImageButton) findViewById(R.id.imageButton3_page2_input_sumit);
		imageButton3_page2_input_sumit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// begin data insert //
				
				String im_name = et1.getText().toString();
				String im_price = et2.getText().toString();
				String sh_name = et3.getText().toString();
				String sh_phone = et4.getText().toString();
				String sh_addr = activity_1_map.cur_addr;
				
				//Log.i("test", sh_addr);
				if(im_name.length() > 0 && im_price.length() > 0 && sh_name.length() > 0 && sh_phone.length() > 0){
					String[] tmp = sh_phone.split("-");
					//Log.i("test", ""+tmp.length);
					if(tmp.length == 3) {
						//현재 좌표를 넣어야 함. 요망.
						double lon = activity_1_map.lon;
						double lat = activity_1_map.lat;
							
						try {
							im_name = URLEncoder.encode(im_name, "utf-8");
							im_price = URLEncoder.encode(im_price, "utf-8");
							sh_name = URLEncoder.encode(sh_name, "utf-8");
							sh_phone = URLEncoder.encode(sh_phone, "utf-8");
							sh_addr = URLEncoder.encode(sh_addr, "utf-8");
							
							String url = "http://54.238.131.230/db_insert.php?im_name="+ im_name
									+ "&im_price=" + im_price + "&sh_name=" + sh_name
									+ "&lon=" + lon + "&lat=" + lat + "&sh_addr=" + sh_addr + "&sh_phone=" + sh_phone;
									
									task = new BackgroundTask();
									task.execute(url);
							
							additional_btn.setEnabled(false);
							imageButton3_page2_input_sumit.setEnabled(false);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}// catch 		
					}
					else {
						Toast.makeText(
								getApplicationContext(),
								"\"02-123-1234\"와 같은 형식으로 입력해주세요.", Toast.LENGTH_LONG)
								.show();
					}
				}
			}
		}); // additional_btn
	}// onCreate

	// begin input dialog for auto dismiss messagebox //
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder;
		switch (id) {

		case DIALOG_SUCCESS:
			builder = new AlertDialog.Builder(activity_3_input.this);
			builder.setTitle("자랑등록이 성공적으로 되었습니다!!!");
			builder.setMessage("당신의 노력덕에 많은 분들이 이익을 얻게 되었습니다.\n (신뢰도가 소폭 조정 되었습니다.)");
			builder.setPositiveButton("확인", null);
			//builder.show();

			dialog = builder.create(); // return에러안나게 하려면 ..
			break;

		case DIALOG_FAIL:
			builder = new AlertDialog.Builder(activity_3_input.this);
			builder.setTitle("값을 입력해주세요.");
			// builder.setMessage("당신의 노력덕에 많은 분들이 이익을 얻게 되었습니다.\n (신뢰도가 소폭 조정 되었습니다.)");
			builder.setPositiveButton("확인", null);
			//builder.show();

			dialog = builder.create(); // return에러안나게 하려면 ..
			break;

		case DIALOG_PROGRESS1:
			dialog = ProgressDialog.show(activity_3_input.this, "",
					"Loading, Please Wait..", true, true);
			break;
		default:
			dialog = null;
		}
		return dialog;

		// end input dialog for auto dismiss messagebox //
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mTextView.setText(activity_1_map.cur_addr);
	}
}// Activity class