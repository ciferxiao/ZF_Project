package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.tool;

import java.security.acl.LastOwnerException;
import java.util.List;

import com.loc.ci;
import com.zhanfan.zf_weather.MainActivity;
import com.zhanfan.zf_weather.R;
import com.zhanfan.zf_weather.database.Sqlitedatabase;
import com.zhanfan.zf_weather.entity.Cityandwoeid;




import android.app.Dialog;
import android.content.Context;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GridCityMAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Cityandwoeid> citymanager;
	private Context context;
	private Dialog mDialog;

	public GridCityMAdapter(Context context, List<Cityandwoeid> citymanager) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.citymanager = citymanager;
	}

	@Override
	public int getCount() {
		return citymanager.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHold viewHold=null;
		if (convertView == null) {
			viewHold=new ViewHold();
			convertView = mInflater.inflate(R.layout.item_gridview_citymanager,
					null);
			viewHold.cityname=(TextView) convertView.findViewById(R.id.grid_city);
			viewHold.delete=(TextView) convertView.findViewById(R.id.grid_item_delete);
			//viewHold.weatherdata=(TextView) convertView.findViewById(R.id.grid_weather);
			convertView.setTag(viewHold);
			
		}else{
			viewHold=(ViewHold) convertView.getTag();
		}
		/*final TextView grid_city = (TextView) convertView
				.findViewById(R.id.gridcityname);*/
		/*TextView grid_temp = (TextView) convertView
				.findViewById(R.id.grid_temp);
		
		TextView grid_weather = (TextView) convertView
				.findViewById(R.id.grid_weather); 
	
		TextView city_item_layout = (TextView) convertView
				.findViewById(R.id.city_item_layout);*/
		//TextView grid_item_delete = (TextView) convertView
		//		.findViewById(R.id.grid_item_delete);
		
	viewHold.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
               citymanager.get(position).getWoeidString();
               
               Sqlitedatabase.deletecityfromwoeidtable(citymanager.get(position).getWoeidString());
               Sqlitedatabase.deletecityfromweatherinformation(citymanager.get(position).getWoeidString());
				Log.i("zhaoyi_delete", "zhaoyidelete");
			
				citymanager.remove(position);
				notifyDataSetChanged();
			}
		});
		
		if (position == citymanager.size()) {
			viewHold.cityname.setText("Add City");
		/*	grid_temp.setText("");
			grid_weather.setText("");*/
	
		
			/*city_item_layout.setBackgroundResource(R.drawable.cityadd_bg);*/
			//grid_item_delete.setText("");
		} else {
			Log.i("zhaoyi_log1120","adapter    "+citymanager.get(position).getCityname());
			//viewHold.delete.setText("¡Á");
			viewHold.cityname.setText(citymanager.get(position).getCityname());
			/*grid_temp.setText(citymanager.get(position).getTemp());
			
			grid_weather.setText(citymanager.get(position).getWeather());
			
			city_item_layout.setBackgroundResource(Color.TRANSPARENT);*/
		}
		return convertView;
	}

	public void setCitymanager(List<Cityandwoeid> citymanager) {
		this.citymanager = citymanager;
	}
	class ViewHold{
		TextView cityname;
		TextView delete;
		TextView weatherdata;
		
	}

}
