package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.tool;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonListAdapter extends BaseAdapter {

	private List<Object> mList ;
	private Context mContext ; 
	private int mResId ;
	private LayoutInflater inflater;
	private int[] mViewIds ;
	
	public CommonListAdapter(Context context,List list ,int resId,int...viewIds) {
		// TODO Auto-generated constructor stub
		mList = list ;
		mContext = context ;
		mResId = resId ;
		mViewIds = viewIds ;
		inflater = LayoutInflater.from(mContext) ;
	}
	public CommonListAdapter(Context context,Object[] array ,int resId,int...viewIds) {
		mList = Arrays.asList(array) ;
		mContext = context ;
		mResId = resId ;
		mViewIds = viewIds ;
		inflater = LayoutInflater.from(mContext) ;
	}
	
	public void listHasChange(List list){
		mList = list ;
		notifyDataSetChanged(); 
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.i("getCount",mList.size()+"") ;
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position); 
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View[] views ;
		Log.i("getView",position+"") ;
		if(convertView == null){
			convertView = inflater.inflate(mResId, null) ;
			int len = mViewIds.length ;
			views = new View[len] ;
			for (int i = 0; i < len; i++) {
				views[i] = convertView.findViewById(mViewIds[i]) ;
			}
			convertView.setTag(views); 
		}else {
			views = (View[]) convertView.getTag();
		}
		bindViews(position,views);
		return convertView;
	}

	public abstract void bindViews(int position ,View...views) ;

	public void remove(Object dragItem) {
		mList.remove(dragItem) ;		
	}
	
	public void remove(int position) {
		mList.remove(position) ;		
	}
	
	public void insert(Object dragItem, int dragPosition) {
		mList.add(dragPosition, dragItem);		
	}

	public List<Object> getList() {
		// TODO Auto-generated method stub
		return mList;
	}
		
}
