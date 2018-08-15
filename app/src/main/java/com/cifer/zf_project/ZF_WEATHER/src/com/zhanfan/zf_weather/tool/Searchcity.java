package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.zhanfan.zf_weather.MainActivity;
import com.zhanfan.zf_weather.R;
import com.zhanfan.zf_weather.Showweather;
import com.zhanfan.zf_weather.WeatherContants;
import com.zhanfan.zf_weather.database.Sqlitedatabase;
import com.zhanfan.zf_weather.entity.CityInfo;
import com.zhanfan.zf_weather.entity.Cityweather;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Window;
import android.view.WindowManager;
public class Searchcity extends Activity implements OnClickListener,
		OnItemClickListener {
	public static final int SERVICE_CONNECTED = 0;
	public static final int SERVICE_UNCONNECTED = 1;
	public static final int SERVICE_QUERYCITY = 2;
	private QueryCityHandler queryCityHandler;
	private String localeLang;
	private String citytext;
	private TextView cancelText;
	private EditText searchCityView;
	private ListView cityListView;
	private TextView cityEmpty;
	private ImageView deleteView;
	private CommonListAdapter cityAdapter = null;
	Weatherservice weatherservice = null;
	private List<CityInfo> mList;
	private ShowCityReceiver mShowCityReceiver;
	List<Cityweather> searchcity = null;
	List<String> getwoeidList =null;
	String woeid;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x06) {
				getserachcityweather(woeid);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.citymanager);
		setContentView(R.layout.searchcity);

		//getActionBar().hide();
		mList = new ArrayList<CityInfo>();
		searchcity = new ArrayList<Cityweather>();
		weatherservice = new Weatherservice();
		cancelText = (TextView) findViewById(R.id.search_cancel);
		cancelText.setOnClickListener(this);
		searchCityView = (EditText) findViewById(R.id.search_city_view);
		searchCityView.addTextChangedListener(textWater);
		cityListView = (ListView) findViewById(R.id.city_list);
		deleteView = (ImageView) findViewById(R.id.search_delete);
		deleteView.setOnClickListener(this);
		handleListAdapter();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
		mShowCityReceiver = new ShowCityReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(WeatherContants.ACTION_UPDATE_VIEW);
		registerReceiver(mShowCityReceiver, filter);
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mShowCityReceiver);
	}

	private class ShowCityReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// Log.i(TAG,"onReceive") ;

			refreshListView();
			if (TextUtils.isEmpty(searchCityView.getText().toString().trim())) {
				// cityEmpty.setVisibility(View.GONE) ;
			}
		}

	}

	public void refreshListView() {
		if (cityAdapter != null) {
			cityAdapter.listHasChange(mList);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.search_cancel:
			finish() ;
			//overridePendingTransition(R.drawable.push_down_in, R.drawable.push_down_out) ;
			break ;
		case R.id.search_delete :
			searchCityView.setText("") ;
			deleteView.setVisibility(View.GONE) ;
			break ;
		}		
	}

	private TextWatcher textWater = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO onTextChanged
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO beforeTextChanged

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO afterTextChanged
			// Log.i(TAG,"afterTextChanged") ;
			// mList.clear() ;
			String newText = s.toString();
			if (newText != null && newText.length() != 0) {
				if (NetUtil.hasNetwork(Searchcity.this)) {
					String queryStr = newText.toString().trim()
							.replace('\n', ' ').replace('\t', ' ');
					citytext = queryStr;
					getQueryResult(queryStr);
					Log.i("textchange", "queryStr  " + queryStr);
				}
				if(newText.length() == 1){
					 deleteView.setVisibility(View.VISIBLE) ;
				 }
			} else {
				deleteView.setVisibility(View.GONE) ;
			}
			Log.i("zhaoyi_log1124", "newText  " + newText);
			// if( newText!= null && newText.length() != 0 ){
			// cityEmpty.setVisibility(View.VISIBLE) ;
			// if(NetUtil.hasNetwork(SearchCity.this)){
			// String queryStr = newText.toString().trim().replace('\n',
			// ' ').replace('\t', ' ') ;
			// cityText = queryStr ;
			// getQueryResult(queryStr) ;
			// }
			// if(newText.length() == 1){
			// deleteView.setVisibility(View.VISIBLE) ;
			// }
			// }else{
			// deleteView.setVisibility(View.GONE) ;
			// //cityAdapter.listHasChange(mList) ;
			// cityEmpty.setVisibility(View.GONE) ;
			// }
		}
	};

	private void handleListAdapter() {
		Log.i("zhaoyi_log1124", "mlist  "+(mList==null));
		cityAdapter = new CommonListAdapter(this, mList, R.layout.cityitem,
				R.id.city_name, R.id.zone_name) {

			@Override
			public void bindViews(int position, View... views) {
				// Log.i(TAG,"bindViews" + mList.size()) ;
				handleBindView(position, views);
			}
		};
		cityListView.setAdapter(cityAdapter);
		cityListView.setOnItemClickListener(this);
	}

	protected void handleBindView(int position, View[] views) {
		// TODO Auto-generated method stub
		int size = mList.size();
		if (size <= position) {
			return;
		}
		CityInfo cityInfo = mList.get(position);
		((TextView) views[0]).setText(cityInfo.getName());
		((TextView) views[0]).setTag(cityInfo);
		((TextView) views[1]).setText(cityInfo.getCountry()
				.concat(cityInfo.getAdmin1()).concat(cityInfo.getAdmin2()));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Log.i("inputcity", "onitemclick    " + mList.get(position).getWoeid());
		Log.i("inputcity", "onitemclick    " + mList.get(position).getAdmin1());
		// getserachcityweather(mList.get(position).getWoeid());
		woeid = null;
		woeid = mList.get(position).getWoeid();
		 Log.i("zhaoyi_log1120","woeid   "+woeid);
	   if(!Sqlitedatabase.selectwoeidifexit(woeid)){
		   Log.i("zhaoyi_log1120","woeid   "+woeid);
			handler.sendEmptyMessage(0x06);
	   }else{
		   Toast.makeText(this, "The city already exists!!! ", Toast.LENGTH_LONG).show();
	   }
		

	}

	// ¿ªÆô×ÓÏß³Ì
	public void getserachcityweather(final String woeidString) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				searchcity = null;
				// searchcity=weatherservice.sendhttpgetweatherinfo(woeidString);
				Intent intent = new Intent(Searchcity.this, Showweather.class);
				intent.putExtra("woeid", woeidString);
				// getwoeidList=new ArrayList<String>();
				//getsearchcity(woeidString);
				//savesearchcity();
				startActivity(intent);

			}

		}).start();
	}

	public void getsearchcity(String woeidString) {
		SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
		getwoeidList = new ArrayList<String>();
		int size = pref.getInt("skeysize", 0);
		Log.i("SharedPreferences", "size  "+size);
		if (size != 0) {
			boolean addornot = true;
			for (int i = 0; i < size; i++) {
				String getwoeid = pref.getString("woeid_" + i, "111111111");
				if (getwoeid.equals(woeidString)) {
					addornot = false;
					Log.i("SharedPreferences", "addornot  "+addornot);
				}
				getwoeidList.add(getwoeid);
			}
			if (addornot) {
				getwoeidList.add(woeidString);
			}
		}else{
			Log.i("SharedPreferences", "getwoeidList.add(    22222");
			getwoeidList.add(woeidString);
		}
	}

	public void savesearchcity() {
		// SharedPrefernces
		// sp=SharedPrefernces.getDefaultSharedPrefernces(this);
		SharedPreferences.Editor editor = getSharedPreferences("data",
				MODE_PRIVATE).edit();
		
		editor.putInt("skeysize", getwoeidList.size());
		for (int i = 0; i < getwoeidList.size(); i++) {
			editor.putString("woeid_" + i, getwoeidList.get(i));
		}
		editor.commit();
	}

	private void getQueryResult(String input) {
		if (input == null || input.length() == 0) {
			return;
		}
		Log.i("tag2", "getQueryResult");
		localeLang = Locale.getDefault().getLanguage();
		handleQueryCity();
	}

	public void getNewHandler() {

		HandlerThread handlerThread = new HandlerThread("checkServiceConnect",
				Process.THREAD_PRIORITY_BACKGROUND);
		handlerThread.start();

		Looper looper = handlerThread.getLooper();

		queryCityHandler = new QueryCityHandler(looper);
	}

	private void handleQueryCity() {
		// TODO handleQueryCity
		// Log.i(TAG,"handleQueryCity") ;
		getNewHandler();
		queryCityHandler.removeCallbacks(timerRunnable);
		queryCityHandler.postDelayed(timerRunnable, 500);
		queryCityHandler.sendEmptyMessage(SERVICE_QUERYCITY);
	}

	private Runnable timerRunnable = new Runnable() {

		@Override
		public void run() {
			queryCityHandler.sendEmptyMessage(SERVICE_QUERYCITY);
		}
	};

	public class QueryCityHandler extends Handler {
		public QueryCityHandler() {
			super();
			// TODO Auto-generated constructor stub
		}

		public QueryCityHandler(Looper looper) {
			super(looper);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SERVICE_CONNECTED:
				// progressDialog.dismiss() ;
				break;
			case SERVICE_QUERYCITY:
				mList = weatherservice.queryCityInfo(citytext, localeLang);
				
//				for (int i = 0; i < mList.size(); i++) {
//					Log.i("inputcity", "ttttttttttt    "
//							+ mList.get(i).getWoeid());
//					Log.i("inputcity", "ttttttttttt    "
//							+ mList.get(i).getCountry());
//				}
				Intent updateView = new Intent(
						WeatherContants.ACTION_UPDATE_VIEW);
				sendBroadcast(updateView);
				break;
			}
		}
	}
}
