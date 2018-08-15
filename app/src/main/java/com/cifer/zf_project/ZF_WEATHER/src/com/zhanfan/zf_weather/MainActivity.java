package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cifer.zf_project.R;
import com.zhanfan.zf_weather.database.Sqlitedatabase;
import com.zhanfan.zf_weather.entity.Cityandwoeid;
import com.zhanfan.zf_weather.entity.Cityweather;
import com.zhanfan.zf_weather.tool.FragAdapter;
import com.zhanfan.zf_weather.tool.Getnetdata;
import com.zhanfan.zf_weather.tool.Searchcity;
import com.zhanfan.zf_weather.tool.ViewFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnClickListener {
    private long lastClickTime;//涓婃鐐瑰嚮鐨勬椂闂�


	TextView temp;
	TextView locationcity;
	TextView textweather;
	ListView listView1;
	ImageView more;
	ImageView refres;
	List<Cityweather> cityweatherinfor;
	Getnetdata getnetdata = null;
	boolean getdata = false;
	private  ProgressDialog pDialog;
	ViewPager viewPager;
	List<Fragment> viewList = null;
	String[] woeidStrings = null;
	 List<Cityandwoeid> woeidList = null;
	Context context;
	DrawerLayout drawerLayout = null;
	View leftview = null;

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (pDialog != null) {
				pDialog.dismiss();

			}
			if (intent.getAction().equals("com.zhanfan.weatherdata")) {
				getdata = true;
			} else if (intent.getAction().equals("com.zhanfan.weatherforecast")) {
				// updatelistview();
			}
		}
	};
	
	FragAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.welcomeactivity);
		context = MainActivity.this;
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_main);
		drawerLayout.setScrimColor(0x00000000);
		leftview = findViewById(R.id.left_drawer);
		drawerLayout.setScrimColor(0x00000000);

		Sqlitedatabase.createtablewoeid();
		Sqlitedatabase.ceatetableweather();

		LayoutInflater inflater = getLayoutInflater();
		viewList = new ArrayList<Fragment>();
		//woeidList = new ArrayList<Cityandwoeid>();

		viewPager = (ViewPager) findViewById(R.id.viewpagerload);
		if (savedInstanceState != null) {
			Log.i("getdatataeta", "1111111");
			getdata = savedInstanceState.getBoolean("getdata");
		}
		// registreciver();

		// FragAdapter adapter = new FragAdapter(getSupportFragmentManager(),
		// viewList);

		// temp = (TextView) findViewById(R.id.daytemp);
		// locationcity = (TextView) findViewById(R.id.location);
		// textweather = (TextView) findViewById(R.id.dayweather);
		more = (ImageView) findViewById(R.id.more);
		refres=(ImageView) findViewById(R.id.refresh11);
		more.setOnClickListener(this);
        refres.setOnClickListener(this); 
      /* refres.setVisibility(View.INVISIBLE);*/
		Log.i("getdatataeta", "22222222222    " + getdata);
		// if(getdata){
		// updatelistview();
		// }else{
		// showProgressDialog("updating.....");
		// }

		// updatelistview();
		
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.i("zhaoyi_log1124","onresume mainactivity");
		//Log.i("zhaoyi_log1124","Sqlitedatabase.selectwoeid(changdu  "+Sqlitedatabase.selectwoeid().size());
		browser();
	}

	public void browser() {
		if (woeidList != null) {
			Log.i("zhaoyi_log1119", "woeidList111111111  " + woeidList.size());
			woeidList = null;
			Log.i("zhaoyi_log1119", "woeidList2222222  " + woeidList);
		}
		woeidList = Sqlitedatabase.selectwoeid(); 
		if (woeidList == null) {
			Log.i("zhaoyi_log1119", "woeidList  " + woeidList);
			viewPager.setAdapter(null);
			Toast.makeText(MainActivity.this,
					"Please add cities to connect to the Internet ", 
					Toast.LENGTH_LONG).show();
		} else {
			Log.i("zhaoyi_log12yang", "woeidList  " + woeidList.size());
			viewList.clear();
//			for(int i=0; i<adapter.getmFragments().size(); i++) {
//				getSupportFragmentManager().beginTransaction().detach(adapter.getmFragments().get(i));
//			} 
			
			for (int i = 0; i < woeidList.size(); i++) {
				Log.i("zhaoyi_log12yang", "woeidstring  " + woeidList.get(i)
						.getWoeidString());
				viewList.add(new ViewFragment(
						woeidList.get(i).getCitydefault(), woeidList.get(i)
								.getWoeidString(), woeidList.get(i)
								.getCityname(), woeidList.get(i).getCityfid()));
				//Log.i("zhaoyi_log1124","sqlitedefaultcityfault  "+woeidList.get(i).getCitydefault());
				Log.i("guobin","sqlitedefaultwoeid  "+woeidList.get(i).getWoeidString());
				//Log.i("zhaoyi_log1124","sqlitedefaultcityname  "+woeidList.get(i).getCityname());
				//Log.i("zhaoyi_log1124","sqlitedefaultfid  "+woeidList.get(i).getCityfid());
				
			}
			/*FragAdapter adapter = new FragAdapter(getSupportFragmentManager(),
					viewList);
			viewPager.setAdapter(adapter);*/
			//adapter=null; 
			adapter = new FragAdapter(getSupportFragmentManager(), viewList);
			viewPager.setAdapter(adapter); 
			//adapter.notifyDataSetChanged();
		}
	}

	public int getsavecity() {
		SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);

		int size = pref.getInt("skeysize", 0);
		woeidStrings = new String[size];
		for (int i = 0; i < size; i++) {
			woeidStrings[i] = pref.getString("woeid_" + i, "111111111");

		}
		Log.i("SharedPreferences", "22222222222    " + size);
		return size;
	}

	

	// registbroadcastreciver
	public void registreciver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.zhanfan.weatherforecast");
		intentFilter.addAction("com.zhanfan.weatherdata");
		registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.i("getdatataeta", "3333333333333    " + getdata);
		outState.putBoolean("getdata", getdata);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// unregisterReceiver(broadcastReceiver);
	}

	

	private  void showProgressDialog(String title) {
		
		Log.i("zy_log", "555555");
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		pDialog.setMessage(title + "...");
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.show();
		Log.i("zy_log", "666666");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void switchLeftLayout() {
		if (drawerLayout.isDrawerOpen(leftview)) {
			drawerLayout.closeDrawer(leftview);
		} else {
			drawerLayout.openDrawer(leftview);
		}
	}
   /**
	*  避免两次按键连续短时间内点击
	* @return
	*/
	private boolean isFastDoubleClick() {
	long time = System.currentTimeMillis();
	long timeD = time - lastClickTime;
		if ( 0 < timeD && timeD < 2500) {
		   return false;
		}
		lastClickTime = time;
		return true;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.more:
			Log.i("ttttttttttttttttttttttttttt", "11111111111111111111111");
			/*
			 * Intent intent = new Intent(this, Searchcity.class);
			 * startActivity(intent);*/
			 
			switchLeftLayout();
			break;
		case R.id.refresh11:
			Log.i("zhaoyi_log1227","refresh121213132");
//			AnimationSet set = new AnimationSet(true);
//			RotateAnimation rotate = new RotateAnimation(0, 360,
//					Animation.RELATIVE_TO_SELF,
//					0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//			rotate.setDuration(500);
//			set.addAnimation(rotate);
//			refres.startAnimation(set);
           	ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = connectivityManager
				.getActiveNetworkInfo();

			if(networkInfo != null && networkInfo.isAvailable()){
		        if(isFastDoubleClick()){
					Intent intentupdate=new Intent();
					intentupdate.setAction("com.zhanfan.updateweatherdata");
					sendBroadcast(intentupdate);
		        }
			}else{
				Toast.makeText(MainActivity.this,"Please check netWork! ", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.add_city:
			Log.i("zhaoyi_log1119", "life");
			Intent intent = new Intent(this, Searchcity.class);
			startActivity(intent);
			switchLeftLayout();
			// switchLeftLayout();
			// switchFragment(new LifeIndexFragment(),null);
			break;
		case R.id.btn_city_manager:
			Log.i("zhaoyi_log1119", "btn_city_manager");
			switchLeftLayout();
			// switchFragment(citymanager, null);

			Intent intent1 = new Intent(this, Citymanager.class);
			startActivity(intent1);
			// switchLeftLayout();
			break;
//		case R.id.show_weather:
//			Log.i("zhaoyi_log1119", "btn_change_bag");
//
//			switchLeftLayout();
//			// switchFragment(new ViewFragment(), null);
//			browser();
//			break;
//		
//		case R.id.btn_share_app:
//			Log.i("zhaoyi_log1119", "btn_share_app");
//			switchLeftLayout();
//			// SystemUtils.shareApp(this);
//			break;
		case R.id.exitapp:
			Log.i("zhaoyi_log1119", "exitapp");
			// showExitDialog();
			switchLeftLayout();
			finish();
			break;
		default:
			break;
		}
	}

	public void switchFragment(Fragment fragment, String str) {
		Log.i("zhaoyi_log1120", "switchFragment");
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.framelayout111, fragment, null);
		// ft.replace(R.id.fragmentlayout, fragment, str);
		Log.i("zhaoyi_log1120", "switchFragment111111");
		ft.commit();
		ft.show(fragment);
	}

}
