package com.cifer.zf_project.ZF_WEATHER.src.com.zhanfan.zf_weather.tool;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.zhanfan.zf_weather.entity.Cityweather;

import android.os.Handler;
import android.util.Log;
import android.util.Xml;

public class Getnetdata {
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.i("lllllllllllllllllllll", "Handler()");
			if (msg.what == 0x01) {
				
			} else if (msg.what == 0x02) {
				

			} else if (msg.what == 0x03) {
				
				
			} else if (msg.what == 0x04) {
			
			}
		};
	};
	static List<Cityweather>  cityweathers=null;//ÎåÌì ÌìÆøÔ€±šÐÅÏ¢
	String woeid;
	
	public static List<Cityweather> getCityweathers() {
		return cityweathers;
	}


	public static void setCityweathers(List<Cityweather> cityweathers) {
		Getnetdata.cityweathers = cityweathers;
	}


		// »ñµÃ³ÇÊÐµÄwoeid
		public void getcitywoeid(String cityname) {
			Log.i("lllllllllllllllllllll", "getcitywoeid(cityname   " + cityname);
			String getwoeidaddr = "https://search.yahoo.com/sugg/gossip/gossip-gl-location/?appid=weather&output=sd1&p2=cn,t,pt,z&lc=en-US&command="
					+ cityname;
			String getwoeidbyxml = "http://query.yahooapis.com/v1/public/yql?q=SELECT%20woeid,name,country,admin1,admin2,timezone%20FROM%20geo.places%20WHERE%20text=%22"+cityname+"%22%20and%20lang=%22en-us%22";
		
			String resultData = "";//»ñµÃµÄÊýŸÝ 
			URL url = null;  
			try  
			{  
			    //¹¹ÔìÒ»žöURL¶ÔÏó  
			    url = new URL(getwoeidbyxml);   
			}  
			catch (MalformedURLException e)  
			{  
			    Log.e("llllll", "MalformedURLException");  
			} 
			try  
		    {  
		        //Ê¹ÓÃHttpURLConnectionŽò¿ªÁ¬œÓ  
		        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();  
		        //µÃµœ¶ÁÈ¡µÄÄÚÈÝ(Á÷)  
		        InputStreamReader in = new InputStreamReader(urlConn.getInputStream());  
		        // ÎªÊä³öŽŽœšBufferedReader  
		        BufferedReader buffer = new BufferedReader(in);  
		        String inputLine = null;  
		        //Ê¹ÓÃÑ­»·ÀŽ¶ÁÈ¡»ñµÃµÄÊýŸÝ  
		        while (((inputLine = buffer.readLine()) != null))  
		        {  
		            //ÎÒÃÇÔÚÃ¿Ò»ÐÐºóÃæŒÓÉÏÒ»žö"\n"ÀŽ»»ÐÐ  
		            resultData += inputLine + "\n";  
		        }           
		        in.close();  
		        urlConn.disconnect();  
		        //ÉèÖÃÏÔÊŸÈ¡µÃµÄÄÚÈÝ  
		        if ( resultData != null )  
		        {  
		        	xmlwoeid(resultData);//œâÎöXML  
		        }  
		         
		    }  
		    catch (IOException e)  
		    {  
		        Log.e("llllll", "IOException");  
		    }  

		}
		
		
		//XMLœâÎö³öµÃµœwoeid
		public  void xmlwoeid(String buffer){
			List<String> woeidlStrings=null;
			 XmlPullParser xmlParser = Xml.newPullParser();//»ñµÃXmlPullParserœâÎöÆ÷     
		      
			    ByteArrayInputStream tInputStringStream = null;  
			    if (buffer != null && !buffer.trim().equals(""))  
			    {  
			       tInputStringStream = new ByteArrayInputStream(buffer.getBytes());  
			    }  
			    else  
			    {  
			        return ;  
			    }  
			  
			    try   
			    {  
			    	woeidlStrings	=new ArrayList<String>();
			    	Log.i("llllllllllllllll", "length "+woeidlStrings.size());
			    	if(woeidlStrings.size()!=0){
			    		Log.i("llllllllllllllll", "kkkkkkkkkkkkkkkkkkkkk");
			    		woeidlStrings.clear();
			    	}
			        //µÃµœÎÄŒþÁ÷£¬²¢ÉèÖÃ±àÂë·œÊœ  
			        //InputStream inputStream=mContext.getResources().getAssets().open(fileName);  
			        //xmlParser.setInput(inputStream, "utf-8");  
			        xmlParser.setInput(tInputStringStream, "UTF-8");  
			          
			        //»ñµÃœâÎöµœµÄÊÂŒþÀà±ð£¬ÕâÀïÓÐ¿ªÊŒÎÄµµ£¬œáÊøÎÄµµ£¬¿ªÊŒ±êÇ©£¬œáÊø±êÇ©£¬ÎÄ±ŸµÈµÈÊÂŒþ¡£  
			        int evtType=xmlParser.getEventType();  
			       
			        while(evtType!=XmlPullParser.END_DOCUMENT)//Ò»Ö±Ñ­»·£¬Ö±µœÎÄµµœáÊø     
			        {   
			            switch(evtType)  
			            {   
			            case XmlPullParser.START_TAG:  
			                String tag = xmlParser.getName();   
			               // Log.i("lllllllllllllllllllll","tag = "+tag);
			                if("woeid".equals(tag)){
			                	String woeid=xmlParser.nextText();
			                	
			                	woeidlStrings.add(woeid);
			                	 
			                	 Log.i("lllllllllllllllllllll","woeid = "+woeid);
			                }
			                
			                //Èç¹ûÊÇcity±êÇ©¿ªÊŒ£¬ÔòËµÃ÷ÐèÒªÊµÀý»¯¶ÔÏóÁË  
			                break;  
			               
			            case XmlPullParser.END_TAG:  
			                //±êÇ©œáÊø                     
			            default:break;  
			            }  
			            //Èç¹ûxmlÃ»ÓÐœáÊø£¬ÔòµŒºœµœÏÂÒ»žöœÚµã  
			            evtType=xmlParser.next();  
			        }  
			    }   
			    catch (XmlPullParserException e) {  
			         // TODO Auto-generated catch block  
			         e.printStackTrace();  
			    }  
			    catch (IOException e1) {  
			         // TODO Auto-generated catch block  
			         e1.printStackTrace();  
			    }
		    woeid=woeidlStrings.get(0);
			    Log.i("lllllllllllllllllllll","woeidttttttttt  "+woeid); 
			    //threadweather(woeidst.get(0));
			    handler.sendEmptyMessage(0x03);
		}
			
		public void sendhttpgetweatherinfo(String woeidString) {
			String getData = null;
			Log.i("lllllllllllllllllllll", "sendhttpgetweatherinfo ");
			String stringtttt = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid="
					+ woeidString + "%20and%20u=%22c%22&format=json";

			try {

				//HttpClient httpClient = new DefaultHttpClient();
				HttpClient httpClient = MyHttpClient.getNewHttpClient();
				HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
						5000);// Á¬œÓ³¬Ê±ÉèÖÃ

				HttpGet httpGet = new HttpGet(stringtttt);

				HttpResponse httpResponse = httpClient.execute(httpGet);

				if (httpResponse.getStatusLine().getStatusCode() == 200) {

					HttpEntity entity = httpResponse.getEntity();
					getData = EntityUtils.toString(entity, "utf-8");
					Log.i("lllllllllllllllllllll", "µÃµœÒªœâÎöµÄÌìÆøÊýŸÝ ");
					parseJSONWith(getData);

				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}
		}

		
		// JSonœâÎö
		public void parseJSONWith(String jsondata) {
			/*if (weatherinformation != null) {
				weatherinformation.clear();
			}*/

			try {
				JSONObject object = new JSONObject(jsondata);
				// JSONObject
				// ciji=object.getJSONObject("query").getJSONObject("results").getJSONObject("item").getJSONObject("condition");
				String tttt = (String) object.getJSONObject("query").get("created");
				JSONObject channel = object.getJSONObject("query")
						.getJSONObject("results").getJSONObject("channel");

				String string = channel.getString("title");

				String city = channel.getJSONObject("location").getString("city");
				//weatherinformation.add(city);
				Log.i("women", "city     " + city);
				String temp = channel.getJSONObject("item")
						.getJSONObject("condition").getString("temp");

				//weatherinformation.add(temp);
				String weather = channel.getJSONObject("item")
						.getJSONObject("condition").getString("text");

				//weatherinformation.add(weather);
				String code = channel.getJSONObject("item")
						.getJSONObject("condition").getString("code");
				//weatherinformation.add(code);
				JSONArray forecastweatherArray=channel.getJSONObject("item")
						.getJSONArray("forecast");
				//ÎåÌìÌìÆøÔ€±šÐÅÏ¢
				cityweathers=new ArrayList<Cityweather>();
				if(cityweathers.size()!=0){
					cityweathers.clear();
				}
				  Cityweather cityweatherenty=null;
				    for (int i = 0; i < 5; i++) {
				    	cityweatherenty=new Cityweather();
				    	cityweatherenty.setCode(forecastweatherArray.getJSONObject(i).get("code").toString());
				    	cityweatherenty.setDay(forecastweatherArray.getJSONObject(i).get("day").toString());
				    	cityweatherenty.setHightemp(forecastweatherArray.getJSONObject(i).get("high").toString());
				    	cityweatherenty.setLowtemp(forecastweatherArray.getJSONObject(i).get("low").toString());
				    	cityweatherenty.setWeathertext(forecastweatherArray.getJSONObject(i).get("text").toString());
				    	cityweathers.add(cityweatherenty);
					}
				Log.i("lllllllllllllllllllll", "cityweathers "+cityweathers.size());
				Log.i("lllllllllllllllllllll", "œâÎö³öÌìÆøÊýŸÝ ");
				handler.sendEmptyMessage(0x01);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void threadweather(final String woeid) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					
					Log.i("lllllllllllllllllllll", "µÃµœweatherÏß³Ì  ");
					sendhttpgetweatherinfo(woeid);
				}

			}).start();
		}
}
