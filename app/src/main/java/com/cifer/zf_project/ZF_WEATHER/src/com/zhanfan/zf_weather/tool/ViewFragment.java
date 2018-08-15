package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zhanfan.zf_weather.R;
import com.zhanfan.zf_weather.database.Sqlitedatabase;
import com.zhanfan.zf_weather.entity.Cityweather;
import com.zhanfan.zf_weather.entity.Dataforecast;
import com.zhanfan.zf_weather.entity.Nowtempandweather;

@SuppressLint("ValidFragment")
public class ViewFragment extends Fragment {
	private ProgressDialog pDialog;
	boolean update = false;
	ViewHolderAdapter viewHolderAdapter = null;
	TextView temp;// 当天温度
	TextView locationcity; // 当地城市名字
	TextView temphightolow;// 最高温度-最低温度
	TextView nowweather; // 当天天气情况
	TextView sunrise; // 日出时间
	TextView sunset; // 日落时间
	TextView visibility;// 能见度
	TextView tv_main_updatetime;
	ImageView weatherimage;// 显示天气情况的图片
	GridView listView;
	View view = null;
	Getnetdata getnetdata = null;
	Context context;
	int citydefault;
	int cityfid;
	String woeidString = null;
	String citysearch;
	List<Dataforecast> foreDataforecasts = null;
	Nowtempandweather nowtempandweathers = null;
	List<Cityweather> searchcity = null;

	private static final String ANDROID_CLOCK_FONT_FILE = "/system/fonts/Samsung_Clock.ttf";
	private static Typeface mClockTypeface = null;

