package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.entity;

public class Cityweather {
	private String code;// ¶ÔÓŠµÄÌìÆø±àºÅ
	private String date;// ÈÕÆÚ
	private String hightemp;// ×îžßÆøÎÂ
	private String lowtemp;// ×îµÍÆøÎÂ
	private String day;//ÖÜŒž
	private String weathertext;// ÌìÆøÐÅÏ¢

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHightemp() {
		return hightemp;
	}

	public void setHightemp(String hightemp) {
		this.hightemp = hightemp;
	}

	public String getLowtemp() {
		return lowtemp;
	}

	public void setLowtemp(String lowtemp) {
		this.lowtemp = lowtemp;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getWeathertext() {
		return weathertext;
	}

	public void setWeathertext(String weathertext) {
		this.weathertext = weathertext;
	}

}
