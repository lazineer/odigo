package com.chowin21.android_odi_go;

import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class Activity_MainActivity extends TabActivity implements OnTabChangeListener {
	public static String selectedMenu = null;
	public static String sh_id = null;
	public static double sh_lon = 0.0;
	public static double sh_lat = 0.0;
	public static String selected = " > 자동메뉴";
	Intent searchIntent;
	/** Called when the activity is first created. */

	private TabHost m_tabHost = null;

	// begin toast 귀찮을때 //
	void showToast(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	// end toast 귀찮을때 //

	// begin item dialog //
	private String[] mItems = null;

	private int[] mIcons = null;

	// end item dialog //
	public void showSpecificShOnMap(String sh_id, double lon, double lat) {
		// showToast(sh_id);
		this.sh_id = sh_id;
		this.sh_lon = lon;
		this.sh_lat = lat;
		m_tabHost.setCurrentTab(0);
	}
	
	public void showSearchTab(String selected) {
		selectedMenu = selected;
		m_tabHost.setCurrentTab(3);
	}


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 회전막기
		setContentView(R.layout.activity_mainactivity_tapview_custom_layout);

		/** TabHost ID */
		m_tabHost = (TabHost) findViewById(android.R.id.tabhost);
		TabSpec map_TabSpec = m_tabHost.newTabSpec("map");
		TabSpec select_TabSpec = m_tabHost.newTabSpec("select");
		TabSpec input_TabSpec = m_tabHost.newTabSpec("input");
		TabSpec search_TabSpec = m_tabHost.newTabSpec("search");
		TabSpec category_TabSpec = m_tabHost.newTabSpec("category");

		LayoutInflater vi1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutInflater vi2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutInflater vi3 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutInflater vi4 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutInflater vi5 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View View_1 = (View) vi1.inflate(R.layout.activity_mainactivity_tab_row_item, null);
		View View_2 = (View) vi2.inflate(R.layout.activity_mainactivity_tab_row_item, null);
		View View_3 = (View) vi3.inflate(R.layout.activity_mainactivity_tab_row_item, null);
		View View_4 = (View) vi4.inflate(R.layout.activity_mainactivity_tab_row_item, null);
		View View_5 = (View) vi5.inflate(R.layout.activity_mainactivity_tab_row_item, null);
		LinearLayout Layout_1 = (LinearLayout) View_1.findViewById(R.id.LinearLayout01);
		LinearLayout Layout_2 = (LinearLayout) View_2.findViewById(R.id.LinearLayout01);
		LinearLayout Layout_3 = (LinearLayout) View_3.findViewById(R.id.LinearLayout01);
		LinearLayout Layout_4 = (LinearLayout) View_4.findViewById(R.id.LinearLayout01);
		LinearLayout Layout_5 = (LinearLayout) View_5.findViewById(R.id.LinearLayout01);
		Layout_1.setBackgroundResource(R.drawable.tab_1_bg);
		Layout_2.setBackgroundResource(R.drawable.tab_2_bg);
		Layout_3.setBackgroundResource(R.drawable.tab_3_bg);
		Layout_4.setBackgroundResource(R.drawable.tab_4_bg);
		Layout_5.setBackgroundResource(R.drawable.tab_5_bg);

		map_TabSpec.setIndicator(View_1);
		map_TabSpec.setContent(new Intent(this, activity_1_map.class));
		select_TabSpec.setIndicator(View_2);
		select_TabSpec.setContent(new Intent(this, activity_2_selected.class));
		input_TabSpec.setIndicator(View_3);
		input_TabSpec.setContent(new Intent(this, activity_3_input.class));
		search_TabSpec.setIndicator(View_4);
		searchIntent = new Intent(this, activity_4_search.class);
		search_TabSpec.setContent(searchIntent);

		category_TabSpec.setIndicator(View_5);
		category_TabSpec.setContent(new Intent(this, activity_5_category.class));

		/** 탭을 TabHost 에 추가한다 */
		m_tabHost.addTab(map_TabSpec);
		m_tabHost.addTab(select_TabSpec);
		m_tabHost.addTab(input_TabSpec);
		m_tabHost.addTab(search_TabSpec);
		m_tabHost.addTab(category_TabSpec);

		m_tabHost.setCurrentTab(0);
		
		mItems = getResources().getStringArray(R.array.items);
		mIcons = new int[5];

		mIcons[0] = R.drawable.activity_0_menu_icon_01;
		mIcons[1] = R.drawable.activity_0_menu_icon_02;
		mIcons[2] = R.drawable.activity_0_menu_icon_04;
		mIcons[3] = R.drawable.activity_0_menu_icon_06;
		mIcons[4] = R.drawable.activity_0_menu_icon_08;

		ImageButton kind_btn = (ImageButton) findViewById(R.id.page1_top_kind_btn);
		kind_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new CustomDialog(Activity_MainActivity.this)
						.setIcon(R.drawable.page1_top_kind_btn)
						.setTitleText("오디Go?")
						.setItems(mItems, mIcons, Gravity.LEFT,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
	
										if (which == 0) {
											showShortToast("점심메뉴가 선택됨 ");
											selected = "> 점심메뉴";
											activity_1_map.mMode = 0;
										} else if (which == 1) {
											showShortToast("후식 음료수등 메뉴가 선택됨");
											selected = "> 후식메뉴";
											activity_1_map.mMode = 1;
										} else if (which == 2) {
											showShortToast("놀이 메뉴가 선택됨");
											selected = "> 놀이메뉴";
											activity_1_map.mMode = 2;
										} else if (which == 3) {
											showShortToast("숙박 메뉴가 선택됨");
											selected = "> 숙박메뉴";
											activity_1_map.mMode = 3;
										} else if (which == 4) {
											showShortToast("시간별 자동메뉴가 선택됨");
											selected = "> 자동메뉴";
											activity_1_map.mMode = 4;
										}

										dialog.cancel();
									}
								}).setNegativeButton(R.string.cancel,
										new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								}).show();

			}
		});

		// end ItemsDialog - 나침판 다이얼로그로 표현 //

		// begin call button//

		ImageButton page1_top_call_btn = (ImageButton) findViewById(R.id.page1_top_call_btn);
		page1_top_call_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				if (activity_1_map.sh_phone == null) {
					showShortToast("전화걸 마커를 먼저 클릭해주세요");
				} else {
					Intent in = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"	+ activity_1_map.sh_phone));
					startActivity(in);

					// Log.i("전화2", activity_1_map.sh_phone);
				}
			}
		});

		// end call button//

	}// end oncreate

	protected void showShortToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
