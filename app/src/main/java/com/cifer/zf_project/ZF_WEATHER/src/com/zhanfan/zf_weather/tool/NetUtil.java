package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
	public static boolean hasNetwork(Context context){
		ConnectivityManager con = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if(workinfo == null || !workinfo.isAvailable())
		{
			return false;
		}
		return true;
	}

}
