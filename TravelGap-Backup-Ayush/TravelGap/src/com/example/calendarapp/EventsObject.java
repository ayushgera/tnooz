package com.example.calendarapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.graphics.Color;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;


public class EventsObject extends AsyncTask<String,Void,String>{
	
	List<Event> eventsObjs=new ArrayList();
	double latitude,longitude;
	double distance,duration=0;
	String[] timeSlots;
	EventsObject(double latitude,double longitude,String[] timeSlots,List<Event> eventObj)
	{
		this.latitude=latitude;
		this.longitude=longitude;
		this.timeSlots=new String[timeSlots.length];
		for(int i=0;i<timeSlots.length;i++)
		{
			this.timeSlots[i]=timeSlots[i];
		}
		this.eventsObjs=eventObj;
	}
	
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	public static double randLong(double min, double max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    
	    double randomNum = rand.nextDouble()%max-rand.nextDouble()%min+min;

	    return randomNum;
	}
	
		public List<Event> getEvents()
		{
			try {
				URLConnection urlConn = new URL("http://api.eventful.com/json/events/search?app_key=6v2qKKF676QjNdPd&&where=12.972600,77.638381&category=books,music,food&location=bangalore&within=25&include=categories&date=2014120700-2014120700").openConnection();
				BufferedReader in=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				
				StringBuffer sbf=new StringBuffer();
				String inLine;
				while((inLine=in.readLine())!=null)
				{
					sbf.append(inLine);
				}
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = null;
				try {
					jsonObject = (JSONObject)jsonParser.parse(sbf.toString());
				} catch (org.json.simple.parser.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String events=jsonObject.get("events").toString();
				try {
					jsonObject=(JSONObject)jsonParser.parse(events);
				} catch (org.json.simple.parser.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONArray jsonArr=(JSONArray)jsonObject.get("event");
				Iterator i=jsonArr.iterator();
				while(i.hasNext())
				{
					String event=i.next().toString();
					try {
						jsonObject=(JSONObject)jsonParser.parse(event);
					} catch (org.json.simple.parser.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Event eventObj=new Event();
					
					eventObj.setLatitude((jsonObject.get("latitude")==null)?"empty":jsonObject.get("latitude").toString());
					eventObj.setLongitude((jsonObject.get("longitude")==null)?"empty":jsonObject.get("longitude").toString());
					//String carurl = getCarUrl(ori, dest);
                    
                    eventObj.setDistance(randInt(10,20)); 
                    eventObj.setCity_name((jsonObject.get("city_name").toString()==null)?"empty":jsonObject.get("city_name").toString());
					eventObj.setId((jsonObject.get("id").toString()==null)?"empty":jsonObject.get("id").toString());
					eventObj.setDuration((randLong(1800000, 3600000))*2);
					eventObj.setTitle((jsonObject.get("title")==null)?"empty":jsonObject.get("title").toString());
					eventObj.setDescription((jsonObject.get("description")==null)?"empty":jsonObject.get("description").toString());
					eventObj.setStart_time((jsonObject.get("start_time")==null)?"empty":jsonObject.get("start_time").toString());
					eventObj.setVenu_address((jsonObject.get("venue_address")==null)?"empty":jsonObject.get("venue_address").toString());
					eventObj.setDescription((jsonObject.get("stop_time")==null)?"empty":jsonObject.get("stop_time").toString());
					eventObj.setVenu_name((jsonObject.get("venue_name")==null)?"empty":jsonObject.get("venue_name").toString());
					
					
					/*String categories=jsonObject.get("categories").toString();
					try {
						jsonObject=(JSONObject)jsonParser.parse(categories);
					} catch (org.json.simple.parser.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    jsonArr=(JSONArray)jsonObject.get("category");
					 i=jsonArr.iterator();
					while(i.hasNext())
					{
						String category=i.next().toString();
						try {
							jsonObject=(JSONObject)jsonParser.parse(category);
							eventObj.setCategory(jsonObject.get("name").toString());
						} catch (org.json.simple.parser.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}	
					*/
					
					
					/*if(addEventObject(eventObj, timeSlots))
					eventsObjs.add(eventObj);
					*/
				}
	
				
			 } catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return eventsObjs;
	}

		
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<Event> events=getEvents();
			return null;
		}
		boolean addEventObject(Event eventObj, String[] freetimeSlots) {
			int alldayEvent = eventObj.getAll_day();
			if (alldayEvent != 0)
				return false;
			double toFroduration = eventObj.getDuration();

			// ---logic to calculate difference in event duration
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
			String eventStartDate = eventObj.getStart_time();
			String eventStopDate = eventObj.getStop_time();

			try {
				// startdate time and end date time of event
				Date eventTimeStart = formatter.parse(eventStartDate);
				Date eventTimeStop = formatter.parse(eventStopDate);
				for (int i = 0; i < freetimeSlots.length; i++) {
					String freeslot = freetimeSlots[i];
					String[] slots = freeslot.split("->");
					SimpleDateFormat formatter2 = new SimpleDateFormat(
							"dd-mm-yyyy hh:mm a");
					String startFreeSlot = slots[0];
					String stopFreeSlot = slots[1];

					Date startTimeFreeSlot = formatter2.parse(startFreeSlot);
					Date stopTimeFreeSlot = formatter2.parse(stopFreeSlot);
					if (((eventTimeStart.getTime() - toFroduration / 2) > startTimeFreeSlot
							.getTime())
							&& ((eventTimeStop.getTime() + toFroduration / 2) < stopTimeFreeSlot
									.getTime())) {
						return true;
					}

				}

			} catch (ParseException e) {
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return false;

		}

 
}

