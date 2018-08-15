package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.RemoteViews;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.cifer.zf_project.R;
import com.zhanfan.zf_weather.database.Sqlitedatabase;
import com.zhanfan.zf_weather.entity.Cityweather;
import com.zhanfan.zf_weather.entity.Dataforecast;
import com.zhanfan.zf_weather.tool.MyHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//wchao

public class Updateservice extends Service {
	private SimpleDateFormat df = new SimpleDateFormat("HHmmss");
	public static RemoteViews remoteViews;
	public static Context context;
	public static AppWidgetManager appwidetmanger;
	public static List<Cityweather> cityweathers = null;// 五天 天气预报信息
	// 为定位城市服务
	private LocationManager locationManager;
	private String provider;
	String citynameString = null;// 获得城市名字
	String woeid = null;// 获得城市的woeid
	// String weatherinformation[] =new String[3];
	boolean hourchangeornot = true;
	int hourchange = 0;
	public static List<String> weatherinformation = new ArrayList<String>();// 获取的天气信息
	// 　Calendar c = Calendar.getInstance();
	String clickaction="com.zhanfanweather.click";
	String clickactiondesk = "com.zhanfandeskclock.click";
		int code = (int) SystemClock.uptimeMillis();
		int flag = PendingIntent.FLAG_UPDATE_CURRENT;
	private static final String ANDROID_CLOCK_FONT_FILE = "/system/fonts/Samsung_Clock.ttf";
	private static Typeface mClockTypeface = null;
	String jingduString = null;
	String weiduString = null;
	Calendar calendar = null;
	int[] numbericon = new int[]{R.drawable.num_0, R.drawable.num_01,
			R.drawable.num_02, R.drawable.num_03, R.drawable.num_04,
			R.drawable.num_05, R.drawable.num_06, R.drawable.num_07,
			R.drawable.num_08, R.drawable.num_09

	};
	int[] weatherpicture = new int[]{R.drawable.org3_ww0,
			R.drawable.org3_ww1, R.drawable.org3_ww2, R.drawable.org3_ww3,
			R.drawable.org3_ww4, R.drawable.org3_ww5, R.drawable.org3_ww6,
			R.drawable.org3_ww7, R.drawable.org3_ww8, R.drawable.org3_ww9,
			R.drawable.org3_ww10, R.drawable.org3_ww11, R.drawable.org3_ww12,
			R.drawable.org3_ww13, R.drawable.org3_ww14, R.drawable.org3_ww15,
			R.drawable.org3_ww16, R.drawable.org3_ww17, R.drawable.org3_ww18,
			R.drawable.org3_ww19, R.drawable.org3_ww20, R.drawable.org3_ww21,
			R.drawable.org3_ww22, R.drawable.org3_ww23, R.drawable.org3_ww24,
			R.drawable.org3_ww25, R.drawable.org3_ww26, R.drawable.org3_ww27,
			R.drawable.org3_ww28

	};

/*	String[] month = new String[]{"Jan", "Feb", "Mar", "Apr",
			"May", "June", "July", "Aug", "Sep", "Oct",
			"Nov", "Dec"

	};*/
	String[] month = new String[] { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December"

	};
	//星期一 Mon 星期二 Tue 星期三 Wed 星期四 Thu 星期五 Fri 星期六 Sat 星期天 Sun 
	String[] week = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday"

	};
	//	int[] imageview = new int[] { R.id.imageView1, R.id.imageView2,
