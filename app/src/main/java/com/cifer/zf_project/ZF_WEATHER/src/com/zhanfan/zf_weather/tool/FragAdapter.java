package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.tool;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragAdapter extends FragmentStatePagerAdapter/*FragmentPagerAdapter*/ {

	private List<Fragment> mFragments;
	FragmentManager fm;
	
	public FragAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.fm = fm;
		mFragments=fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragments.size();
	}


}
