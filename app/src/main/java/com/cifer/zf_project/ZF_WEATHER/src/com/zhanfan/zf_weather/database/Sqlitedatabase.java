package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.database;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

import com.zhanfan.zf_weather.entity.Cityandwoeid;
import com.zhanfan.zf_weather.entity.Dataforecast;
import com.zhanfan.zf_weather.entity.Defaultcity;
import com.zhanfan.zf_weather.entity.Nowtempandweather;

import android.R.integer;
import android.app.LocalActivityManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.InflateException;

public class Sqlitedatabase {
	static SQLiteDatabase sld;
	static String sql;
	static Cursor cur;

	public static void createtablewoeid() {
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			// primary key, citywoeid, city
			sql = "create table if not exists woeidtable"
					+ "("
					+ "fid INTEGER primary key,"
					+ "citywoeid varchar(20),"
					+ "city varchar(20),"
					+ "location INTEGER,"
					+ "nowtemp varchar(20),nowweather varchar(20),sunrise varchar(20),sunset varchar(20),visiblity varchar(20),nowcode varchar(20),nowdate varchar(100),updatetime varchar(100));";
			Log.i("sqlitedata", "00000000000");
			sld.execSQL(sql);
			Log.i("sqlitedata", "11111111111");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("sqlitedata", "00000eeeeeeeeeeee " + e.toString());
			e.printStackTrace();
		}
	}
	// create weatherdata
	public static void ceatetableweather() {
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", //
					null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);//
			// citywoeid weekth temp weather code
			sql = "create table if not exists weatherinformation" + "("
					+ "citywoeid char(20)," + "weatherid INTEGER primary key,"
					+ "weekth varchar(20)," + "temp varchar(20),"
					+ "weather varchar(20)," + "code varchar(20)" + ");";
			Log.i("sqlitedata", "22222222222");
			sld.execSQL(sql);
			Log.i("sqlitedata", "3333333333");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("sqlitedata", "2222222222eeeeeeeeeeee " + e.toString());
			e.printStackTrace();
		}
	}

	// get max weatherid
	public static String Maxweatherid() {
		String maxid = "";

		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "select max(weatherid) from weatherinformation";
			cur = sld.rawQuery(sql, null);
			while (cur.moveToNext()) {
				maxid = cur.getString(/* cur.getColumnIndex("weatherid") */0);
				Log.i("sqlitedata", "zhaoyi3333333" + maxid + "zhaoyi");
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("sqlitedata", "zhaoyi_zhaoyi" + maxid + "zhaoyi");
		return maxid;
	}

	// get max fid
	public static String Maxfid() {
		String maxfid = "";
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "select max(fid) from woeidtable";
			cur = sld.rawQuery(sql, null);
			while (cur.moveToNext()) {
				maxfid = cur.getString(/* cur.getColumnIndex("fid") */0);

			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return maxfid;
	}

	// get location
	public static int getlocation(int fid) {
		int location = 0;
		Log.i("zhaoyi_log1124", "getlocation0000  " + location);
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "select location from woeidtable where fid=" + fid + ";";
			cur = sld.rawQuery(sql, null);

			if (!cur.moveToNext()) {

				location = 0;
				Log.i("zhaoyi_log1124", "getlocation111111  " + location);
				cur.close();

			} else if (cur.moveToFirst()) {
				// cur.moveToPrevious();
				location = cur.getInt(0);
				Log.i("zhaoyi_log1124", "getlocation222222222  " + location);
				cur.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("zhaoyi_log1124", "yichang  " + e.toString());
			e.printStackTrace();
		}
		return location;
	}

	// get nowtemp,weaher
	public static Nowtempandweather getnowtempandweather(String woeidString) {
		Nowtempandweather nowtempandweather = null;
		Log.i("zhaoyi_log1124", "getlocation0000  " + woeidString);
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "select nowtemp,nowweather,sunrise,sunset,visiblity,nowcode,nowdate,updatetime from woeidtable where citywoeid="
					+ woeidString + ";";
			cur = sld.rawQuery(sql, null);

			if (!cur.moveToNext()) {
				cur.close();

			} else if (cur.moveToFirst()) {
				// cur.moveToPrevious();
				String nowtem = cur.getString(0);
				String nowweather = cur.getString(1);
				String sunrise=cur.getString(2);
				String sunset=cur.getString(3);
				String visiblity=cur.getString(4);
				String nowcode=cur.getString(5);
				String nowdate = cur.getString(6);
				String updatetime = cur.getString(7);
				nowtempandweather = new Nowtempandweather(nowtem, nowweather,sunrise,sunset,visiblity,nowcode,nowdate,updatetime);
				cur.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("zhaoyi_log1124", "yichang  " + e.toString());
			e.printStackTrace();
		}
		return nowtempandweather;
	}

	public static void insertwoeid(String fid, String cityweid, String city,
			int location, String nowtemp, String nowweather, String sunrise,
			String sunset, String visibility,String nowcode,String nowdate,String updatetime) {
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "insert into woeidtable values('" + fid + "','" + cityweid
					+ "','" + city + "','" + location + "','" + nowtemp + "','"
					+ nowweather + "','" + sunrise + "','" + sunset + "','"
					+ visibility + "','"+nowcode+ "','"+nowdate+"','"+updatetime+"');";
			sld.execSQL(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void insertweathdata(Dataforecast data) {
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "insert into weatherinformation values('"
					+ data.getCitywoeid() + "','" + data.getWeatherid() + "','"
					+ data.getWeekth() + "','" + data.getTemp() + "','"
					+ data.getWeatherdata() + "','" + data.getCode() + "');";
			sld.execSQL(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// update cityweatherdata
	public static void updateweatherdata(Dataforecast dataforecast) {
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "update weatherinformation set weekth ='"
					+ dataforecast.getWeekth() + "', temp ='"
					+ dataforecast.getTemp() + "',weather ='"
					+ dataforecast.getWeatherdata() + "',code ='"
					+ dataforecast.getCode() + "' where weatherid ='"
					+ dataforecast.getWeatherid() + "';";
			sld.execSQL(sql);
		} catch (SQLException e) {
			Log.i("llllllllllllll", "gengxinshibai111111111    "+e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	* no use
	 * update listview weatherdata
	 * */
	
	public static void updatelistweatherdata(Dataforecast dataforecast){
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "update weatherinformation set weekth ='"
					+ dataforecast.getWeekth() + "', temp ='"
					+ dataforecast.getTemp() + "', code ='"
					+ dataforecast.getCode() + "' where weatherid ='"
					+ dataforecast.getWeatherid() + "';";
			sld.execSQL(sql);
		} catch (SQLException e) {
			Log.i("wchao", " update listview weatherdata "+e.toString());
			e.printStackTrace();
		}
		
		
	}

	// update citydefault
	public static void updatecitydefault(int citydefault, int fid) {
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "update woeidtable set location='" + citydefault
					+ "' where fid=" + fid + ";";
			sld.execSQL(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// update nowtemandweather
	public static void updatenowtemandweather(String woeidString,
			String nowtem, String nowweather, String sunrise, String sunset,
			String visiblity,String nowcode,String nowdate,String updatetime) {
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "update woeidtable set nowtemp='" + nowtem
					+ "', nowweather= '" + nowweather + "',sunrise= '"
					+ sunrise + "',sunset='" + sunset + "',visiblity='"
					+ visiblity + "',nowcode='"+nowcode+"',nowdate='"+nowdate+"',updatetime='"+updatetime+"' where citywoeid=" + woeidString + ";";
			Log.i("llllllllllllll", "gengxinchenggong");
			sld.execSQL(sql);
			Log.i("llllllllllllll", "gengxinchenggong");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.i("llllllllllllll", "gengxinshibai  "+e.toString());
			Log.i("hahhhahhhhhhh", "exception   " + e.toString());
			e.printStackTrace();
		}
	}

	// select citydefault shifoubeisheweimoren
	public static Defaultcity selectcitydefaultboolen() {
		boolean setdefault = false;
		String citywoeid = null;
		Defaultcity defaultcity = null;
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "select location,citywoeid from woeidtable";
			cur = sld.rawQuery(sql, null);
			if (!cur.moveToNext()) {
				defaultcity = null;
				cur.close();
			} else {
				cur.moveToPrevious();
				while (cur.moveToNext()) {
					if (cur.getInt(0) == 1) {
						setdefault = true;
						citywoeid = cur.getString(1);
						Log.i("zhaoyi_log1124", "shujuku   1212  " + citywoeid);
						defaultcity = new Defaultcity(setdefault, citywoeid);

						break;
					}

				}
				cur.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return defaultcity;
	}

	// update citydefault
	public static void update1citydefault(int citydefault, int fid) {
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "update woeidtable set location='" + citydefault
					+ "' where fid !=" + fid + ";";
			sld.execSQL(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// update citydefault
	public static void update2citydefault(int citydefault) {
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "update woeidtable set location='" + citydefault + "';";
			sld.execSQL(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// get weatherid from weatherinformation
	public static List<String> selectgetweatherid(String woeid) {
		List<String> listweaherid = null;
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			listweaherid = new ArrayList<String>();
			sql = "select weatherid from weatherinformation where citywoeid="
					+ woeid + ";";
			cur = sld.rawQuery(sql, null);
			if (!cur.moveToNext()) {
				listweaherid = null;
				cur.close();
			} else {
				cur.moveToPrevious();
				while (cur.moveToNext()) {
					String weatheridString = cur.getString(0);
					listweaherid.add(weatheridString);
				}
				cur.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listweaherid;

	}

	// select woeid
	public static List<Cityandwoeid> selectwoeid() {
		List<Cityandwoeid> list = null;
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			list = new ArrayList<Cityandwoeid>();

			Log.i("sqlitedata", "sisisisisisisisisisi  ");
			sql = "select citywoeid,city,location,fid from woeidtable;";
			Log.i("sqlitedata", "sisisisisis11111111111111111 ");
			cur = sld.rawQuery(sql, null);
			Log.i("sqlitedata", "sisisisisis22222222222222 ");
			if (!cur.moveToNext()) {
				Log.i("sqlitedata", "sisisisisis333333333333 ");
				list = null;
				cur.close();
			} else {
				Log.i("sqlitedata", "sisisisisis444444444 ");
				cur.moveToPrevious();//
				while (cur.moveToNext()) {
					Log.i("sqlitedata", "sisisisisis5555555555 ");
					int si = cur.getColumnCount();
					Log.i("sqlitedata", "si  " + si);
					for (int i = 0; i < si; i++) {
						Log.i("sqlitedata", "woeid  " + cur.getColumnName(i));
					}
					Log.i("sqlitedata",
							"woeid  " + cur.getColumnIndex("citywoeid"));
					String woeidString = cur.getString(cur
							.getColumnIndex("citywoeid"));
					String citynameString = cur.getString(cur
							.getColumnIndex("city"));
					int citylocation = cur.getInt(cur
							.getColumnIndex("location"));
					int cityfid = cur.getInt(cur.getColumnIndex("fid"));
					// cur.gets
					Log.i("sqlitedata", "woeidString111111111 " + woeidString);

					list.add(new Cityandwoeid(woeidString, citynameString,
							citylocation, cityfid));
				}
				cur.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	// select weatherdata
	public static List<Dataforecast> selectforecast(String woeid) {
		List<Dataforecast> dataforecasts = null;
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			dataforecasts = new ArrayList<Dataforecast>();
			sql = "select weekth,temp,weather,code from weatherinformation where citywoeid="
					+ woeid + ";";
			cur = sld.rawQuery(sql, null);
			if (!cur.moveToNext()) {
				Log.i("zhaoyi_log1124", "return null");
				dataforecasts = null;
				cur.close();
			} else {
				cur.moveToPrevious();
				while (cur.moveToNext()) {

					String weekth = cur.getString(cur.getColumnIndex("weekth"));
					String temString = cur
							.getString(cur.getColumnIndex("temp"));
					String weatherString = cur.getString(cur
							.getColumnIndex("weather"));
					String codeString = cur.getString(cur
							.getColumnIndex("code"));
					dataforecasts.add(new Dataforecast(weekth, temString,
							weatherString, codeString));
					Log.i("sqlitedata", "weekth   " + weekth);

				}
				cur.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("zhaoyi_log1124", "exception " + e.toString());
			e.printStackTrace();
		}

		return dataforecasts;
	}

	// get today weaherdata
	public static Dataforecast gettodayweather(String woeid) {
		Dataforecast dataforecast = null;
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "select weekth,temp,weather,code from weatherinformation where citywoeid="
					+ woeid + ";";
			cur = sld.rawQuery(sql, null);
			if (!cur.moveToNext()) {
				dataforecast = null;
				Log.i("zhaoyi_log1124", "get-------null");
				cur.close();
			} else if (cur.moveToFirst()) {
				Log.i("zhaoyi_log1124", "gettodayweather");
				String weekth = cur.getString(cur.getColumnIndex("weekth"));
				String temString = cur.getString(cur.getColumnIndex("temp"));
				String weatherString = cur.getString(cur
						.getColumnIndex("weather"));
				String codeString = cur.getString(cur.getColumnIndex("code"));
				dataforecast = new Dataforecast(weekth, temString,
						weatherString, codeString);
				cur.close();

			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataforecast;
	}

	// select woeid if exit
	public static boolean selectwoeidifexit(String woeidsString) {
		boolean isornotexit = false;
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "select citywoeid from woeidtable";
			cur = sld.rawQuery(sql, null);
			if (cur.moveToNext()) {
				cur.moveToPrevious();
				String citywoeid = null;
				while (cur.moveToNext()) {

					citywoeid = cur.getString(cur.getColumnIndex("citywoeid"));
					if (citywoeid.endsWith(woeidsString)) {
						isornotexit = true;
						break;
					}

				}

				cur.close();
			} else {
				isornotexit = false;
				cur.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isornotexit;
	}

	// delete city from woeidtable
	public static boolean deletecityfromwoeidtable(String woeid) {
		boolean delete = false;
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "delete from woeidtable where citywoeid='" + woeid + "'";
			sld.execSQL(sql);
			delete = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return delete;
	}

	// delete cityweatherdata from weatherinformation
	public static boolean deletecityfromweatherinformation(String woeid) {
		boolean deletecitdata = false;
		try {
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.zhanfan.zf_weather/mydb", null, // CursorFactory
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY);
			sql = "delete from weatherinformation where citywoeid='" + woeid
					+ "'";
			sld.execSQL(sql);
			deletecitdata = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return deletecitdata;
	}
}