//			R.id.imageView3, R.id.imageView4 };
	public AMapLocationClient mapLocationClient = null;
	public AMapLocationClientOption mLocationOption = null;

	@Override
	public void onCreate() {

		startClockListener();
		// TODO Auto-generated method stub
		context = Updateservice.this;
		remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.weatherwidget);
		appwidetmanger = AppWidgetManager.getInstance(context);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Sqlitedatabase.createtablewoeid();
		Sqlitedatabase.ceatetableweather();
		// getlocation();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_TIME_TICK); // 时间的流逝
		intentFilter.addAction(Intent.ACTION_TIME_CHANGED); // 时间被改变，人为设置时间
		intentFilter.addAction(Intent.ACTION_DATE_CHANGED);// 日期被改变，人为设置日期
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		intentFilter.addAction("com.zhanfan.zidonglocation");
		intentFilter.addAction("com.zhanfan.setdefaultcity");

		//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
	    /*if (Build.VERSION.SDK_INT >= 23) {   
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

			connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {  
				@Override 
				public void onAvailable(Network network) {
					super.onAvailable(network);
					if(network != null){
						if (Sqlitedatabase.selectcitydefaultboolen() != null) {
							//判断数据库里是否有设置默认城市，如果有就更新该设置为默认城市的天气信息
							String defaultwoeid = Sqlitedatabase
									.selectcitydefaultboolen().getDefaultwoeid();
							threadweather(defaultwoeid);
						} else {
							//如果没有设置为默认城市就，重新定位，获得城市并更新天气信息
							handler.sendEmptyMessage(0x02);
						}
					}
				}
				@Override
	            public void onLost(Network network) {
	                super.onLost(network);
	                updateUI();
	            }
				
			});
		}else{
			intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		}*/
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(broadcastrecevier, intentFilter);
		super.onCreate();
	}

	// 利用高德地图获得经纬度
	public void getjingweidu() {
		Log.d("wchao getjingweidu", "22222222getjingweidu");
		// 初始化定位
		mapLocationClient = new AMapLocationClient(getApplicationContext());
		Log.d("wchao getjingweidu", "getjingweidu399999");
		mapLocationClient.setLocationListener(mLocationListener);
		 //初始化AMapLocationClientOPtion对象
		Log.d("wchao getjingweidu", "getjingweidu333333333");
		mLocationOption = new AMapLocationClientOption();

		// 设置定位模式AMapLocationMode.Hight_Accuracy,高精度模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		 //设置定位模式为AMapLocationMode.Battery_saving,低功耗模式
		// 低功耗模式只会使用WIFI和基站定位
		mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
		// //仅用设备定位模式：不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，自 v2.9.0 版本支持返回地址描述信息。
		// //设置定位模式为AMapLocationMode.Device_Sensor,仅设备模式
		// mLocationOption.setLocationMode(AMapLocationMode.Device_Sensors);

		// 获取一次定位结果：
		// 该方法默认为false。
		mLocationOption.setOnceLocation(true);

		// 获取最近3s内精度最高的一次定位结果：
		// 设置setOnceLocationLatest(boolean
		// b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean
		// b)接口也会被设置为true，反之不会，默认为false。
		mLocationOption.setOnceLocationLatest(true);
		// 设置是否返回地址信息
		mLocationOption.setNeedAddress(true);

		// 单位是毫秒，默认为30000毫秒，建议超时间不要低于8000毫秒
		mLocationOption.setHttpTimeOut(20000);

		// 设置参数
		mapLocationClient.setLocationOption(mLocationOption);
		Log.d("wchao getjingweidu", "getjingweidu55555");
		// 启动
		mapLocationClient.startLocation();

	}
	AMapLocationListener mLocationListener = new AMapLocationListener() {

		@Override
		public void onLocationChanged(AMapLocation arg0) {
			// TODO Auto-generated method stub
			if (arg0 != null) {
				if (arg0.getErrorCode() == 0) {
					Log.i("AmapError", "精度  " + arg0.getLongitude() + "  纬度  "
							+ arg0.getLatitude());
					threadgetlocationcityname(
							String.valueOf(arg0.getLongitude()),
							String.valueOf(arg0.getLatitude()));
				} else {
					// 定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
					Log.e("AmapError",
							"location Error, ErrCode:" + arg0.getErrorCode()
									+ ", errInfo:" + arg0.getErrorInfo());
				}

			}
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("lllllllllllllllllllll",
				"onStartCommand  "
						+ (Sqlitedatabase.selectcitydefaultboolen() == null));
		updateUI();
		if (Sqlitedatabase.selectcitydefaultboolen() != null) {

		} else {
			getjingweidu();
		}

		// threadgetlocationcityname();
		return super.onStartCommand(intent, flags, startId);
	}

	public void threadgetlocationcityname(final String jingdu,
										  final String weidu) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				getlocationcityname(jingdu, weidu);
				Log.i("lllllllllllllllllllll", "getlocationcityname()");
			}
		}).start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	BroadcastReceiver broadcastrecevier = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// getlocation();
			Log.i("guangbo", "22222222222222guangbo");
			if (intent.getAction().equals(
					"android.net.conn.CONNECTIVITY_CHANGE")) {// 监听网络状态的变化
				Log.i("lllllllllllllllllllll", "888888888");
				ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connectivityManager
						.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isAvailable()) {//如果网络可用
					Log.i("lllllllllllllllllllll", "keyong");
					if (Sqlitedatabase.selectcitydefaultboolen() != null) {
						//判断数据库里是否有设置默认城市，如果有就更新该设置为默认城市的天气信息
						String defaultwoeid = Sqlitedatabase
								.selectcitydefaultboolen().getDefaultwoeid();
						Log.i("lllllllllllllllllllll", "shujuku huode "
								+ defaultwoeid);
						threadweather(defaultwoeid);
					} else {
						//如果没有设置为默认城市就，重新定位，获得城市并更新天气信息
						handler.sendEmptyMessage(0x02);
					}
				} else {//网络不可用
					Log.i("lllllllllllllllllllll", "bukeyong");
					updateUI();
				}
			} else if (intent.getAction().equals("com.zhanfan.zidonglocation")) {//开启自动定位
				Log.i("lllllllllll", "zidonglocation");
				handler.sendEmptyMessage(0x04);
			} else if (intent.getAction().equals("com.zhanfan.setdefaultcity")) {//设为默认城市
				Log.i("lllllllllll", "setdefaultcity");
				//获得对方发广播时候传过来的数据：设为默认城市的名字，和woeid
				String defaultwoeid = intent.getStringExtra("citywoeid");
				String defaultcityname = intent.getStringExtra("cityname");
				Log.i("lllllllllll", "defaultcityname  " + defaultcityname
						+ " defaultwoeid " + defaultwoeid);
				// threadweather(defaultwoeid);
				if (Sqlitedatabase.gettodayweather(defaultwoeid) != null) {//从数据库里获得设为默认城市的天气信息
					if (weatherinformation != null
							|| weatherinformation.size() != 0) {//将此集合里的数据清空，此集合元素为String 类型
						weatherinformation.clear();
					}
					weatherinformation.add(defaultcityname);
					Log.d("wchao defaultname", "defaultname"+defaultcityname);
					weatherinformation.add(Sqlitedatabase
							.getnowtempandweather(defaultwoeid).getTemString());
					weatherinformation.add(Sqlitedatabase
							.getnowtempandweather(defaultwoeid)
							.getWeatherinforString());
					weatherinformation.add(Sqlitedatabase.gettodayweather(
							defaultwoeid).getCode());
					//将数据添加集合之后，就更新界面
					Log.i("lllllllllll", "update----end");
					updateUI();

				}
			}
			Log.i("meizu1111", "1111111");
			updateUI();
		}
	};

	// 判断天气对应正确的图片
	public int getpictureid() {
		int code =28;
		try {
			if (weatherinformation.size()>=4) {
				code = Integer.parseInt(weatherinformation.get(3));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if ((code == 0) || (code == 1) || (code == 2) || (code == 3)
				|| (code == 19) || (code == 20) || (code == 21) || (code == 22)
				|| (code == 23) || (code == 24) || (code == 25)) {
			return 27;
		} else if ((code == 36) || (code == 34) || (code == 32)) {
			return 0;
		} else if ((code == 33) || (code == 31)) {
			return 20;
		} else if ((code == 28) || (code == 30) || (code == 26) || (code == 44)) {
			return 1;
		} else if ((code == 29) || (code == 27)) {
			return 21;
		} else if ((code == 37) || (code == 38) || (code == 39) || (code == 45)
				|| (code == 47)) {
			return 4;
		} else if ((code == 3) || (code == 4)) {
			return 10;
		} else if ((code == 5) || (code == 6) || (code == 7) || (code == 8)
				|| (code == 10) || (code == 18) || (code == 35)) {
			return 6;
		} else if ((code == 9) || (code == 11) || (code == 12) || (code == 40)) {
			return 7;
		} else if ((code == 13) || (code == 14) || (code == 15) || (code == 16)
				|| (code == 42) || (code == 46)) {
			return 12;
		} else if ((code == 41) || (code == 43)) {
			return 15;
		} else if (code == 17) {
			return 26;
		}

		return 28;
	}

	public int getlastpictureid(String codestring) {
		int code = 28;
		try {
			code = Integer.parseInt(codestring);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if ((code == 0) || (code == 1) || (code == 2) || (code == 3)
				|| (code == 19) || (code == 20) || (code == 21) || (code == 22)
				|| (code == 23) || (code == 24) || (code == 25)) {
			return 27;
		} else if ((code == 36) || (code == 34) || (code == 32)) {
			return 0;
		} else if ((code == 33) || (code == 31)) {
			return 20;
		} else if ((code == 28) || (code == 30) || (code == 26) || (code == 44)) {
			return 1;
		} else if ((code == 29) || (code == 27)) {
			return 21;
		} else if ((code == 37) || (code == 38) || (code == 39) || (code == 45)
				|| (code == 47)) {
			return 4;
		} else if ((code == 3) || (code == 4)) {
			return 10;
		} else if ((code == 5) || (code == 6) || (code == 7) || (code == 8)
				|| (code == 10) || (code == 18) || (code == 35)) {
			return 6;
		} else if ((code == 9) || (code == 11) || (code == 12) || (code == 40)) {
			return 7;
		} else if ((code == 13) || (code == 14) || (code == 15) || (code == 16)
				|| (code == 42) || (code == 46)) {
			return 12;
		} else if ((code == 41) || (code == 43)) {
			return 15;
		} else if (code == 17) {
			return 26;
		}

		return 28;
	}

	// save lasttime weatherdata
	public void getlastweather() {
		SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);

		String lastcode = pref.getString("code", "00");
		String lastweatherdata = pref.getString("weaherdata", "Cloudly 16℃");
		String lastlocation = pref.getString("location", "New York");

	}

	public void savesearchcity() {
		// SharedPrefernces
		// sp=SharedPrefernces.getDefaultSharedPrefernces(this);
		SharedPreferences.Editor editor = getSharedPreferences("data",
				MODE_PRIVATE).edit();

		if (weatherinformation.size()<4) {
			return;
		}

		editor.putString("code", weatherinformation.get(3));
		editor.putString("weaherdata", weatherinformation.get(2) + " "
				+ weatherinformation.get(1) + "°");
		editor.putString("location", weatherinformation.get(0));
		editor.commit();
	}

	public static CharSequence get24ModeFormat() {
		String skeleton = "Hm";
		return DateFormat.getBestDateTimePattern(Locale.getDefault(), skeleton);
	}


	private Typeface getClockTypeface() {
		if (mClockTypeface == null) {
			mClockTypeface = Typeface.createFromFile(ANDROID_CLOCK_FONT_FILE);
		}
		return mClockTypeface;
	}

	// 根据当前时间设置小部件相应的数字图片
	private void updateUI() {

		remoteViews.setCharSequence(R.id.the_clock, "setFormat24Hour", get24ModeFormat());

		calendar = Calendar.getInstance();
		boolean shifou = DateFormat.is24HourFormat(getBaseContext());// 判断是否是24小时制
		String timeString = df.format(new Date());
		Log.d("wchao timestring", "timeSring---->"+timeString);
		int monthString = calendar.get(Calendar.MONTH);
		int dateString = calendar.get(Calendar.DATE);
		int weekString = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int ii = calendar.get(Calendar.FRIDAY);
		Log.i("meizu1111", "monthString   " + monthString + "   dateString   "
				+ dateString + "    weekString " + weekString + "  ii  " + ii);
		Log.i("1106_zhaoyi", "hourchangeornot  " + hourchangeornot);
		if (hourchangeornot) {

			hourchange = Integer.parseInt(timeString.substring(0, 2));

			Log.i("1106_zhaoyi", "hourchange  " + hourchange);
			hourchangeornot = false;
		}
		// 每隔一小时 向网络访问一次新的天气数据
		int everyhour = Integer.parseInt(timeString.substring(0, 2));
		Log.i("1106_zhaoyi", "everyhour  " + everyhour);
		if (hourchange != everyhour) {
			Log.i("1106_zhaoyi", "hourchange!=everyhour");
			hourchangeornot = true;
			handler.sendEmptyMessage(0x04);
		}
		
//		int num;
//		for (int i = 0; i < imageview.length; i++) {
//			Log.i("zy_log", "timeString   " + timeString);
//			Log.i("zhaoyizhanfan", "shifou   " + shifou);
//
//			if (!shifou) {
//				String time = timeString.substring(0, 2);
//				int hour = Integer.parseInt(time);
//				if (hour >= 13) {
//					hour -= 12;
//					if (hour >= 10) {
//						timeString = hour + timeString.substring(2, 4);
//					} else {
//						timeString = "0" + hour + timeString.substring(2, 4);
//					}
//
//				} else if (hour == 0) {
//					timeString = "12" + timeString.substring(2, 4);
//				}
//			}
//
//			num = timeString.charAt(i) - 48;
//            Log.i("lllllllllllll","数字图片code  "+num);
//			remoteViews.setImageViewResource(imageview[i], numbericon[num]);
//		}
		Log.i("lllllllllll", "判断集合长度   " + weatherinformation.size());
		if (weatherinformation.size() != 0) {

			Log.i("lllllllllllllll", "size   " + weatherinformation.size());
			String city = weatherinformation.get(0);// 城市名字
			Log.d("wchao city", "city============="+city);
			String weainformation = /*weatherinformation.get(2) +*/" "
					+ weatherinformation.get(1) + "℃";// 当地天气信息

			Log.i("lllllllllllllll", "city   " + city + "   weainformation  " + weainformation);
			remoteViews.setTextViewText(R.id.weather, weainformation);

			remoteViews.setTextViewText(R.id.location, city);
			remoteViews.setImageViewResource(R.id.imageView5,
					weatherpicture[getpictureid()]);
		   // remoteViews.setViewVisibility(R.id.text, View.VISIBLE);
			//remoteViews.setViewVisibility(R.id.the_clock, View.VISIBLE);
			//remoteViews.setViewVisibility(R.id.date, View.VISIBLE);
			Log.i("lllllllllllllll", "更新小部件界面，。。。。。。。。。。");
			savesearchcity();
			// String information=city
			Intent intent = new Intent();
			intent.setAction("com.zhanfan.weatherchange");
			intent.setAction("com.zhanfan.weatherforecast");
			intent.putExtra("city", city);
			intent.putExtra("weainformation", weainformation);
			intent.putExtra("picture", getpictureid());
			sendBroadcast(intent);
		}
//	else {
//
//			SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
//
//			String lastcode = pref.getString("code", "60");
//			String lastweatherdata = pref
//					.getString("weaherdata", "Not Connect");
//			String lastlocation = pref.getString("location", "Not Connect");
//			Log.i("lllllllllllllll", "lastcode   " + lastcode
//					+ "  lastweatherdata   " + lastweatherdata
//					+ " lastlocation  " + lastlocation);
//
//			remoteViews.setTextViewText(R.id.weather, lastweatherdata);
//			remoteViews.setTextViewText(R.id.location, lastlocation);
//			remoteViews.setImageViewResource(R.id.imageView5,
//					weatherpicture[getlastpictureid(lastcode)]);
//            Log.i("lllllllllllllll", "更新界面数据");
//
//		}

	/*	String texttime = month[monthString] + " " + dateString + "th" + " "
				+ week[weekString];
		String texttime = month[monthString] + " " + dateString + "th" + " "
				+ week[weekString];*/
		//remoteViews.setTextViewText(R.id.date, texttime);
//		SimpleDateFormat df_time = new SimpleDateFormat("EEE MMMM d");
		//SimpleDateFormat df_time = new SimpleDateFormat("EEEE, MMMM dd");
		//String time = df_time.format(new Date());
		//Log.d("wchao df_time", "df_time"+time);
//		remoteViews.setTextViewText(R.id.date, time);

        String[] ampm_list = new DateFormatSymbols().getAmPmStrings();
        Log.d("wchao ampm","ampm list==="+ "   ampm_list.size()"+ampm_list.length   +"   ampm_list.get(0)=="+ampm_list[0]);
        //String amPm = isPm == null ? "" : (isPm ? ampm[1] : ampm[0]);
        
        int ampm = calendar.get(Calendar.AM_PM);
		if(!shifou){
		    remoteViews.setViewVisibility(R.id.tv_amOrpm,View.VISIBLE);
		    if(ampm == 0){
	            //String am = "AM";
	            Log.d("wchao ampm---0---->",ampm_list[0]);
                remoteViews.setTextViewText(R.id.tv_amOrpm,ampm_list[0]);
		    }else if(ampm == 1){
		       //String pm = "PM";
		         Log.d("wchao ampm---1---->",ampm_list[1]);
		        remoteViews.setTextViewText(R.id.tv_amOrpm,ampm_list[1]);
		    }
	    }else{
	        remoteViews.setViewVisibility(R.id.tv_amOrpm,View.GONE);
	    }
	    
	    
	    
	    
		CharSequence dateFormat = DateFormat.getBestDateTimePattern(Locale.getDefault(),
               "EEEMMMMd");
		Log.d("wchao dateformat","dateformat=="+dateFormat);
      	remoteViews.setCharSequence(R.id.date, "setFormat12Hour", dateFormat);
       	remoteViews.setCharSequence(R.id.date, "setFormat24Hour", dateFormat);

		// 将AppWidgetProvider的子类包装成ComponentName对象
		ComponentName componentName = new ComponentName(context, Weather.class);
		// 调用AppWidgetManager将remoteViews添加到ComponentName中
		appwidetmanger.updateAppWidget(componentName, remoteViews);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastrecevier);
	}

	public void sendhttpgetweatherinfo(String woeidString) {
		String getData = null;
		Log.i("lllllllllllllllllllll", "sendhttpgetweatherinfo ");
		String stringtttt = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid="
				+ woeidString + "%20and%20u=%22c%22&format=json";

		try {

			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = MyHttpClient.getNewHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					5000);// 连接超时设置

			HttpGet httpGet = new HttpGet(stringtttt);

			HttpResponse httpResponse = httpClient.execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				HttpEntity entity = httpResponse.getEntity();
				getData = EntityUtils.toString(entity, "utf-8");
				Log.i("lllllllllllllllllllll", "get  getdata ");
				parseJSONWith(getData, woeidString);

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
	}

	// JSon解析
	public void parseJSONWith(String jsondata, String woeid1111) {
		if (weatherinformation.size() != 0) {
			Log.i("ttttzhaoyi_log", "weatherinformation.size  "
					+ weatherinformation.size());
			weatherinformation.clear();
			Log.i("ttttzhaoyi_log", "weatherinformation.size222222  "
					+ weatherinformation.size());
		}

		try {
			JSONObject object = new JSONObject(jsondata);
			// JSONObject
			// ciji=object.getJSONObject("query").getJSONObject("results").getJSONObject("item").getJSONObject("condition");
			String date_simple = (String) object.getJSONObject("query").get("created");
			JSONObject channel = object.getJSONObject("query")
					.getJSONObject("results").getJSONObject("channel");

			String string = channel.getString("title");
			//获取上一次更新时间
			String lastBuildDate = channel.getString("lastBuildDate");
			Log.d("wchao lastBuildDate", "lastBuildDate--->"+lastBuildDate);
			String city = channel.getJSONObject("location").getString("city");
			weatherinformation.add(city);
			//获得到天气的能见度
			String vistbility = channel.getJSONObject("atmosphere").getString("visibility");
			//获得日出日落时间
			String sunrise = channel.getJSONObject("astronomy").getString("sunrise");
			String sunset = channel.getJSONObject("astronomy").getString("sunset");
			//打印得到的数据
			Log.i("zhaoyi_log1223", "nengjiandu " + vistbility + "sunrise   " + sunrise + "sunset  " + sunset);
			Log.i("women", "city     " + city);
			//getdate
			String date = channel.getJSONObject("item").getJSONObject("condition").getString("date");
			Log.d("wchao date", date+"=====3333333333");
			
			String temp = channel.getJSONObject("item")
					.getJSONObject("condition").getString("temp");
			
			weatherinformation.add(temp);
			String weather = channel.getJSONObject("item")
					.getJSONObject("condition").getString("text");
			weatherinformation.add(weather);
			String code = channel.getJSONObject("item")
					.getJSONObject("condition").getString("code");
			weatherinformation.add(code);
			Log.i("zhaoyi_log1124", "woeid   " + woeid1111 + "  trueorfalse  " + Sqlitedatabase.selectwoeidifexit(woeid1111));
			Log.i("llllllllllllllll", "此woeid是否存在  " + Sqlitedatabase.selectwoeidifexit(woeid1111));
			if (!Sqlitedatabase.selectwoeidifexit(woeid1111)) {
				Log.i("zhaoyi_log1120", "woeid   " + woeid1111);

				JSONArray forecastweatherArray = channel.getJSONObject("item")
						.getJSONArray("forecast");

				// cityweathers = new ArrayList<Cityweather>();
				// if (cityweathers.size() != 0) {
				// cityweathers.clear();
				// }
				String maxfid = Sqlitedatabase.Maxfid();
				if (("".equals(maxfid)) || (maxfid == null)) {
					maxfid = "0";
				} else {
					maxfid = String.valueOf(Integer.parseInt(maxfid) + 1);
				}
				Log.d("wchao date ---->", "wchao 99999 date_simple"+date);
				Sqlitedatabase.insertwoeid(maxfid, woeid, city, 0, temp,weather, sunrise, sunset, vistbility, code,date,lastBuildDate);
				Log.i("lllllllllllllll", "新城市是否插入数据库");
				for (int i = 0; i < 5; i++) {
					String maxweatherid = Sqlitedatabase.Maxweatherid();
					Log.i("maxweatherid", "maxweatheridtttttt  " + maxweatherid);
					if (("".equals(maxweatherid)) || (maxweatherid == null)) {
						maxweatherid = "0";
					} else {
						maxweatherid = String.valueOf(Integer
								.parseInt(maxweatherid) + 1);

						Log.i("maxweatherid", "maxweatherid  " + maxweatherid);
					}
					String weekth = forecastweatherArray.getJSONObject(i)
							.get("day").toString();
					
					/*
					 * highTemp
					 * */
					String forecasehightemp = forecastweatherArray.getJSONObject(i).get("high").toString();
					String forecaselowtemp = forecastweatherArray.getJSONObject(i).get("low").toString();
					
					String forecasetemp = forecastweatherArray.getJSONObject(i)
							.get("high").toString()
							+ "°C ~"
							+ forecastweatherArray.getJSONObject(i).get("low")
							.toString();
					String weatherdata = forecastweatherArray.getJSONObject(i)
							.get("text").toString();
					String codeString = forecastweatherArray.getJSONObject(i)
							.get("code").toString();
					
					/*
					Sqlitedatabase.insertweathdata(new Dataforecast(
							maxweatherid, woeid, weekth, forecasetemp,
							weatherdata, codeString));
					*/		
					
					Sqlitedatabase.insertweathdata(new Dataforecast(
							maxweatherid, woeid, weekth, forecasehightemp,
							forecaselowtemp, codeString));
				}
				Intent intentlocation = new Intent();
				intentlocation.setAction("com.zhanfan.againlocation");
				context.sendBroadcast(intentlocation);

			} else {
				//Toast.makeText(this, "The city already exists!!! ",
				//		Toast.LENGTH_LONG).show();
			}

			handler.sendEmptyMessage(0x01);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.i("lllllllllllllllllllll", "Handler()");
			if (msg.what == 0x01) {
				Log.i("lllllllllllllllllllll", "0x01");
				if (weatherinformation.size() != 0) {
					String city = weatherinformation.get(0);
					String temp = weatherinformation.get(1);
					String weather = weatherinformation.get(2);
					String code = weatherinformation.get(3);
					Log.i("lllllllllllllllllllll", "temp     " + temp + "  city  "
							+ city + "   weather  " + weather + "code  " + code);
				}
				updateUI();

			} else if (msg.what == 0x02) {
				Log.i("lllllllllllllllllllll", "0x02   " + citynameString);
				// sendhttpgetweatherinfo();
				boolean bbb = (citynameString == null);
				Log.i("lllllllllllllllllllll", "是否是空   " + bbb);
				if (citynameString != null) {

					// sendhttpgetweatherinfo();
					threadgetwoeid(citynameString);
				} else {
					Log.i("lllllllllllllllllllll", "得到经纬度 ");
					getjingweidu();
				}

			} else if (msg.what == 0x03) {
				Log.i("lllllllllllllllllllll", "0x03   " + woeid);
				threadweather(woeid);
			} else if (msg.what == 0x04) {
				// threadgetlocationcityname();
				if (Sqlitedatabase.selectcitydefaultboolen() != null) {//判断
					String defaultwoeid = Sqlitedatabase
							.selectcitydefaultboolen().getDefaultwoeid();
					Log.i("lllllllllllllllllllll", "shujuku huode "
							+ defaultwoeid);
					threadweather(defaultwoeid);
				} else {
					getjingweidu();
				}

			}
		}

		;
	};

	/**
	 * 返回查询条件
	 *
	 * @return
	 */
	/*
	 * private Criteria getCriteria() { Criteria criteria = new Criteria(); //
	 * 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
	 * criteria.setAccuracy(Criteria.ACCURACY_FINE); // 设置是否要求速度
	 * criteria.setSpeedRequired(false); // 设置是否允许运营商收费
	 * criteria.setCostAllowed(false); // 设置是否需要方位信息
	 * criteria.setBearingRequired(false); // 设置是否需要海拔信息
	 * criteria.setAltitudeRequired(false); // 设置对电源的需求
	 * criteria.setPowerRequirement(Criteria.POWER_LOW); return criteria; }
	 *//**
	 * 实时更新文本内容
	 *
	 * @param location
	 */
	/*
	 * private void updateView(Location location) { if (location != null) { //
	 * editText.setText("设备位置信息\n\n经度："); //
	 * editText.append(String.valueOf(location.getLongitude())); //
	 * editText.append("\n纬度："); //
	 * editText.append(String.valueOf(location.getLatitude())); jingduString =
	 * String.valueOf(location.getLongitude()); weiduString =
	 * String.valueOf(location.getAltitude()); double jingdu =
	 * location.getLongitude(); double weidu = location.getLatitude();
	 * Log.i("zhanfan_log", "jingdu   " + jingdu + "   weidu  " + weidu); } else
	 * { // 清空EditText对象 // editText.getEditableText().clear(); } }
	 * 
	 * // 状态监听 GpsStatus.Listener listener = new GpsStatus.Listener() { public
	 * void onGpsStatusChanged(int event) { switch (event) { // 第一次定位 case
	 * GpsStatus.GPS_EVENT_FIRST_FIX: Log.i("zhanfan_log", "第一次定位"); break; //
	 * 卫星状态改变 case GpsStatus.GPS_EVENT_SATELLITE_STATUS: // Log.i("zhanfan_log",
	 * "卫星状态改变"); // 获取当前状态 GpsStatus gpsStatus =
	 * locationManager.getGpsStatus(null); // 获取卫星颗数的默认最大值 int maxSatellites =
	 * gpsStatus.getMaxSatellites(); // 创建一个迭代器保存所有卫星 Iterator<GpsSatellite>
	 * iters = gpsStatus.getSatellites() .iterator(); int count = 0; while
	 * (iters.hasNext() && count <= maxSatellites) { GpsSatellite s =
	 * iters.next(); count++; } System.out.println("搜索到：" + count + "颗卫星");
	 * break; // 定位启动 case GpsStatus.GPS_EVENT_STARTED: Log.i("zhanfan_log",
	 * "定位启动"); break; // 定位结束 case GpsStatus.GPS_EVENT_STOPPED:
	 * Log.i("zhanfan_log", "定位结束"); break; } }; };
	 * 
	 * // 位置监听 private LocationListener locationListener = new
	 * LocationListener() {
	 *//**
	 * 位置信息变化时触发
	 */
	/*
	 * public void onLocationChanged(Location location) { updateView(location);
	 * Log.i("zhanfan_log", "时间：" + location.getTime()); Log.i("zhanfan_log",
	 * "经度：" + location.getLongitude()); Log.i("zhanfan_log", "纬度：" +
	 * location.getLatitude()); Log.i("zhanfan_log", "海拔：" +
	 * location.getAltitude()); }
	 *//**
	 * GPS状态变化时触发
	 */
	/*
	 * public void onStatusChanged(String provider, int status, Bundle extras) {
	 * switch (status) { // GPS状态为可见时 case LocationProvider.AVAILABLE:
	 * Log.i("zhanfan_log", "当前GPS状态为可见状态"); break; // GPS状态为服务区外时 case
	 * LocationProvider.OUT_OF_SERVICE: Log.i("zhanfan_log", "当前GPS状态为服务区外状态");
	 * break; // GPS状态为暂停服务时 case LocationProvider.TEMPORARILY_UNAVAILABLE:
	 * Log.i("zhanfan_log", "当前GPS状态为暂停服务状态"); break; } }
	 *//**
	 * GPS开启时触发
	 */
	/*
	 * public void onProviderEnabled(String provider) { Location location =
	 * locationManager.getLastKnownLocation(provider); updateView(location); }
	 */

	/**
	 * GPS禁用时触发
	 */
	/*
	 * public void onProviderDisabled(String provider) { updateView(null); }
	 * 
	 * };
	 */
	// 获得当地城市的名字
	public void getlocationcityname(String jingdu, String weidu) {
		// 31.20124 121.515695 jingdu 121.520218 weidu 31.198527

		Log.i("lllllllllllllllllllll", "jingdu " + jingdu + "  weidu  " + weidu);
		String locationaddr = "http://maps.google.cn/maps/api/geocode/json?latlng="
				+ weidu + "," + jingdu + "&sensor=true&language=en-US";
		try {

			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = MyHttpClient.getNewHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					5000);// 连接超时设置

			HttpGet httpGet = new HttpGet(locationaddr);

			HttpResponse httpResponse = httpClient.execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				HttpEntity entity = httpResponse.getEntity();
				String getDatacity = EntityUtils.toString(entity, "utf-8");
				Log.i("lllllllllllllllllllll", "zhaoyicity " + getDatacity);
				jsonforcityname(getDatacity);

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

	}

	// 解析城市名字
	public void jsonforcityname(String jString) {

		try {
			JSONObject object = new JSONObject(jString);

			JSONArray jsonArray = object.getJSONArray("results");

			JSONObject ob11 = jsonArray.getJSONObject(0);

			JSONArray arry2 = ob11.getJSONArray("address_components");
			JSONObject object2 = arry2.getJSONObject(3);

			citynameString = object2.getString("long_name");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//获得城市名字之后，发送消息，让系统开启新线程获取改城市的天气信息
		handler.sendEmptyMessage(0X02);

	}

	public void threadgetwoeid(final String cityname) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				Log.i("lllllllllllllllllllll", "threadgetwoeid(   ");
				getcitywoeid(cityname);
				// queryCityInfo(cityname,Locale.getDefault().getLanguage());
			}
		}).start();
	}

	public static int countStr(String str1, String str2) {
		int counter = 0;
		if (str1.indexOf(str2) == -1) {
			return 0;
		} else if (str1.indexOf(str2) != -1) {
			counter++;
			countStr(str1.substring(str1.indexOf(str2) + str2.length()), str2);
			return counter;
		}
		return 0;
	}

	// 获得城市的woeid
	public void getcitywoeid(String cityname) {
		Log.i("lllllllllllllllllllll", "getcitywoeid(cityname   " + cityname);
		if (cityname.contains(" ")) {
			// int count=countStr(cityname,"");

			Log.i("lllllllllllllllllllll", " 含有空格");
			// int ad=cityname.substring(start);
			int ad = cityname.indexOf(" ");
			cityname = cityname.substring(0, ad) + "%20"
					+ cityname.substring(ad + 1, cityname.length());
			Log.i("lllllllllllllllllllll", " 含有空格ad " + ad + "   ttt  "
					+ cityname);
		}

		String getwoeidaddr = "https://search.yahoo.com/sugg/gossip/gossip-gl-location/?appid=weather&output=sd1&p2=cn,t,pt,z&lc=en-US&command="
				+ cityname;
		String getwoeidbyxml = "http://query.yahooapis.com/v1/public/yql?q=SELECT%20woeid,name,country,admin1,admin2,timezone%20FROM%20geo.places%20WHERE%20text=%22"
				+ cityname + "%22%20and%20lang=%22en-us%22";

		String resultData = "";// 获得的数据
		URL url = null;
		try {
			// 构造一个URL对象
			url = new URL(getwoeidbyxml);
		} catch (MalformedURLException e) {
			Log.e("llllll", "MalformedURLException");
		}
		try {
			// 使用HttpURLConnection打开连接
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			Log.e("llllll", "HttpURLConnection");
			// 得到读取的内容(流)
			InputStreamReader in = new InputStreamReader(
					urlConn.getInputStream());
			Log.e("llllll", "InputStreamReader");
			// 为输出创建BufferedReader
			BufferedReader buffer = new BufferedReader(in);
			Log.e("llllll", "BufferedReader");
			String inputLine = null;
			// 使用循环来读取获得的数据
			Log.e("llllll", "inputLine");
			while (((inputLine = buffer.readLine()) != null)) {
				Log.e("llllll", "inputLinettttttttt");
				// 我们在每一行后面加上一个"\n"来换行
				resultData += inputLine + "\n";
			}
			in.close();
			urlConn.disconnect();
			Log.e("llllll", "iurlConn.disconnect()");
			// 设置显示取得的内容
			if (resultData != null) {
				Log.e("llllll", "resultData != null");
				xmlwoeid(resultData);// 解析XML
				Log.e("llllll", "xmlwoeid(resultData)");
			}

		} catch (IOException e) {
			Log.e("llllll", "IOException" + e.toString());
		}

	}

	// XML解析出得到woeid
	public void xmlwoeid(String buffer) {
		List<String> woeidlStrings = null;
		XmlPullParser xmlParser = Xml.newPullParser();// 获得XmlPullParser解析器

		ByteArrayInputStream tInputStringStream = null;
		if (buffer != null && !buffer.trim().equals("")) {
			tInputStringStream = new ByteArrayInputStream(buffer.getBytes());
		} else {
			return;
		}

		try {
			woeidlStrings = new ArrayList<String>();
			Log.i("llllllllllllllll", "length " + woeidlStrings.size());
			if (woeidlStrings.size() != 0) {
				Log.i("llllllllllllllll", "kkkkkkkkkkkkkkkkkkkkk");
				woeidlStrings.clear();
			}
			// 得到文件流，并设置编码方式
			// InputStream
			// inputStream=mContext.getResources().getAssets().open(fileName);
			// xmlParser.setInput(inputStream, "utf-8");
			xmlParser.setInput(tInputStringStream, "UTF-8");

			// 获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
			int evtType = xmlParser.getEventType();

			while (evtType != XmlPullParser.END_DOCUMENT)// 一直循环，直到文档结束
			{
				switch (evtType) {
					case XmlPullParser.START_TAG:
						String tag = xmlParser.getName();
						// Log.i("lllllllllllllllllllll","tag = "+tag);
						if ("woeid".equals(tag)) {
							String woeid = xmlParser.nextText();

							woeidlStrings.add(woeid);

							Log.i("lllllllllllllllllllll", "woeid = " + woeid);
						}

						// 如果是city标签开始，则说明需要实例化对象了
						break;

					case XmlPullParser.END_TAG:
						// 标签结束
					default:
						break;
				}
				// 如果xml没有结束，则导航到下一个节点
				evtType = xmlParser.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		woeid = woeidlStrings.get(0);
		Log.i("lllllllllllllllllllll", "woeidttttttttt  " + woeid);
		// threadweather(woeidst.get(0));
		handler.sendEmptyMessage(0x03);
	}

	public void threadweather(final String woeid) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				Log.i("lllllllllllllllllllll", "得到weather线程  ");
				sendhttpgetweatherinfo(woeid);
			}

		}).start();
	}


	public void startClockListener() {

		IntentFilter localIntentFilter = new IntentFilter();
		localIntentFilter.addAction("android.intent.action.TIME_TICK");
		localIntentFilter.addAction("android.intent.action.TIME_CHANGED");
		localIntentFilter.addAction("android.intent.action.TIME_SET");
		registerReceiver(this.timeReceiver, localIntentFilter);
	}
	private BroadcastReceiver timeReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {

			if ("android.intent.action.TIME_TICK".equals(intent.getAction())
					|| "android.intent.action.TIME_CHANGED".equals(intent.getAction())
					|| "android.intent.action.TIME_SET".equals(intent.getAction())) {
				ConnectivityManager con = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
				NetworkInfo mNetworkInfo = con.getActiveNetworkInfo();
				if (mNetworkInfo != null) {
					//执行相关操作
					Intent  intentclick =new Intent(clickactiondesk);
					PendingIntent pendingIntent=PendingIntent.getBroadcast(context, 0, intentclick,flag);
					remoteViews.setOnClickPendingIntent(R.id.linerlayouthour, pendingIntent);
			
					Intent intentclockIntent = new Intent(clickaction);
					PendingIntent pendingIntent_weather=PendingIntent.getBroadcast(context, 0, intentclockIntent,flag);
					remoteViews.setOnClickPendingIntent(R.id.linerlayout_weather, pendingIntent_weather);

					ComponentName componentName = new ComponentName(context, Weather.class);
					// 调用AppWidgetManager将remoteViews添加到ComponentName中
					appwidetmanger.updateAppWidget(componentName, remoteViews);
				} else {

					//执行相关操作
					Intent  intentclick =new Intent(clickactiondesk);
					PendingIntent pendingIntent=PendingIntent.getBroadcast(context, 0, intentclick,flag);
					remoteViews.setOnClickPendingIntent(R.id.linerlayouthour, pendingIntent);
			
					Intent intentclockIntent = new Intent(clickaction);
					PendingIntent pendingIntent_weather=PendingIntent.getBroadcast(context, 0, intentclockIntent,flag);
					remoteViews.setOnClickPendingIntent(R.id.linerlayout_weather, pendingIntent_weather);

					ComponentName componentName = new ComponentName(context, Weather.class);
					// 调用AppWidgetManager将remoteViews添加到ComponentName中
					appwidetmanger.updateAppWidget(componentName, remoteViews);
				}
			}

		}
	};
}
