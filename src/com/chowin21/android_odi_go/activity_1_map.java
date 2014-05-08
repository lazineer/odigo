/* 
 * NMapViewer.java $version 2010. 1. 1
 * 
 * Copyright 2010 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */

package com.chowin21.android_odi_go;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutCustomOverlay;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

/**
 * Sample class for map viewer library.
 * 
 * @author kyjkim
 */
public class activity_1_map extends NMapActivity {
	String sh_addr = null;
	String sh_name = null;
	String im_name1 = null;
	String im_name2 = null;
	int im_price1 = 0;
	int im_price2 = 0;
	ImageButton mCallButton;
	GPSListener gpsListener = null;
	String provider = null;
	LocationManager manager = null;
	public static String cur_addr = null;
	public static String sh_phone = null;
	TextView mTextView;
	BackgroundTask task;
	BgTaskforShPhone task_specific_sh;
	public static int mMode = 4;// 모든 카테고리
	// begin declare myExampleHandler//
	ExampleHandler myExampleHandler = new ExampleHandler();
	Geocoder gc;
	public static double lat = 37.5666091;
	public static double lon = 126.978371;
	boolean bFromSearch = false;
	public static activity_1_map map;
	// end declare myExampleHandler//

	private static final String LOG_TAG = "OdigoMain";
	private static final boolean DEBUG = false;

	// set your API key which is registered for NMapViewer library.
	private static final String API_KEY = "9d16009b388387bc59162abac74db9a6";

	private NMapView mMapView;
	private NMapController mMapController;

	private static final NGeoPoint NMAP_LOCATION_DEFAULT = new NGeoPoint(lon,
			lat);

	// begin 레벨이 낮아지면 더 세부적으로 보는건가 ?
	// private static final int NMAP_ZOOMLEVEL_DEFAULT = 11;
	private static final int NMAP_ZOOMLEVEL_DEFAULT = 10;
	// end 레벨이 낮아지면 더 세부적으로 보는건가 ?

	private static final int NMAP_VIEW_MODE_DEFAULT = NMapView.VIEW_MODE_VECTOR;
	private static final boolean NMAP_TRAFFIC_MODE_DEFAULT = false;
	private static final boolean NMAP_BICYCLE_MODE_DEFAULT = false;

	private static final String KEY_ZOOM_LEVEL = "NMapViewer.zoomLevel";
	private static final String KEY_CENTER_LONGITUDE = "NMapViewer.centerLongitudeE6";
	private static final String KEY_CENTER_LATITUDE = "NMapViewer.centerLatitudeE6";
	private static final String KEY_VIEW_MODE = "NMapViewer.viewMode";
	private static final String KEY_TRAFFIC_MODE = "NMapViewer.trafficMode";
	private static final String KEY_BICYCLE_MODE = "NMapViewer.bicycleMode";

	private SharedPreferences mPreferences;
	private NMapOverlayManager mOverlayManager;
	private NMapLocationManager mMapLocationManager;
	private NMapViewerResourceProvider mMapViewerResourceProvider;
	private NMapPOIdataOverlay mFloatingPOIdataOverlay;
	private NMapPOIitem mFloatingPOIitem;

	// begin
	private NMapPOIdataOverlay mPOIdataOverlay = null;

	// end

	private class GPSListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			lat = location.getLatitude();
			lon = location.getLongitude();

			mMapController.setMapCenter(new NGeoPoint(lon, lat));
			mMapController.animateTo(new NGeoPoint(lon, lat));
			findPlacemarkAtLocation(lon, lat);
			
			showMarker(lon, lat, 2, mMode);

			manager.removeUpdates(gpsListener);
			
