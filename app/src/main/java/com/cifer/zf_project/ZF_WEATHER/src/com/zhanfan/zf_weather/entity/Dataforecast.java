package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.entity;

public class Dataforecast {
	public String weatherid;
	public String citywoeid;
	public String weekth;
	public String temp;
	public String weatherdata;
	public String code;

	
	public Dataforecast() {
		super();
	}
	public Dataforecast(String weekth, String temp, String weatherdata,String code) {
		super();
		this.weekth = weekth;
		this.temp = temp;
		this.weatherdata = weatherdata;
		this.code = code;
	}
	public Dataforecast(String weatherid, String weekth, String temp,
			String weatherdata, String code) {
		super();
		this.weatherid = weatherid;
		this.weekth = weekth;
		this.temp = temp;
		this.weatherdata = weatherdata;
		this.code = code;
	}

	public Dataforecast(String weatherid, String citywoeid, String weekth,
			String temp, String weatherdata, String code) {
		super();
		this.weatherid = weatherid;
		this.citywoeid = citywoeid;
		this.weekth = weekth;
		this.temp = temp;
		this.weatherdata = weatherdata;
		this.code = code;
	}

	public String getWeatherid() {
		return weatherid;
	}

	public void setWeatherid(String weatherid) {
		this.weatherid = weatherid;
	}

	public String getCitywoeid() {
		return citywoeid;
	}

	public void setCitywoeid(String citywoeid) {
		this.citywoeid = citywoeid;
	}

	public String getWeekth() {
		return weekth;
	}

	public void setWeekth(String weekth) {
		this.weekth = weekth;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getWeatherdata() {
		return weatherdata;
	}

	public void setWeatherdata(String weatherdata) {
		this.weatherdata = weatherdata;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
