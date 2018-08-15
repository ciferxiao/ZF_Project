package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.tool;

import java.security.PublicKey;
import java.util.List;

import com.zhanfan.zf_weather.R;
import com.zhanfan.zf_weather.entity.Cityweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderAdapter extends BaseAdapter {
	private Context context;
	private List<Cityweather> cityweathers;
    private LayoutInflater mInflater;
    int[] weatherpicture = new int[] { R.drawable.weather0,
			R.drawable.weather1, R.drawable.weather2, R.drawable.weather3,
			R.drawable.weather4, R.drawable.weather5, R.drawable.weather6,
			R.drawable.weather7, R.drawable.weather8, R.drawable.weather9,
			R.drawable.weather10, R.drawable.weather11, R.drawable.weather12,
			R.drawable.weather13, R.drawable.weather14, R.drawable.weather15,
			R.drawable.weather16, R.drawable.weather17, R.drawable.weather18,
			R.drawable.weather19, R.drawable.weather20, R.drawable.weather21,
			R.drawable.weather22, R.drawable.weather23, R.drawable.weather24,
			R.drawable.weather25, R.drawable.weather26, R.drawable.weather27,
			R.drawable.weather28, R.drawable.weather29, R.drawable.weather30,
			R.drawable.weather31, R.drawable.weather32, R.drawable.weather33,
			R.drawable.weather34, R.drawable.weather35, R.drawable.weather36,
			R.drawable.weather37, R.drawable.weather38, R.drawable.weather39,
			R.drawable.weather40, R.drawable.weather41, R.drawable.weather42,
			R.drawable.weather43, R.drawable.weather44, R.drawable.weather45,
			R.drawable.weather46, R.drawable.weather47

	};
	public ViewHolderAdapter(Context context, List<Cityweather> cityweathers) {
		super();
		this.mInflater=LayoutInflater.from(context);
		this.cityweathers = cityweathers;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cityweathers == null ? 0 : cityweathers.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cityweathers.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//ÓÃ³ÖÓÐÕßÄ£ÊœÌážßÐ§ÂÊ
		ViewHolder viewHolder=null;
		if(viewHolder==null){
			viewHolder=new ViewHolder();
			//Íš¹ýLayoutInflaterÊµÀý»¯²ŒŸÖ
			convertView=mInflater.inflate(R.layout.showforecast, null);
			viewHolder.day=(TextView) convertView.findViewById(R.id.day);
			viewHolder.temp=(TextView) convertView.findViewById(R.id.temp);
			viewHolder.weathertext=(TextView) convertView.findViewById(R.id.textweather);
			viewHolder.weathercode = (ImageView) convertView
					.findViewById(R.id.weathercode);
			convertView.setTag(viewHolder);
		}else{
			//Íš¹ýtag ÕÒµœ»ºŽæµÄ²ŒŸÖ
			viewHolder=(ViewHolder) convertView.getTag();
		}

		
		//ÉèÖÃÊýŸÝ
		viewHolder.day.setText(cityweathers.get(position).getDay());
		viewHolder.temp.setText(cityweathers.get(position).getHightemp()+"℃");
//		viewHolder.temp.setText(cityweathers.get(position).getHightemp()+"℃ ~"+cityweathers.get(position).getLowtemp()+"℃");
//		viewHolder.weathertext.setText(cityweathers.get(position).getWeathertext());
		viewHolder.weathertext.setText(cityweathers.get(position).getLowtemp()+"℃");
		viewHolder.weathercode.setImageResource(weatherpicture[Integer.parseInt(cityweathers.get(position).getCode())]);
		return convertView;
	}

   class  ViewHolder{
//	   private String code;// ¶ÔÓŠµÄÌìÆø±àºÅ
//		private String date;// ÈÕÆÚ
//		private String hightemp;// ×îžßÆøÎÂ
//		private String lowtemp;// ×îµÍÆøÎÂ
//		private String day;//ÖÜŒž
//		private String weathertext;// ÌìÆøÐÅÏ¢ 
	    TextView day;
	    TextView temp;
	    ImageView weathercode;
	    TextView weathertext;
   }
}
