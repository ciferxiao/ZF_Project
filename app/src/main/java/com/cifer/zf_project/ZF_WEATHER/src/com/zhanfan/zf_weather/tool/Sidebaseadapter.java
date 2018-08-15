package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.tool;

import java.util.List;

import com.zhanfan.zf_weather.R;
import com.zhanfan.zf_weather.database.Sqlitedatabase;
import com.zhanfan.zf_weather.entity.Cityandwoeid;
import com.zhanfan.zf_weather.sidedelete.SwipeItemLayout;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Sidebaseadapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Cityandwoeid> citymanager;
	private Context context;
	int citydefault;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x20) {
				Log.i("hahhhahhhhhhh", "pos  ");
				// setdefaultciyty(pos);
			}
		};
	};

	public Sidebaseadapter(List<Cityandwoeid> citymanager, Context context) {
		super();
		this.citymanager = citymanager;
		this.context = context;

		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return citymanager.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return citymanager.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHold viewHold = null;
		View view1 = null;
		View view2 = null;
		if (convertView == null) {
			viewHold = new ViewHold();
			view1 = mInflater.inflate(R.layout.sidedelete1, null);
			view2 = mInflater.inflate(R.layout.sidedelete2, null);
			viewHold.nowtemp = (TextView) view1.findViewById(R.id.nowtep);
			viewHold.cityname = (TextView) view1
					.findViewById(R.id.citymanager_cityname);
			viewHold.weatherinfor = (TextView) view1
					.findViewById(R.id.citymanager_weatherinfor);
			viewHold.lowtohightemp = (TextView) view1
					.findViewById(R.id.citymanager_tempre);
			viewHold.setdefaultcity = (TextView) view2
					.findViewById(R.id.setdefault);
			viewHold.delete = (TextView) view2.findViewById(R.id.deletecity);
			convertView = new SwipeItemLayout(view1, view2, null, null);
			convertView.setTag(viewHold);
		} else {
			viewHold = (ViewHold) convertView.getTag();
		}
		final int pos = position;
		Log.i("zhaoyi_log1204", "defaultcity   "
				+ citymanager.get(pos).getCitydefault());
		if (citymanager.get(pos).getCitydefault() == 1) {
			viewHold.setdefaultcity.setText("Default");
			viewHold.setdefaultcity.setEnabled(false);
		} else {
			viewHold.setdefaultcity.setText("Set Default");
			viewHold.setdefaultcity.setEnabled(true);
		}
		viewHold.setdefaultcity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Sqlitedatabase.updatecitydefault(1, citymanager.get(pos)
						.getCityfid());
				Sqlitedatabase.update1citydefault(0, citymanager.get(pos)
						.getCityfid());
				Log.i("zhaoyi_log1204", "senreciver00000000000000");
				Log.i("guangbo", "200000000000000guangbo");
				newthread(pos);
//				Intent intent23 = new Intent();
//				intent23.setAction("com.zhanfan.setdefaultcity");
//
//				intent23.putExtra("citywoeid", citymanager.get(pos)
//						.getWoeidString());
//				intent23.putExtra("cityname", citymanager.get(pos)
//						.getCityname());
//				context.sendBroadcast(intent23);
//				Intent intent24 = new Intent();
//				intent24.setAction("com.zhanfan.defaultcitychanged");
//				context.sendBroadcast(intent24);
				Log.i("zhaoyi_log1204", "senreciver111111111");
				// notifyDataSetChanged();

				((TextView) v).setText("Default");
				// notifyDataSetChanged();
				Log.i("guangbo", "111111111111guangbo");
				Log.i("hahhhahhhhhhh", "44444444444444");
				// setdefaultcity.setText("Default");

			}
		});

		viewHold.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Sqlitedatabase.deletecityfromwoeidtable(citymanager.get(pos)
						.getWoeidString());
				Sqlitedatabase.deletecityfromweatherinformation(citymanager
						.get(pos).getWoeidString());
				Log.i("zhaoyi_delete", "zhaoyidelete");

				citymanager.remove(pos);
				notifyDataSetChanged();
				// notifyDataSetChanged();
				Log.i("hahhhahhhhhhh", "5555555555555"); 
			}
		});
		Log.i("zhaoyi_log1216","string   "+ citymanager.get(pos).getWoeidString());
        if(Sqlitedatabase.getnowtempandweather(
								citymanager.get(pos).getWoeidString())==null){

              System.exit(0);
         }
		Log.i("hahhhahhhhhhh",
				"temp  "
						+ Sqlitedatabase.getnowtempandweather(
								citymanager.get(pos).getWoeidString())
								.getTemString() + "");
		viewHold.nowtemp.setText(Sqlitedatabase.getnowtempandweather(
				citymanager.get(pos).getWoeidString()).getTemString()
				+ "℃");
		viewHold.cityname.setText(citymanager.get(pos).getCityname());
		viewHold.weatherinfor.setText(Sqlitedatabase.getnowtempandweather(
				citymanager.get(pos).getWoeidString()).getWeatherinforString());
        
       /* //由于，当天的天气，和预报的天气有差别， 所以这里取预报天气里的第一天的天气
        viewHold.weatherinfor.setText(Sqlitedatabase
				.selectforecast(citymanager.get(pos).getWoeidString()).get(0).getWeatherdata());*/
		viewHold.lowtohightemp.setText(Sqlitedatabase
				.selectforecast(citymanager.get(pos).getWoeidString()).get(0)
				.getTemp()
				+ "℃");
		// viewHold.weatherinfor.setText(citymanager.get(pos).get)

		return convertView;
	}

	class ViewHold {
		TextView nowtemp;
		TextView cityname;
		TextView weatherinfor;
		TextView lowtohightemp;
		TextView setdefaultcity;
		TextView delete;

	}

	public void newthread(final int pos) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent23 = new Intent();
				intent23.setAction("com.zhanfan.setdefaultcity");

				intent23.putExtra("citywoeid", citymanager.get(pos)
						.getWoeidString());
				intent23.putExtra("cityname", citymanager.get(pos)
						.getCityname());
				context.sendBroadcast(intent23);
				Intent intent24 = new Intent();
				intent24.setAction("com.zhanfan.defaultcitychanged");
				context.sendBroadcast(intent24);

				Log.i("zhaoyi_log1204", "senreciver");
			}
		}).start();
	}



	// @Override
	// public View getView( int position, View convertView, ViewGroup parent) {
	// // TODO Auto-generated method stub
	// ViewHold viewHold=null;
	// if(convertView==null){
	// viewHold=new ViewHold();
	// convertView=mInflater.inflate(R.layout.sidelistviewitem, null);
	// viewHold.cityname=(TextView)
	// convertView.findViewById(R.id.citymanager_name);
	// viewHold.delete=(TextView) convertView.findViewById(R.id.delete);
	// convertView.setTag(viewHold);
	// }else{
	// viewHold=(ViewHold) convertView.getTag();
	// }
	// final int pos = position;
	// viewHold.delete.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	//
	// Sqlitedatabase.deletecityfromwoeidtable(citymanager.get(pos).getWoeidString());
	// Sqlitedatabase.deletecityfromweatherinformation(citymanager.get(pos).getWoeidString());
	// Log.i("zhaoyi_delete", "zhaoyidelete");
	//
	// citymanager.remove(pos);
	// notifyDataSetChanged();
	//
	// }
	// });
	// viewHold.cityname.setText(citymanager.get(position).getCityname());
	// return convertView;
	// }
	//
	// class ViewHold {
	// TextView cityname;
	// TextView delete;
	// }
}
