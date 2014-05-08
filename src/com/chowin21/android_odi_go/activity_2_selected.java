package com.chowin21.android_odi_go;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class activity_2_selected extends Activity {

	// Begin selected textview 시간, 메뉴나오는곳

	TextView mTextView;
	int curYear, curMonth, curDay, curHour, curMinute, curNoon, curSecond;

	Calendar c;

	String tag = null;
	String noon = "";

	Date curMillis;

	TimerTask second;

	TextView getData;
	private final Handler handler = new Handler();

	// End selected textView 시간, 메뉴나오는곳
	// 리스트뷰 선언
	private ListView listview;

	// 데이터를 연결할 Adapter
	DataAdapter adapter;

	// 데이터를 담을 자료구조
	ArrayList<CData> alist;
	
	public void sendSelected(String selected) {
		((Activity_MainActivity)this.getParent()).showSearchTab(selected);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 회전막기
		setContentView(R.layout.activity_2_selected);

		// 선언한 리스트뷰에 사용할 리스뷰연결
		listview = (ListView) findViewById(R.id.listView1);

		// 객체를 생성합니다
		alist = new ArrayList<CData>();

		// 데이터를 받기위해 데이터어댑터 객체 선언
		adapter = new DataAdapter(this, alist);

		// 리스트뷰에 어댑터 연결
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				// TODO Auto-generated method stub
				CData data = adapter.getItem(position);
				sendSelected(data.getLabel());
				//Toast.makeText(getApplicationContext(), "Selected : " + data.getLabel() +", " + data.getShId(), 2000).show();
			}
			
		});

		// ArrayAdapter를 통해서 ArrayList에 자료 저장
		// 하나의 String값을 저장하던 것을 CData클래스의 객체를 저장하던것으로 변경
		// CData 객체는 생성자에 리스트 표시 텍스트뷰1의 내용과 텍스트뷰2의 내용 그리고 사진이미지값을 어댑터에 연결

		// CData클래스를 만들때의 순서대로 해당 인수값을 입력
		// 한줄 한줄이 리스트뷰에 뿌려질 한줄 한줄이라고 생각하면 됩니다.

		// Begin List Data
		//
		adapter.add(new CData(getApplicationContext(), "짜장면", "21.81%"));
		adapter.add(new CData(getApplicationContext(), "짬봉", "18.17%"));
		adapter.add(new CData(getApplicationContext(), "부대찌게", "17.05%"));
		adapter.add(new CData(getApplicationContext(), "김치찌게", "14.55%"));
		adapter.add(new CData(getApplicationContext(), "갈비탕", "10.11%"));
		adapter.add(new CData(getApplicationContext(), "제육볶음", "8.76%"));
		adapter.add(new CData(getApplicationContext(), "된장찌게", "5.46%"));
		adapter.add(new CData(getApplicationContext(), "보쌈", "5.01%"));
		adapter.add(new CData(getApplicationContext(), "주먹밥" ,"3.14%"));
		adapter.add(new CData(getApplicationContext(), "김밥", "1.98%"));
		// End List Data

		// Begin selected textview 시간, 메뉴나오는곳

		//getData = (TextView) findViewById(R.id.selected_textView1);
		selected_textview_clock();

		// End selected textview

	}
	
	private void selected_textview_clock() {
		getData = (TextView) findViewById(R.id.selected_textView1);

		second = new TimerTask() {
			@Override
			public void run() {
//				Log.d(tag, curYear + "." + curMonth + "." + curDay + "."
//						+ curHour + ":" + curMinute + "." + curSecond);
				Update();
			}
		};
		Timer timer = new Timer();
		timer.schedule(second, 0, 1000);

	}
	// Begin selected textview 시간, 메뉴나오는곳
	protected void Update() {
		c = Calendar.getInstance();
		// curMillis = c.getTime();
		curYear = c.get(Calendar.YEAR);
		curMonth = c.get(Calendar.MONTH) + 1;
		curDay = c.get(Calendar.DAY_OF_MONTH);
		curHour = c.get(Calendar.HOUR_OF_DAY);
		curNoon = c.get(Calendar.AM_PM);
		if (curNoon == 0) {
			noon = "오전";
		} else {
			noon = "오후";
			curHour -= 12;
		}
		curMinute = c.get(Calendar.MINUTE);
		curSecond = c.get(Calendar.SECOND);

		Runnable updater = new Runnable() {
			public void run() {
				getData.setText("현재시간 " + noon + curHour + "시 " + curMinute
						+ "분 " + Activity_MainActivity.selected);

			}
		};
		handler.post(updater);
	}

	// End selected textview 시간, 메뉴나오는곳
	private class DataAdapter extends ArrayAdapter<CData> {
		// 레이아웃 XML을 읽어들이기 위한 객체
		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<CData> object) {

			// 상위 클래스의 초기화 과정
			// context, 0, 자료구조
			super(context, 0, object);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		// 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구문
		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View view = null;

			// 현재 리스트의 하나의 항목에 보일 컨트롤 얻기

			if (v == null) {

				// XML 레이아웃을 직접 읽어서 리스트뷰에 넣음
				view = mInflater.inflate(R.layout.myitem, null);
			} else {

				view = v;
			}

			// 자료를 받는다.
			final CData data = this.getItem(position);

			if (data != null) {
				TextView tv = (TextView) view.findViewById(R.id.textView1);
				
				tv.setText(data.getLabel() + " " + data.getData());
				//Log.i("test",""+data.getLabel());
				tv.setTextColor(Color.WHITE);
			}

			return view;

		}

	}

	// CData안에 받은 값을 직접 할당

	class CData {

		private String m_szLabel;
		private String m_szData;

		public CData(Context context, String p_szLabel, String p_szDataFile) {

			m_szLabel = p_szLabel;

			m_szData = p_szDataFile;
		}

		public String getLabel() {
			return m_szLabel;
		}

		public String getData() {
			return m_szData;
		}
	}
}
