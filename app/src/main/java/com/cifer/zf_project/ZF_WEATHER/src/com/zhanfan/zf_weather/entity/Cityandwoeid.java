package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.entity;

import android.R.integer;





public class Cityandwoeid {
	private String woeidString;
	private String cityname;
    private int citydefault;
    private int cityfid;
	public Cityandwoeid(String woeidString, String cityname,int citydefault,int cityfid) {
		super();
		this.woeidString = woeidString;
		this.cityname = cityname;
		this.citydefault=citydefault;
		this.cityfid=cityfid;
	}

	public int getCityfid() {
		return cityfid;
	}

	public void setCityfid(int cityfid) {
		this.cityfid = cityfid;
	}

	public int getCitydefault() {
		return citydefault;
	}

	public void setCitydefault(int citydefault) {
		this.citydefault = citydefault;
	}

	public String getWoeidString() {
		return woeidString;
	}

	public void setWoeidString(String woeidString) {
		this.woeidString = woeidString;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

}
