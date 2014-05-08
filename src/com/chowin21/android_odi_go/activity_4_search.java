package com.chowin21.android_odi_go;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class activity_4_search extends Activity {
	ExampleHandler myExampleHandler = new ExampleHandler();
	private static final String LOG_TAG = "OdigoSearch";
	BackgroundTask task;
	TextView mTextView;
	EditText et;
	
	// 리스트뷰 선언
	private ListView listview;

	// 데이터를 연결할 Adapter
	DataAdapter adapter;

	// 데이터를 담을 자료구조
	ArrayList<CData> alist;

	
	class BackgroundTask extends AsyncTask<String, Void, Void> {
		InputSource is;
	    protected Void doInBackground(String... urls) {
	    	
	        try {
	            URL url= new URL(urls[0]);
	            is = new InputSource(url.openStream());
	            SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
	
	
				XMLReader xr = sp.getXMLReader();
				xr.setContentHandler(myExampleHandler);
				xr.parse(is);
	        } catch (Exception e) {
	        	Log.e(LOG_TAG, "doInBackground Error", e);
	        }
			return null;
	    }
	   
	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			
			int aa = myExampleHandler.getParsedData().size();
			String im_nameSearched = "";	//상품명
			int im_priceSearched = 0;		//상품가격
			double distSearched = 0.0;		//현재 위치에서 몇 km 떨어져있는지 
			double lon = 0.0;
			double lat = 0.0;
			
			for (int i = 0; i < aa; i++) {
				im_nameSearched = myExampleHandler.getParsedData().elementAt(i).getExtractedString(13);
				im_priceSearched = myExampleHandler.getParsedData().elementAt(i).getExtractedInt(14);
				distSearched = myExampleHandler.getParsedData().elementAt(i).getDist();
				lon = myExampleHandler.getParsedData().elementAt(i).getLon();
				lat = myExampleHandler.getParsedData().elementAt(i).getLat();
				
				distSearched = Math.round(distSearched * 10.0) / 10.0;
				
				adapter.add(new CData(getApplicationContext(), 
						im_nameSearched + " " + im_priceSearched + "원"	+ " +" + distSearched + "km",
						myExampleHandler.getParsedData().elementAt(i).getExtractedString(1), lon, lat));
			}
		}
	
	
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			adapter.clear();
			myExampleHandler.clearParsedData();
		}
	}
	
	public void sendShInfo(String sh_id, double lon, double lat) {
		((Activity_MainActivity)this.getParent()).showSpecificShOnMap(sh_id, lon, lat);
	}
	
	void listLowestSh(String im_name) {
		double lon = activity_1_map.lon;
		double lat = activity_1_map.lat;
		try {
			im_name = URLEncoder.encode(im_name, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url = "http://54.238.131.230/db_search_im.php?lon=" + lon + "&lat=" + lat + "&im_name=" + im_name;
		
		task = new BackgroundTask();
		task.execute(url);
	}
	
	void hideKeyboard(EditText et) {
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);   
		  
		//키보드를 띄운다.   
		//imm.showSoftInput(et, 0);   
		  
		//키보드를 없앤다.   
		imm.hideSoftInputFromWindow(et.getWindowToken(),0);
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 회전막기
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_4_search);

		mTextView = (TextView) findViewById(R.id.textView1);
		
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
				hideKeyboard(et);
				sendShInfo(data.getShId(), data.getLon(), data.getLat());

				//Toast.makeText(getApplicationContext(), "Selected : " + data.getLabel() +", " + data.getShId(), 2000).show();
			}
			
		});
		
		
		et = (EditText) findViewById(R.id.editText1);
		TextWatcher watcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
//					
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String im_name = s.toString();//검색할 단어
				if(im_name.length() > 0) {
					listLowestSh(im_name);
				}
			}
		
		};
		
		et.addTextChangedListener(watcher);
		
		//Begin X button for delete search text line
		
		Button btn_x = (Button) findViewById(R.id.activitiy_4_btn_x);
		btn_x.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//activity_4_search a_btn5_x = activity_4_search();
				
				EditText delete = (EditText) findViewById(R.id.editText1);
				delete.setText("");
			}
		});
		
		//End X button for delete search text line
		
	} // onCreate
	
		
	private class DataAdapter extends ArrayAdapter<CData> {
		// 레이아웃 XML을 읽어들이기 위한 객체
		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<CData> object) {

			// 상위 클래스의 초기화 과정
			// context, 0, 자료구조
			super(context, 0, object);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
				// 화면 출력
				TextView tv = (TextView) view.findViewById(R.id.textView1);
				tv.setText(data.getLabel());
				tv.setTextColor(Color.WHITE);
			}

			return view;

		}

	}

	// CData안에 받은 값을 직접 할당

	class CData {

		private String m_szLabel; 
		private String m_szShId;
		private double lon;
		private double lat;

		public CData(Context context, String p_szLabel, String p_szShId, double p_lon, double p_lat) {
			m_szLabel = p_szLabel;
			m_szShId = p_szShId;
			lon = p_lon;
			lat = p_lat;
		}

		public String getLabel() {
			return m_szLabel;
		}

		public String getShId() {
			return m_szShId;
		}
		
		public double getLon() {
			return lon;
		}
		
		public double getLat() {
			return lat;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(Activity_MainActivity.selectedMenu != null) {
			et.setText(Activity_MainActivity.selectedMenu);
			listLowestSh(Activity_MainActivity.selectedMenu);
			Activity_MainActivity.selectedMenu = null;
		}
		
		mTextView.setText(activity_1_map.cur_addr);
	}
}
