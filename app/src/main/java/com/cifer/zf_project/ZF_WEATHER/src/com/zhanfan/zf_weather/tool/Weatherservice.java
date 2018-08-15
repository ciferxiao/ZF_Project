package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.tool;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.zhanfan.zf_weather.entity.CityInfo;
import com.zhanfan.zf_weather.entity.Cityweather;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

public class Weatherservice {
	public List<CityInfo> queryCityInfo(String text, String localeLang) {
		Log.i("lzqtestwhere", "queryCityInfo+++++++++localeLang++++===="
				+ localeLang);
		DefaultHttpClient client = new DefaultHttpClient();
		String getUrl = getRequestUrl(text, localeLang);
		HttpGet get = new HttpGet(getUrl);
		// Log.i(TAG,"queryCityInfo") ;
		try {
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				Log.i("lzqtestwhere", "queryCityInfo+++++++++result++++===="
						+ result);
				// Log.i(TAG,result) ;
				return parseCityInfo(result);
			}
		} catch (Exception e) {
			// Tool.print(getResources().getString(R.string.server_error)) ;
		}
		return new ArrayList<CityInfo>();
	}

	// http://query.yahooapis.com/v1/public/yql?q=SELECT * FROM%20geo.places
	// WHERE text="yaqueling" and lang="zh-cn"
	public String getRequestUrl(String text, String localeLang) {
		return "http://query.yahooapis.com/v1/public/yql?q=SELECT%20woeid,name,country,"
				+ "admin1,admin2,timezone%20FROM%20geo.places%20WHERE%20text=%22"
				+ text + "%22%20and%20lang=%22" + localeLang + "%22";
	}

	protected List<CityInfo> parseCityInfo(String result) {
		// Log.i(TAG,"parseCityInfo") ;
		XmlPullParser xmlParser = Xml.newPullParser();
		ByteArrayInputStream mByteArrayInputStream = null;
		if (result == null || TextUtils.isEmpty(result.trim())) {
			return null;
		} else {
			mByteArrayInputStream = new ByteArrayInputStream(result.getBytes());
			try {
				xmlParser.setInput(mByteArrayInputStream, "UTF-8");
				return parseXmlStream(xmlParser);
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private List<CityInfo> parseXmlStream(XmlPullParser xmlParser)
			throws XmlPullParserException, IOException {
		// Log.i(TAG ,"parseXmlStream") ;
		int evtType = xmlParser.getEventType();
		int count = 0;
		List<CityInfo> cityInfoList = new ArrayList<CityInfo>();
		CityInfo cityInfo = null;
		String tag = null;
		while (evtType != XmlPullParser.END_DOCUMENT) {
			switch (evtType) {
			case XmlPullParser.START_TAG:
				tag = xmlParser.getName();
				if (tag.equals("query")) {
					count = Integer.parseInt(xmlParser.getAttributeValue(0)
							.trim());
					if (count == 0) {
						return cityInfoList;
					}
				} else if (tag.equals("place")) {
					cityInfo = new CityInfo();
				} else if (tag.equals("name")) {
					cityInfo.setName(xmlParser.nextText());
				} else if (tag.equals("country")) {
					cityInfo.setCountry(xmlParser.nextText());
				} else if (tag.equals("admin1")) {
					cityInfo.setAdmin1(xmlParser.nextText());
				} else if (tag.equals("admin2")) {
					cityInfo.setAdmin2(xmlParser.nextText());
				} else if (tag.equals("timezone")) {
					cityInfo.setTimezone(xmlParser.nextText());
				} else if (tag.equals("woeid")) {
					cityInfo.setWoeid(xmlParser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				tag = xmlParser.getName();
				if (tag.equals("place")) {
					cityInfoList.add(cityInfo);
					cityInfo = null;
				}
				break;

			}
			evtType = xmlParser.next();
		}
		// Log.i(TAG,cityInfoList.toString()) ;
		Log.i("zhaoyi", "shuju  " + cityInfoList.get(0).getCountry());
		return cityInfoList;
	}


	
}
