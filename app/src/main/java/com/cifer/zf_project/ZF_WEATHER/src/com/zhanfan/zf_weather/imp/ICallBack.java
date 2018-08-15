package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.imp;



public interface ICallBack {
	//Sqlitedatabase.insertwoeid(maxfid, woeid, citysearch2222222,0,temp,weather,sunrise,sunset,vistbility,code);
	void InsertWordIdData(String maxfid, String woeid, String citysearch, int num, String temp, String weather, String sunrise, String sunset, String visibility, String code, String nowdate, String updatetime);
	//Sqlitedatabase.insertweathdata(new Dataforecast(maxweatherid, woeid, weekth, forecasehightemp, forecaselowtemp, codeString));
	void InsertWeatherData(String maxweatherid, String woeid, String weeth, String forecasehightemp, String forecaselowtemp, String codeStirng);
}