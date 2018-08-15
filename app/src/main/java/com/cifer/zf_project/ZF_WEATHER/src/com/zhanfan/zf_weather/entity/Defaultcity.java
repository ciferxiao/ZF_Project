package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.entity;

public class Defaultcity {
	
	private boolean setdefault;
	private String defaultwoeid;
	
	
	
	public Defaultcity(boolean setdefault, String defaultwoeid) {
		super();
		this.setdefault = setdefault;
		this.defaultwoeid = defaultwoeid;
	}
	public boolean isSetdefault() {
		return setdefault;
	}
	public void setSetdefault(boolean setdefault) {
		this.setdefault = setdefault;
	}
	public String getDefaultwoeid() {
		return defaultwoeid;
	}
	public void setDefaultwoeid(String defaultwoeid) {
		this.defaultwoeid = defaultwoeid;
	}
	

}