	int[] background = new int[] { R.drawable.clear_d, R.drawable.clear_n,
			R.drawable.cloudy_d, R.drawable.cloudy_n, R.drawable.foggy_d,
			R.drawable.foggy_n, R.drawable.rain_d, R.drawable.rain_n,
			R.drawable.snow_d, R.drawable.snow_n, R.drawable.storm_d,
			R.drawable.storm_n, R.drawable.sunny_bg, R.drawable.wind_bg,
			R.drawable.zmohubg };
	int[] weatherpicture = new int[] { R.drawable.org3_ww0,
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

	public ViewFragment() {
		super();
	}

	public ViewFragment(int citydefault1, String woeid, String cityname,
			int cityfid) {

		super();
		Log.i("zhaoyi_log1124", "woeid  " + woeid);
		this.citydefault = citydefault1;
		this.woeidString = woeid;
		this.citysearch = cityname;
		this.cityfid = cityfid;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x10) {
				getweatherdata(woeidString);
			} else if (msg.what == 0x11) {
				Log.i("updateweatherinforamtion", "更新发消息");
				changelistview();
			}
		};
	};

	// 判断天气对应正确的图片
	public int getpictureid(String nowweathercode) {
		int code = Integer.parseInt(nowweathercode);
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

	//
	public int getlastpictureid(String codestring) {
		int code = Integer.parseInt(codestring);
		if ((code == 0) || (code == 1) || (code == 2) || (code == 3)
				|| (code == 4) || (code == 37) || (code == 38) || (code == 39)
				|| (code == 47)) {
			return 10;
		} else if ((code == 36) || (code == 34) || (code == 32)) {
			return 12;
		} else if ((code == 23) || (code == 24) || (code == 25)) {
			return 13;
		} else if ((code == 31)) {
			return 0;
		} else if ((code == 33)) {
			return 1;
		} else if ((code == 26) || (code == 28) || (code == 30) || (code == 44)) {
			return 2;
		} else if ((code == 27) || (code == 29)) {
			return 3;
		} else if ((code == 19) || (code == 20) || (code == 21) || (code == 22)) {
			return 4;
		} else if ((code == 9) || (code == 11) || (code == 12) || (code == 10)
				|| (code == 40) || (code == 45)) {
			return 6;
		} else if ((code == 5) || (code == 6) || (code == 7) || (code == 8)
				|| (code == 13) || (code == 14) || (code == 15) || (code == 16)
				|| (code == 17) || (code == 18) || (code == 35) || (code == 41)
				|| (code == 42) || (code == 43) || (code == 46)) {
			return 8;
		} else {
			return 0;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity().getApplicationContext();
		context.getSharedPreferences("weather_future_data", Context.MODE_PRIVATE);
		update = false;
		registreciver();
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = connectivityManager
				.getActiveNetworkInfo();

		citydefault = Sqlitedatabase.getlocation(cityfid);
		Log.i("zhaoyi_log1124", "cityfid  " + cityfid + "  citydefault "
				+ citydefault);
		// registreciver();
		view = inflater.inflate(R.layout.viewpateitem, container, false);
		innit();// 控件初始化

		// temp.setText("20℃");
		if(networkInfo != null && networkInfo.isAvailable()){
			handler.sendEmptyMessage(0x10);
		}
		Log.i("zhaoyi_log1124", "viewfragement   " + woeidString);
		foreDataforecasts = new ArrayList<Dataforecast>();
		nowtempandweathers = Sqlitedatabase.getnowtempandweather(woeidString);
		Log.d("wchao 000000nowtempandweathers", "----------------"+nowtempandweathers);
		foreDataforecasts = Sqlitedatabase.selectforecast(woeidString);
		Log.i("zhaoyi_log1124", "viewfragement111  ");
		if ((foreDataforecasts != null) && (nowtempandweathers != null)) {
			Log.i("zhaoyi_log1124", "viewfragement222221  ");
			locationcity.setText(citysearch);// 当前城市的名字
		//	temp.setTypeface(getClockTypeface());
			temp.setText(nowtempandweathers.getTemString()+"℃");// 当天的某时刻的温度
			Log.d("wchao 8888888888888888888", ""+foreDataforecasts.get(0).getTemp());
			//temphightolow.setText(foreDataforecasts.get(0).getTemp() + "℃ ~ "+foreDataforecasts.get(0).getWeatherdata()+"℃");// 当天最高温度到最低温度
			temphightolow.setText(nowtempandweathers.getNowdate().substring(0, 22));
			Log.d("wchao  000000foreDataforecasts.get(0).getTemp()   ", "foreDataforecasts.get(0).getTemp()--->"+foreDataforecasts.get(0).getTemp());
			sunrise.setText( nowtempandweathers.getSunrise());// 日出时间
			sunset.setText(nowtempandweathers.getSunset());// 日落时间
			nowweather.setText(nowtempandweathers.getWeatherinforString());// 当天天气情况
			visibility.setText(nowtempandweathers.getVisibility() + "km");// 当天天气可见度
			tv_main_updatetime.setText(nowtempandweathers.getUpdatetime().substring(0, 22));
			weatherimage.setBackgroundResource(weatherpicture[getpictureid(nowtempandweathers
							.getNowcode())]);// 天气情况对应的图片
			// Integer.parseInt(foreDataforecasts.get(0).getCode())
			Log.i("zhaoyi_log1124", "cityname  " + citysearch + " code     "
					+ getlastpictureid(foreDataforecasts.get(0).getCode()));
			view.setBackgroundResource(background[getlastpictureid(nowtempandweathers
					.getNowcode())]);// 天气情况对应的背景图片
			//直接读取本地数据
			
			if(foreDataforecasts.size() > 1){
				listView.setAdapter(new ViewHolderAdaptersqlite(context,foreDataforecasts));
			}
			//listView.setAdapter(new ViewHolderAdapter(context, searchcity));
		} else {
			Log.i("zhaoyi_log1124", "viewfragement333333  ");
			handler.sendEmptyMessage(0x10);
			/*
			 * Sqlitedatabase.createtablewoeid();
			 * Sqlitedatabase.ceatetableweather(); foreDataforecasts =
			 * Sqlitedatabase.selectforecast(woeidString); boolean zhenjia =
			 * (foreDataforecasts == null); Log.i("zhaoyi_log1124", "zhenjia  "
			 * + zhenjia); locationcity.setText(citysearch);
			 * listView.setAdapter(new ViewHolderAdaptersqlite(context,
			 * foreDataforecasts));
			 */
			getActivity().finish();// 如果数据为空，就直接退出程序

		}

		return view;
	}

	public void innit() {
		locationcity = (TextView) view.findViewById(R.id.location);// 当前城市的名字
		listView = (GridView) view.findViewById(R.id.listView1);// 界面listview
		/**
		 * add wchao
		 * 禁止GridView的滑动
		 * */
		listView.setOnTouchListener(new OnTouchListener() {

               @Override
               public boolean onTouch(View v, MotionEvent event) {
                    return MotionEvent.ACTION_MOVE == event.getAction() ? true
                              : false;
               }
          });

		temp = (TextView) view.findViewById(R.id.daytemp);// 当天的温度
		temphightolow = (TextView) view.findViewById(R.id.temphightolow);// 当天最高温度到最低温度
		nowweather = (TextView) view.findViewById(R.id.dayweather);// 当天天气情况
		sunrise = (TextView) view.findViewById(R.id.sunraise);// 日出时间
		sunset = (TextView) view.findViewById(R.id.sunset);// 日落时间
		visibility = (TextView) view.findViewById(R.id.nengjiandu);// 能见度
		weatherimage = (ImageView) view.findViewById(R.id.weatherimage);// 当天天气情况对应的图片
		tv_main_updatetime = (TextView) view.findViewById(R.id.tv_main_updatetime);

	}

	public void changelistview() {
		Log.i("viewdatadata", "citysearch----tttttttt  " + citysearch);
		Log.i("dialog_zhaoyi", "pDialog----tttttttt  " + (pDialog==null));
		 if(pDialog!=null){
		 pDialog.dismiss();
		 }
		/*if (update) {
			Toast.makeText(context, "Update Success!!!!", Toast.LENGTH_SHORT)
					.show();
		}*/
		Log.i("updateweatherinforamtion", "更新信息了，请注意");
		// 滑动到当前页面：如果有网络或者手动点击刷新按钮更新城市信息
		// start
		locationcity.setText(citysearch);// 当前城市的名字
		//temp.setTypeface(getClockTypeface());
		temp.setText(nowtempandweathers.getTemString()+"℃");// 当天的某时刻的温度
		//	temphightolow.setText(foreDataforecasts.get(0).getTemp() + "℃ ~ "+foreDataforecasts.get(0).getWeatherdata()+"℃");// 当天最高温度到最低温度
		// 截取字符串 String a = s.substring(8, 22);
		temphightolow.setText(nowtempandweathers.getNowdate().substring(0, 22));
		sunrise.setText(nowtempandweathers.getSunrise());// 日出时间
		sunset.setText(nowtempandweathers.getSunset());// 日落时间
		nowweather.setText(nowtempandweathers.getWeatherinforString());// 当天天气情况
		visibility.setText( nowtempandweathers.getVisibility()+ "km");// 当天天气可见度
		tv_main_updatetime.setText(nowtempandweathers.getUpdatetime().substring(0, 22));
		weatherimage.setBackgroundResource(weatherpicture[getpictureid(nowtempandweathers
						.getNowcode())]);// 天气情况对应的图片
		// Integer.parseInt(foreDataforecasts.get(0).getCode())
		Log.i("zhaoyi_log1124", "cityname  " + citysearch + " code     "
				+ getlastpictureid(foreDataforecasts.get(0).getCode()));
		view.setBackgroundResource(background[getlastpictureid(nowtempandweathers
				.getNowcode())]);// 天气情况对应的背景图片
		// end
		listView.setAdapter(new ViewHolderAdapter(context, searchcity));
	}

	// 开启一条线程获取天气信息
	public void getweatherdata(final String woeid) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sendhttpgetweatherinfo(woeid);
			}
		}).start();
	}

	public void sendhttpgetweatherinfo(String woeidString) {
		String getData = null;
		Log.i("lllllllllllllllllllll", "sendhttpgetweatherinfo ");
		String stringtttt = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid="
				+ woeidString + "%20and%20u=%22c%22&format=json";

		// 此处使用Volley 提高网络数据访问的效率
		// 1.首先需要获取到一个RequestQueue对象
		RequestQueue mQueue = Volley.newRequestQueue(context);
		// 注意这里拿到的RequestQueue是一个请求队列对象，它可以缓存所有的HTTP请求，然后按照一定的算法并发地发出这些请求。
		// RequestQueue内部的设计就是非常合适高并发的，因此我们不必为每一次HTTP请求都创建一个RequestQueue对象，
		// 这是非常浪费资源的，基本上在每一个需要和网络交互的Activity中创建一个RequestQueue对象就足够了。
		
		// 2.发出一条HTTP请求，还需要创建一个StringRequest对象，
		//这里new出了一个StringRequest对象，StringRequest的构造函数需要传入三个参数，第一个参数就是目标服务器的URL地址，
		//第二个参数是服务器响应成功的回调，第三个参数是服务器响应失败的回调
		StringRequest request = new StringRequest(stringtttt,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
                     Log.i("zhaoyi_logvolley","logvolley0000  "+response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.i("zhaoyi_logvolley","VolleyError000  "+error);
					}
				});
		
		JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(stringtttt, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("zhaoyi_logvolley","logvolley11111  "+response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.i("zhaoyi_logvolley","VolleyError11111  "+error);
			}
		});
		
		//3.最后将StringRequest添加到mQueue中去
		   mQueue.add(jsonObjectRequest);
		

		try {

			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = MyHttpClient.getNewHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					5000);// 脕卢艙脫鲁卢脢卤脡猫脰脙

			HttpGet httpGet = new HttpGet(stringtttt);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			Log.i("wangluorequest",
					"httpResponse  " + httpResponse.getStatusLine());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				HttpEntity entity = httpResponse.getEntity();
				getData = EntityUtils.toString(entity, "utf-8");
				Log.i("lllllllllllllllllllll", "得到数据天气信息 ");
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

	public List<Cityweather> parseJSONWith(String jsondata, String woeidString) {
		// if (weatherinformation.size() != 0) {
		// Log.i("ttttzhaoyi_log", "weatherinformation.size  "
		// + weatherinformation.size());
		// weatherinformation.clear();
		// Log.i("ttttzhaoyi_log", "weatherinformation.size222222  "
		// + weatherinformation.size());
		// }
		searchcity = null;
		searchcity = new ArrayList<Cityweather>();
		if (searchcity.size() != 0) {
			searchcity.clear();
			citysearch = null;
		}
		try {
			Log.i("lllllllllllllllllllll", "开始解析天气数据 ");
			JSONObject object = new JSONObject(jsondata);
			// JSONObject
			// ciji=object.getJSONObject("query").getJSONObject("results").getJSONObject("item").getJSONObject("condition");
			String date_simple = (String) object.getJSONObject("query").get("created");
			JSONObject channel = object.getJSONObject("query")
					.getJSONObject("results").getJSONObject("channel");

			String string = channel.getString("title");
			String lastBuildDate = channel.getString("lastBuildDate");
			citysearch = channel.getJSONObject("location").getString("city");
			Log.i("viewdatadata", "citysearch  " + citysearch);
			// weatherinformation.add(city);
			Log.i("women", "city     " + citysearch);
			String temp = channel.getJSONObject("item")
					.getJSONObject("condition").getString("temp");

			// weatherinformation.add(temp);
			String weather = channel.getJSONObject("item")
					.getJSONObject("condition").getString("text");
			Log.i("llllllllllllll", "weatherttttttttttttt   " + weather
					+ "  city   " + citysearch);
			// update nowtemp and nowweather
			Log.i("llllllllllllll", "temp  " + temp + "  weather  " + weather
					+ "  citysearch  " + citysearch);
			// 获得到天气的能见度
			String vistbility = channel.getJSONObject("atmosphere").getString(
					"visibility");
			// 获得日出日落时间
			String sunrise = channel.getJSONObject("astronomy").getString(
					"sunrise");
			String sunset = channel.getJSONObject("astronomy").getString(
					"sunset");
			String nowcode = channel.getJSONObject("item")
					.getJSONObject("condition").getString("code");
			String nowdate = channel.getJSONObject("item")
					.getJSONObject("condition").getString("date");
			Log.d("wchao nowdate", "wchao date--->"+nowdate);
			// 打印得到的数据
			Log.i("llllllllllllll", "nengjiandu " + vistbility + "sunrise   "
					+ sunrise + "sunset  " + sunset);
			// 更新数据库里的数据
			Sqlitedatabase.updatenowtemandweather(woeidString, temp, weather,
					sunrise, sunset, vistbility, nowcode,nowdate,lastBuildDate);
			Log.i("llllllllllllll", "数据更新成功");
			// weatherinformation.add(weather);
			String code = channel.getJSONObject("item")
					.getJSONObject("condition").getString("code");
			// weatherinformation.add(code);
			JSONArray forecastweatherArray = channel.getJSONObject("item")
					.getJSONArray("forecast");

			Cityweather cityweatherenty = null;
			List<String> list = Sqlitedatabase.selectgetweatherid(woeidString);
			for (int i = 0; i < 5; i++) {
				cityweatherenty = new Cityweather();
				cityweatherenty.setCode(forecastweatherArray.getJSONObject(i)
						.get("code").toString());
				cityweatherenty.setDay(forecastweatherArray.getJSONObject(i)
						.get("day").toString());
				cityweatherenty.setHightemp(forecastweatherArray
						.getJSONObject(i).get("high").toString());
				cityweatherenty.setLowtemp(forecastweatherArray
						.getJSONObject(i).get("low").toString());
				cityweatherenty.setWeathertext(forecastweatherArray
						.getJSONObject(i).get("text").toString());
				Log.i("llllllllllllll", "weatherttttlllllllll   "
						+ forecastweatherArray.getJSONObject(i).get("text")
								.toString());
				// Sqlitedatabase.upda
				if (list != null) {
					if ((list.size() >= 5) && (list.get(i) != null)) {
						// 更新数据库里的数据
						/*Sqlitedatabase.updateweatherdata(new Dataforecast(list
								.get(i), forecastweatherArray.getJSONObject(i)
								.get("day").toString(), forecastweatherArray
								.getJSONObject(i).get("high").toString()
								+ "℃ ~"
								+ forecastweatherArray.getJSONObject(i)
										.get("low").toString(),
								forecastweatherArray.getJSONObject(i)
										.get("text").toString(),
								forecastweatherArray.getJSONObject(i)
										.get("code").toString()));*/
						
						Sqlitedatabase.updateweatherdata(new Dataforecast(list
								.get(i), forecastweatherArray.getJSONObject(i)
								.get("day").toString(), forecastweatherArray
								.getJSONObject(i).get("high").toString()
								,forecastweatherArray.getJSONObject(i)
										.get("low").toString(),
								forecastweatherArray.getJSONObject(i)
										.get("code").toString()));
					}
				}

				searchcity.add(cityweatherenty);
			}
			// Log.i("lllllllllllllllllllll",
			// "cityweathers " + cityweathers.size());
			// Log.i("lllllllllllllllllllll", "艙芒脦枚鲁枚脤矛脝酶脢媒鸥脻 ");
			handler.sendEmptyMessage(0x11);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return searchcity;
	}

	BroadcastReceiver updataReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			 showProgressDialog("Just a moment, please!!");
			Log.i("zhaoyilog_log_1207", "onReceive(");
			update = true;
			handler.sendEmptyMessage(0x10);
		}

	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		context.unregisterReceiver(updataReceiver);
	}

	 private void showProgressDialog(String title)
	 {
	 Log.i("zy_log", "555555");
	 pDialog = new ProgressDialog(getActivity());
	 pDialog.setCancelable(true);
	 pDialog.setMessage(title+"...");
	 pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	 pDialog.show();
	 Log.i("zy_log", "666666");
	 }
	public void registreciver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.zhanfan.updateweatherdata");
		context.registerReceiver(updataReceiver, intentFilter);
	}


	private Typeface getClockTypeface() {
		if (mClockTypeface == null) {
			mClockTypeface = Typeface.createFromFile(ANDROID_CLOCK_FONT_FILE);
		}
		return mClockTypeface;
	}

	

}
