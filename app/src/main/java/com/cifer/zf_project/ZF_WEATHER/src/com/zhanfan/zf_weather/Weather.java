package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.net.NetworkRequest;
import android.net.Network;

public class Weather extends AppWidgetProvider { 
	String clickaction="com.zhanfanweather.click";
	String clickString = null;
	RemoteViews remoteViews=null;
	String netAction= "android.net.conn.CONNECTIVITY_CHANGE";
	String clickactiondesk = "com.zhanfandeskclock.click";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		ConnectivityManager con=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = con.getActiveNetworkInfo();
		Log.i("zhanfanzhaoyi_log", "8888888888888888");
		// TODO Auto-generated method stub
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.weatherwidget);
		//Updateservice.context=context;
		//Updateservice.remoteViews=remoteViews;
		//Updateservice.appwidetmanger=appWidgetManager;
		Intent intent=new Intent(context, Updateservice.class);
		context.startService(intent);
		int code = (int) SystemClock.uptimeMillis();
		int flag = PendingIntent.FLAG_UPDATE_CURRENT;
		if(mNetworkInfo!=null){
			//执行相关操作
			Intent  intentclick =new Intent(clickactiondesk);
			PendingIntent pendingIntent=PendingIntent.getBroadcast(context, code, intentclick,flag);
			remoteViews.setOnClickPendingIntent(R.id.linerlayouthour, pendingIntent);
			
			Intent intentclockIntent = new Intent(clickaction);
			PendingIntent pendingIntent_weather=PendingIntent.getBroadcast(context, code, intentclockIntent,flag);
			remoteViews.setOnClickPendingIntent(R.id.linerlayout_weather, pendingIntent_weather);
			ComponentName componentName = new ComponentName(context, Weather.class);
			// 调用AppWidgetManager将remoteViews添加到ComponentName中
			appWidgetManager.updateAppWidget(componentName, remoteViews);
		} else {

			//执行相关操作
			Intent  intentclick =new Intent(clickactiondesk);
			PendingIntent pendingIntent=PendingIntent.getBroadcast(context, code, intentclick,flag);
			remoteViews.setOnClickPendingIntent(R.id.linerlayouthour, pendingIntent);
			
			Intent intentclockIntent = new Intent(clickaction);
			PendingIntent pendingIntent_weather=PendingIntent.getBroadcast(context, code, intentclockIntent,flag);
			remoteViews.setOnClickPendingIntent(R.id.linerlayout_weather, pendingIntent_weather);
			ComponentName componentName = new ComponentName(context, Weather.class);
			// 调用AppWidgetManager将remoteViews添加到ComponentName中
			appWidgetManager.updateAppWidget(componentName, remoteViews);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		AppWidgetManager appWidgetManager= AppWidgetManager.getInstance(context);
		ComponentName ComponentName = new ComponentName(context, getClass());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName);
		ConnectivityManager con=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = con.getActiveNetworkInfo();
		// TODO Auto-generated method stub
		Log.i("zhanfanzhaoyi_log","111111111111");
		if(intent.getAction().equals(clickaction)){
			Log.d("wchao intent", "intent_deskclock1111");
			Intent  intentclick =new Intent(context, MainActivity.class);
			intentclick.addCategory(Intent.CATEGORY_LAUNCHER);  
			intentclick.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
			context.startActivity(intentclick);
		}else if(intent.getAction().equals(clickactiondesk)){
			Intent intent_deskclock = new Intent(Intent.ACTION_MAIN);  
			intent_deskclock.addCategory(Intent.CATEGORY_LAUNCHER);   
			Log.d("wchao intent", "intent_deskclock2222");
			ComponentName component_deskclock = new ComponentName("com.android.deskclock", "com.android.deskclock.DeskClock");
			intent_deskclock.setComponent(component_deskclock);
			context.startActivity(intent_deskclock);
		} else if (intent.getAction().equals(netAction)) {
			if (mNetworkInfo!=null) {
				onUpdate(context, appWidgetManager, appWidgetIds);
			}
		}
	}
	// 第一个widget被创建时调用
	@Override
	public void onEnabled(Context context) {

		Log.d("zhanfanzhaoyi_log", "onEnabled");
		// 在第一个 widget 被创建时，开启服务
		Intent intent=new Intent(context, Updateservice.class);
		context.startService(intent);

		AppWidgetManager appWidgetManager= AppWidgetManager.getInstance(context);
		ComponentName ComponentName = new ComponentName(context, getClass());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName);
		ConnectivityManager con=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT >= 23) {   
			con.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {  
				@Override 
				public void onAvailable(Network network) {
					super.onAvailable(network);
					if (network !=null) {
						onUpdate(context, appWidgetManager, appWidgetIds);
					}
				}
				@Override
				public void onLost(Network network) {
					super.onLost(network);
				}
			});
		}
		NetworkInfo mNetworkInfo = con.getActiveNetworkInfo();

		remoteViews = new RemoteViews(context.getPackageName(), R.layout.weatherwidget);
		int code = (int) SystemClock.uptimeMillis();
		int flag = PendingIntent.FLAG_UPDATE_CURRENT;
		if(mNetworkInfo!=null){
			//执行相关操作
			Intent  intentclick =new Intent(clickactiondesk);
			PendingIntent pendingIntent=PendingIntent.getBroadcast(context, code, intentclick,flag);
			remoteViews.setOnClickPendingIntent(R.id.linerlayouthour, pendingIntent);
			
			Intent intentclockIntent = new Intent(clickaction);
			PendingIntent pendingIntent_weather=PendingIntent.getBroadcast(context, code, intentclockIntent,flag);
			remoteViews.setOnClickPendingIntent(R.id.linerlayout_weather, pendingIntent_weather);
			ComponentName componentName = new ComponentName(context, Weather.class);
			// 调用AppWidgetManager将remoteViews添加到ComponentName中
			appWidgetManager.updateAppWidget(componentName, remoteViews);
		} else {
			//执行相关操作
			Intent  intentclick =new Intent(clickactiondesk);
			PendingIntent pendingIntent=PendingIntent.getBroadcast(context, code, intentclick,flag);
			remoteViews.setOnClickPendingIntent(R.id.linerlayouthour, pendingIntent);
			
			Intent intentclockIntent = new Intent(clickaction);
			PendingIntent pendingIntent_weather=PendingIntent.getBroadcast(context, code, intentclockIntent,flag);
			remoteViews.setOnClickPendingIntent(R.id.linerlayout_weather, pendingIntent_weather);
			ComponentName componentName = new ComponentName(context, Weather.class);
			// 调用AppWidgetManager将remoteViews添加到ComponentName中
			appWidgetManager.updateAppWidget(componentName, remoteViews);
		}
		super.onEnabled(context);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(context, Updateservice.class);
		context.stopService(intent);
		super.onDeleted(context, appWidgetIds);
	}

}