			if (DEBUG) {
				String msg = "Latitude : " + lat + "\nLongitude:" + lon;
				Log.i("GPSListener", msg);

				// Toast.makeText(getApplicationContext(), msg,
				// Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "상태: 위치 서비스 이용 불가.",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "상태: 위치 서비스 이용 가능.",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				Toast.makeText(getApplicationContext(), "상태: 위치 서비스 범위 밖입니다.",
						Toast.LENGTH_LONG).show();
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Toast.makeText(getApplicationContext(),
						"상태: 일시적으로 위치 서비스 사용 불가능.", Toast.LENGTH_LONG).show();
				break;
			case LocationProvider.AVAILABLE:
				Toast.makeText(getApplicationContext(), "상태: 위치 서비스 이용 가능.",
						Toast.LENGTH_LONG).show();
				break;
			}

		}

	}

	class BackgroundTask extends AsyncTask<String, Void, Void> {
		InputSource is;

		protected Void doInBackground(String... urls) {

			try {
				URL url = new URL(urls[0]);
				is = new InputSource(url.openStream());
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();

				XMLReader xr = sp.getXMLReader();
				xr.setContentHandler(myExampleHandler);
				xr.parse(is);
			} catch (Exception e) {
				Log.e(LOG_TAG, "doInBackground Error", e);
				Toast.makeText(activity_1_map.this, e.toString(),
						Toast.LENGTH_LONG).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			showPOIdataOverlay();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			myExampleHandler.clearParsedData();
		}
	}

	class BgTaskforShPhone extends AsyncTask<String, Void, Void> {
		InputSource is;

		protected Void doInBackground(String... urls) {

			try {
				URL url = new URL(urls[0]);
				is = new InputSource(url.openStream());
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();

				XMLReader xr = sp.getXMLReader();
				xr.setContentHandler(myExampleHandler);
				xr.parse(is);
			} catch (Exception e) {
				Log.e(LOG_TAG, "doInBackground Error", e);
				Toast.makeText(activity_1_map.this, e.toString(),
						Toast.LENGTH_LONG).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			sh_name = myExampleHandler.getParsedData().elementAt(0)
					.getExtractedString(2);
			
			sh_addr = myExampleHandler.getParsedData().elementAt(0)
					.getExtractedString(5);
			
			sh_phone = myExampleHandler.getParsedData().elementAt(0)
					.getExtractedString(6);
			
			// Log.i(LOG_TAG, sh_phone);
			if (sh_phone.length() > 7) {
				String[] tmp = sh_phone.split("-");

				// 국번 없는 전화번호이면
				if (tmp.length == 2) {
					if(sh_addr.substring(0, 2).equals("서울"))
						sh_phone = "02-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("인천"))
						sh_phone = "032-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("대전"))
						sh_phone = "042-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("대구"))
						sh_phone = "053-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("부산"))
						sh_phone = "051-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("강원"))
						sh_phone = "033-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("경기"))
						sh_phone = "031-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("제주"))
						sh_phone = "064-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("충남") || sh_addr.substring(0, 3).equals("충청남"))
						sh_phone = "041-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("충북") || sh_addr.substring(0, 3).equals("충청북"))
						sh_phone = "043-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("경북") || sh_addr.substring(0, 3).equals("경상북"))
						sh_phone = "054-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("경남") || sh_addr.substring(0, 3).equals("경상남"))
						sh_phone = "055-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("전북") || sh_addr.substring(0, 3).equals("전라북"))
						sh_phone = "063-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("전남") || sh_addr.substring(0, 3).equals("전라남"))
						sh_phone = "061-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("광주"))
						sh_phone = "062-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("울산"))
						sh_phone = "052-" + sh_phone;
					else if(sh_addr.substring(0, 2).equals("세종"))
						sh_phone = "044-" + sh_phone;
					else
						;
				}

				if (DEBUG)
					Log.i("전화1", sh_phone);
			}
			
			if (bFromSearch) {
				mOverlayManager.clearOverlays();
				showPOIdataOverlay();
				mPOIdataOverlay.selectPOIitem(0, true);
				// Log.i(LOG_TAG, ""+mPOIdataOverlay.size());
				Activity_MainActivity.sh_id = null;
				bFromSearch = false;
			} else {
				if(myExampleHandler.getParsedData().size() > 0){ // 가격 한개만있으면 에러나는것 해결
					im_name1 = myExampleHandler.getParsedData().elementAt(0).getExtractedString(13);
					im_price1 = myExampleHandler.getParsedData().elementAt(0).getExtractedInt(14);
					
					if(myExampleHandler.getParsedData().size() > 1) {
						im_name2 = myExampleHandler.getParsedData().elementAt(1).getExtractedString(13);
						im_price2 = myExampleHandler.getParsedData().elementAt(1).getExtractedInt(14);
					}
					else {
						im_name2 = null;
						im_price2 = 0;
					}
				}
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			myExampleHandler.clearParsedData();
		}
	}
	
	double getMapCenterLon() {
		return mMapController.getMapCenter().getLongitude();
	}
	
	double getMapCenterLat() {
		return mMapController.getMapCenter().getLatitude();
	}
	
	
	void showMarker(double lon, double lat, int dist, int mode) {
		String url = "http://54.238.131.230/db_get_sh.php?lon="
				+ lon + "&lat="	+ lat + "&dist=" + dist
				+ "&mode=" + mode;

		task = new BackgroundTask();
		task.execute(url);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 회전막기

		setContentView(R.layout.activity_1_map);

		mMapView = (NMapView) findViewById(R.id.mapView); // 맵올려주고
		mTextView = (TextView) findViewById(R.id.tv_address_activity1);
		mCallButton = (ImageButton) getParent().findViewById(
				R.id.page1_top_call_btn);// 전화기 버튼

		// set a registered API key for Open MapViewer Library
		mMapView.setApiKey(API_KEY); // 위에 선언부분에서 이미 입력함

		// initialize map view
		mMapView.setClickable(true);
		mMapView.setEnabled(true);
		mMapView.setFocusable(true);
		mMapView.setFocusableInTouchMode(true);
		mMapView.requestFocus();

		// register listener for map state changes
		mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
		mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
		mMapView.setOnMapViewDelegate(onMapViewTouchDelegate);

		// use map controller to zoom in/out, pan and set map center, zoom level
		// etc.
		mMapController = mMapView.getMapController();

		// use built in zoom controls
		NMapView.LayoutParams lp = new NMapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				NMapView.LayoutParams.BOTTOM_RIGHT);
		mMapView.setBuiltInZoomControls(true, lp);

		// create resource provider
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// set data provider listener
		super.setMapDataProviderListener(onDataProviderListener);

		// create overlay manager
		mOverlayManager = new NMapOverlayManager(this, mMapView,
				mMapViewerResourceProvider);
		// register callout overlay listener to customize it.
		mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);
		// register callout overlay view listener to customize it.
		mOverlayManager
				.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);

		((ImageButton) findViewById(R.id.activity_1_myreallocation))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mMapController.setMapCenter(new NGeoPoint(lon, lat));
						mMapController.animateTo(new NGeoPoint(lon, lat));
						findPlacemarkAtLocation(lon, lat);
						showMarker(lon, lat, 2, mMode);
					}
				});

		mMapController.setMapCenter(NMAP_LOCATION_DEFAULT);
		showMarker(lon, lat, 2, mMode);
		
		startLocationService();

		myAddressBox();
		
		map = activity_1_map.this;
	} // end onCreate

	private void startLocationService() {
		// 위치 관리자 객체 참조
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// 위치 정보를 받을 리스너 생성
		gpsListener = new GPSListener();
		long minTime = 0;
		float minDistance = 0;

		// GPS를 이용한 위치 요청
		if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
		} else {
			provider = LocationManager.NETWORK_PROVIDER;
		}

		manager.requestLocationUpdates(provider, minTime, minDistance,
				gpsListener);

		// 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
		try {
			Location lastLocation = manager.getLastKnownLocation(provider);
			if (lastLocation != null) {
				lat = lastLocation.getLatitude();
				lon = lastLocation.getLongitude();

				findPlacemarkAtLocation(lon, lat);

				if (DEBUG) {
					Toast.makeText(
							getApplicationContext(),
							"Last Known Location : " + "Latitude : " + lat
									+ "\nLongitude:" + lon, Toast.LENGTH_LONG)
							.show();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (DEBUG) {
			Toast.makeText(getApplicationContext(),
					"위치 확인이 시작되었습니다. 로그를 확인하세요.", Toast.LENGTH_SHORT).show();
		}
	}

	public void myAddressBox() {
		// begin alpha for activity1 + 2 //

		Drawable alpha = ((TextView) findViewById(R.id.tv_address_activity1))
				.getBackground();
		alpha.setAlpha(125);

		Drawable alpha2 = ((TextView) findViewById(R.id.tv_address_activity2))
				.getBackground();
		alpha2.setAlpha(125);

	}// end alpha for activity1 + 2 //

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//찾기에서 넘어왔을 때
		if (Activity_MainActivity.sh_id != null) {
			String url = "http://54.238.131.230/db_get_sh_from_id.php?sh_id="
					+ Activity_MainActivity.sh_id;
			bFromSearch = true;
			task_specific_sh = new BgTaskforShPhone();
			task_specific_sh.execute(url);
			// Log.i(LOG_TAG, "OnResume");
		}
		else
			showMarker(mMapController.getMapCenter().getLongitude(), mMapController.getMapCenter().getLatitude(), 2, mMode);
	}

	@Override
	protected void onStop() {
		super.onStop();
		manager.removeUpdates(gpsListener);
	}

	@Override
	protected void onDestroy() {

		// save map view state such as map center position and zoom level.
		saveInstanceState();

		super.onDestroy();

	} // Destory

	/* Test Functions */

	// begin 화면 말풍선 표시
	private void showPOIdataOverlay() {

		// Markers for POI item
		int markerId = NMapPOIflagType.PIN;

		int size = myExampleHandler.getParsedData().size();

		// set POI data
		NMapPOIdata poiData = new NMapPOIdata(size, mMapViewerResourceProvider);
		poiData.beginPOIdata(size);

		for (int i = 0; i < size; i++) {
			// markerId =
			// Integer.parseInt(myExampleHandler.getParsedData().elementAt(i).getExtractedString(1));
			NMapPOIitem item = poiData.addPOIitem(myExampleHandler
					.getParsedData().elementAt(i).getLon(), myExampleHandler
					.getParsedData().elementAt(i).getLat(), myExampleHandler
					.getParsedData().elementAt(i).getExtractedString(13)
					+ ":"
					+ myExampleHandler.getParsedData().elementAt(i)
							.getExtractedInt(14)
					+ "원\n"
					+ myExampleHandler.getParsedData().elementAt(i)
							.getExtractedString(2), markerId, 0);
			item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
			item.setTailText(myExampleHandler.getParsedData().elementAt(i)
					.getExtractedString(1));

			if (DEBUG)
				Log.i(LOG_TAG, myExampleHandler.getParsedData().elementAt(i)
						.getLon()
						+ ","
						+ myExampleHandler.getParsedData().elementAt(i)
								.getLat());
		}

		poiData.endPOIdata();

		if (mPOIdataOverlay != null)
			mOverlayManager.removeOverlay(mPOIdataOverlay);
		// mPOIdataOverlay.removeAllPOIdata();

		// create POI data overlay
		mPOIdataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
		// mPOIdataOverlay.removeAllPOIdata();

		// set event listener to the overlay
		mPOIdataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

		// select an item
		// mPOIdataOverlay.selectPOIitem(0, true);

		// show all POI data
		// poiDataOverlay.showAllPOIdata(0);
	}

	/* NMapDataProvider Listener */
	private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

		@Override
		public void onReverseGeocoderResponse(NMapPlacemark placeMark,
				NMapError errInfo) {

			if (DEBUG) {
				Log.i(LOG_TAG, "onReverseGeocoderResponse: placeMark="
						+ ((placeMark != null) ? placeMark.toString() : null));
			}

			if (errInfo != null) {
				Log.e(LOG_TAG, "Failed to findPlacemarkAtLocation: error="
						+ errInfo.toString());

				Toast.makeText(activity_1_map.this, errInfo.toString(),
						Toast.LENGTH_LONG).show();
				return;
			}

			if (mFloatingPOIitem != null && mFloatingPOIdataOverlay != null) {
				mFloatingPOIdataOverlay.deselectFocusedPOIitem();

				if (placeMark != null) {
					mFloatingPOIitem.setTitle(placeMark.toString());
				}
				mFloatingPOIdataOverlay.selectPOIitemBy(
						mFloatingPOIitem.getId(), false);
			}

			mTextView.setText(placeMark.toString());
			cur_addr = placeMark.toString();
		}

	};

	/* MapView State Change Listener */
	private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {

		@Override
		public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {

			if (errorInfo == null) { // success
				// restore map view state such as map center position and zoom
				// level.
				restoreInstanceState();
				// Testing code//

				// int longitudeE6 = mPreferences.getInt(KEY_CENTER_LONGITUDE,
				// NMAP_LOCATION_DEFAULT.getLongitudeE6());
				// int latitudeE6 = mPreferences.getInt(KEY_CENTER_LATITUDE,
				// NMAP_LOCATION_DEFAULT.getLatitudeE6());
				// int level = mPreferences.getInt(KEY_ZOOM_LEVEL,
				// NMAP_ZOOMLEVEL_DEFAULT);
				// mMapController.setMapCenter(new NGeoPoint(longitudeE6,
				// latitudeE6), level);

				// r.43
				// mMapController.setMapCenter(new NGeoPoint(126.978371,
				// 37.5666091) // 내위치를 여기다 넣어주면될듯
				// , 10); // zoom level

				// End Testing code//

				// mMapController.setMapCenter(null, 14); // zoom level
				// error
				// 현재 화면에서 내위치 화면으로 가라 어떻게 ??

			} else { // fail
				Log.e(LOG_TAG,
						"onFailedToInitializeWithError: "
								+ errorInfo.toString());

				Toast.makeText(activity_1_map.this, errorInfo.toString(),
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onAnimationStateChange(NMapView mapView, int animType,
				int animState) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onAnimationStateChange: animType=" + animType
						+ ", animState=" + animState);
			}
		}

		@Override
		public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onMapCenterChange: center=" + center.toString());
			}
		}

		@Override
		public void onZoomLevelChange(NMapView mapView, int level) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onZoomLevelChange: level=" + level);
			}
		}

		@Override
		public void onMapCenterChangeFine(NMapView mapView) {

		}
	};

	private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

		@Override
		public void onLongPress(NMapView mapView, MotionEvent ev) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLongPressCanceled(NMapView mapView) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTouchDown(NMapView mapView, MotionEvent ev) {

		}

		@Override
		public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
			if (task.getStatus() == AsyncTask.Status.FINISHED) {
				showMarker(mMapController.getMapCenter().getLongitude(), mMapController.getMapCenter().getLatitude(), 2, mMode);
			}
		}

		@Override
		public void onTouchUp(NMapView mapView, MotionEvent ev) {
			// TODO Auto-generated method stub

		}

	};

	private final NMapView.OnMapViewDelegate onMapViewTouchDelegate = new NMapView.OnMapViewDelegate() {

		@Override
		public boolean isLocationTracking() {
			if (mMapLocationManager != null) {
				if (mMapLocationManager.isMyLocationEnabled()) {
					return mMapLocationManager.isMyLocationFixed();
				}
			}
			return false;
		}

	};

	/* POI data State Change Listener */
	private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

		@Override
		public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay,
				NMapPOIitem item) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onCalloutClick: title=" + item.getTitle());
			}

			// [[TEMP]] handle a click event of the callout
			if (sh_addr != null) {

				// Toast.makeText(activity_1_map.this, sh_addr,
				// Toast.LENGTH_LONG).show();

				// Begin 말풍선 Dialog

				AlertDialog.Builder alt_bld = new AlertDialog.Builder(
						activity_1_map.this);
				alt_bld.setIcon(R.drawable.page1_top_call_btn_pressed);
				
				String msg = sh_addr + "\n" + "Tel:" + sh_phone;
				
				if(im_name1 != null) {
					msg = msg + "\n" + im_name1 +":"+im_price1 + "원";
					
					if(im_name2 != null)
						msg = msg + "\n" + im_name2 +":"+im_price2 + "원";
				}
				
				alt_bld.setTitle(sh_name)
					.setMessage(msg)
			
						// AlertDialog alert = alt_bld.create();

						.setPositiveButton("전화걸기",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Action for 'Yes' Button
										Intent in = new Intent(
												Intent.ACTION_DIAL,
												Uri.parse("tel:"
														+ activity_1_map.sh_phone));
										startActivity(in);
									}
								})
						.setNegativeButton("닫기",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Action for 'NO' Button
										dialog.cancel();
									}
								});
				AlertDialog alert = alt_bld.create();
				// Title for AlertDialog
				// alert.setTitle("Title");
				// // Icon for AlertDialog
				// alert.setIcon(R.drawable.icon);
				alert.show();

				// 주소 정리
				// End 말풍선 Dialog
			}
		}

		@Override
		public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay,
				NMapPOIitem item) {
			if (DEBUG) {
				if (item != null) {
					Log.i(LOG_TAG, "onFocusChanged: " + item.toString() + " "
							+ item.getTailText());
				} else {
					Log.i(LOG_TAG, "onFocusChanged: ");
				}
			}

			if (item != null) {
				String url = "http://54.238.131.230/db_get_sh_from_id2.php?sh_id="
						+ item.getTailText();

				task_specific_sh = new BgTaskforShPhone();
				task_specific_sh.execute(url);

				// 전화 가능 버튼
				if (mCallButton != null) {
					mCallButton
							.setImageResource(R.drawable.page1_top_call_btn_pressed);
				}
			} else {
				im_name1 = null;
				im_price1 = 0;
				im_name2 = null;
				im_price2 = 0;
				sh_phone = null;
				sh_addr = null;

				// 전화 불가 버튼
				if (mCallButton != null)
					mCallButton.setImageResource(R.drawable.page1_top_call_btn);
			}
		}

	};

	private final NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener = new NMapOverlayManager.OnCalloutOverlayListener() {

		@Override
		public NMapCalloutOverlay onCreateCalloutOverlay(
				NMapOverlay itemOverlay, NMapOverlayItem overlayItem,
				Rect itemBounds) {

			// handle overlapped items
			if (itemOverlay instanceof NMapPOIdataOverlay) {
				NMapPOIdataOverlay poiDataOverlay = (NMapPOIdataOverlay) itemOverlay;

				// check if it is selected by touch event
				if (!poiDataOverlay.isFocusedBySelectItem()) {
					int countOfOverlappedItems = 1;

					NMapPOIdata poiData = poiDataOverlay.getPOIdata();
					for (int i = 0; i < poiData.count(); i++) {
						NMapPOIitem poiItem = poiData.getPOIitem(i);

						// skip selected item
						if (poiItem == overlayItem) {
							continue;
						}

						// check if overlapped or not
						if (Rect.intersects(poiItem.getBoundsInScreen(),
								overlayItem.getBoundsInScreen())) {
							countOfOverlappedItems++;
						}
					}

					if (countOfOverlappedItems > 1) {
						String text = countOfOverlappedItems
								+ " overlapped items for "
								+ overlayItem.getTitle();
						Toast.makeText(activity_1_map.this, text,
								Toast.LENGTH_LONG).show();
						return null;
					}
				}
			}

			// use custom old callout overlay
			if (overlayItem instanceof NMapPOIitem) {
				NMapPOIitem poiItem = (NMapPOIitem) overlayItem;

				if (poiItem.showRightButton()) {
					return new NMapCalloutCustomOldOverlay(itemOverlay,
							overlayItem, itemBounds, mMapViewerResourceProvider);
				}
			}

			// use custom callout overlay
			return new NMapCalloutCustomOverlay(itemOverlay, overlayItem,
					itemBounds, mMapViewerResourceProvider);

			// set basic callout overlay
			// return new NMapCalloutBasicOverlay(itemOverlay, overlayItem,
			// itemBounds);
		}

	};

	private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener() {

		@Override
		public View onCreateCalloutOverlayView(NMapOverlay itemOverlay,
				NMapOverlayItem overlayItem, Rect itemBounds) {

			if (overlayItem != null) {
				// [TEST] 말풍선 오버레이를 뷰로 설정함
				String title = overlayItem.getTitle();
				if (title != null && title.length() > 5) {
					return new NMapCalloutCustomOverlayView(
							// activity_1_map.this, itemOverlay, overlayItem,
							// itemBounds);
							activity_1_map.this, itemOverlay, overlayItem,
							itemBounds);
				}
			}
			// begin 말풍선
			// null을 반환하면 말풍선 오버레이를 표시하지 않음
			return null;
			// end 말풍선
		}

	};

	/* Local Functions */

	private void restoreInstanceState() {
		mPreferences = getPreferences(MODE_PRIVATE);

		int longitudeE6 = mPreferences.getInt(KEY_CENTER_LONGITUDE,
				NMAP_LOCATION_DEFAULT.getLongitudeE6());
		int latitudeE6 = mPreferences.getInt(KEY_CENTER_LATITUDE,
				NMAP_LOCATION_DEFAULT.getLatitudeE6());
		int level = mPreferences.getInt(KEY_ZOOM_LEVEL, NMAP_ZOOMLEVEL_DEFAULT);
		int viewMode = mPreferences.getInt(KEY_VIEW_MODE,
				NMAP_VIEW_MODE_DEFAULT);
		boolean trafficMode = mPreferences.getBoolean(KEY_TRAFFIC_MODE,
				NMAP_TRAFFIC_MODE_DEFAULT);
		boolean bicycleMode = mPreferences.getBoolean(KEY_BICYCLE_MODE,
				NMAP_BICYCLE_MODE_DEFAULT);

		mMapController.setMapViewMode(viewMode);
		mMapController.setMapViewTrafficMode(trafficMode);
		mMapController.setMapViewBicycleMode(bicycleMode);
		mMapController.setMapCenter(new NGeoPoint(longitudeE6, latitudeE6),
				level); // 객체 생성함
	}

	private void saveInstanceState() {
		if (mPreferences == null) {
			return;
		}

		NGeoPoint center = mMapController.getMapCenter();
		int level = mMapController.getZoomLevel();
		int viewMode = mMapController.getMapViewMode();
		boolean trafficMode = mMapController.getMapViewTrafficMode();
		boolean bicycleMode = mMapController.getMapViewBicycleMode();

		SharedPreferences.Editor edit = mPreferences.edit();

		edit.putInt(KEY_CENTER_LONGITUDE, center.getLongitudeE6());
		edit.putInt(KEY_CENTER_LATITUDE, center.getLatitudeE6());
		edit.putInt(KEY_ZOOM_LEVEL, level);
		edit.putInt(KEY_VIEW_MODE, viewMode);
		edit.putBoolean(KEY_TRAFFIC_MODE, trafficMode);
		edit.putBoolean(KEY_BICYCLE_MODE, bicycleMode);

		edit.commit();

	}
}
