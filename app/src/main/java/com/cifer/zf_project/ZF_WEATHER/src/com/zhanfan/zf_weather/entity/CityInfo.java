package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.entity;

import android.database.Cursor;
import android.provider.BaseColumns;


public class CityInfo {
	
	String name ;
	String country ;
	String admin1 ;
	String admin2 ;
	String timezone ;
	String woeid ;
		
	public CityInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CityInfo(String name, String country, String admin1, String admin2,
			String timezone, String woeid) {
		super();
		this.name = name;
		this.country = country;
		this.admin1 = admin1;
		this.admin2 = admin2;
		this.timezone = timezone;
		this.woeid = woeid;
	}

	public CityInfo(Cursor cursor) {
		super();
			// TODO Auto-generated constructor stub
			name = cursor.getString(Columns.CITYINFO_NAME_INDEX) ;
			country = cursor.getString(Columns.CITYINFO_COUNTRY_INDEX) ;
			admin1 = cursor.getString(Columns.CITYINFO_ADMIN1_INDEX) ;
			admin2 = cursor.getString(Columns.CITYINFO_ADMIN2_INDEX) ;
			timezone = cursor.getString(Columns.CITYINFO_TIMEZONE_INDEX) ;
			woeid = cursor.getString(Columns.CITYINFO_WOEID_INDEX) ;
	}
	
	public static class Columns implements BaseColumns{				
		
		public static final String NAME = "name" ;
		public static final String COUNTRY = "country" ;
		public static final String ADMIN1 = "admin1" ;
		public static final String ADMIN2 = "admin2" ;
		public static final String TIMEZONE = "timezone" ;
		public static final String WOEID = "woeid" ;
		
		public static final String[] CITYINFO_QUERY_COLUMNS = {
			_ID,NAME,COUNTRY,ADMIN1,ADMIN2,TIMEZONE,WOEID};
		
		public static final int CITYINFO_NAME_INDEX = 1;
		public static final int CITYINFO_COUNTRY_INDEX = 2;
		public static final int CITYINFO_ADMIN1_INDEX = 3;
		public static final int CITYINFO_ADMIN2_INDEX = 4;
		public static final int CITYINFO_TIMEZONE_INDEX = 5;
		public static final int CITYINFO_WOEID_INDEX = 6;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAdmin1() {
		return admin1;
	}
	public void setAdmin1(String admin1) {
		this.admin1 = admin1;
	}
	public String getAdmin2() {
		return admin2;
	}
	public void setAdmin2(String admin2) {
		this.admin2 = admin2;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getWoeid() {
		return woeid;
	}
	public void setWoeid(String woeid) {
		this.woeid = woeid;
	}

	@Override
	public String toString() {
		return "CityInfo [name=" + name + ", country=" + country + ", admin1="
				+ admin1 + ", admin2=" + admin2 + ", timezone=" + timezone
				+ ", woeid=" + woeid + "]";
	}
	
}
