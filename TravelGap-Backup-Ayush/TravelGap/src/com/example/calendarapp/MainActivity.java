package com.example.calendarapp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	public static final String[] EVENT_PROJECTION = new String[] {
		CalendarContract.Events.TITLE,
		CalendarContract.Events.EVENT_LOCATION,
		CalendarContract.Instances.BEGIN,
		CalendarContract.Instances.END,
		CalendarContract.Events.ALL_DAY
	};

	static final String KEY_SONG = "song"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "artist";
	static final String KEY_DURATION = "duration";
	static final String KEY_THUMB = "image";
	
	ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	
	// The indices for the projection array above.
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
	public static final String[] FIELDS = { CalendarContract.Calendars.NAME,
		  CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
		  CalendarContract.Calendars._ID,
		  CalendarContract.Calendars.VISIBLE };
	public static final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/calendars");
	Set<String> calendars = new HashSet<String>();
	ContentResolver contentResolver;
	TextView t1;
	long[] startDateTime=new long[256];
	long[] endDateTime=new long[256];
	double latitude=0,longitude=0;
	
	int i=0;
	 ListView list;
	  LazyAdapter adapter;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		contentResolver = getContentResolver();
		Uri uri = Calendars.CONTENT_URI;   
		String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND (" 
		                        + Calendars.ACCOUNT_TYPE + " = ?) AND ("
		                        + Calendars.OWNER_ACCOUNT + " = ?))";
		String email=getLoggedInAccount();
		
		String[] selectionArgs = new String[] {email, "com.google",
		        email}; 
		String[] timeSlots=getFreeTimeSlots();
		Bundle extras=getIntent().getExtras();
		String categories=null;
		if(extras!=null)
		{
			categories=extras.getString("Categories");
		}
		else 
			categories="empty";
		
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener ll = new mylocationlistener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		if(latitude==0||longitude==0)
		{
			latitude=12.972600;
			longitude=77.638381;		
		}
		
		List<Event> eventObjs=new ArrayList();
		EventsObject eventObj=new EventsObject(latitude,longitude,timeSlots,eventObjs);
		String[] params=new String[3];
		
		eventObj.execute(params);
		list=(ListView)findViewById(R.id.list);
		
		ArrayList<String> titles=StaticData.getTitles();
		String[] distance={"12.2km","11.1km","13.4km","9.8km","7.5km","6.5km","17.4km","19.2km","5.4km","10.2"};
		String[] duration={"55min","52min","1hr2min","40min","28min","21min","1hr25min","1hr35min","14min","48min"};
		Integer[] images={R.drawable.f1,R.drawable.f3,R.drawable.f7,R.drawable.f9,R.drawable.f11};
		
		for (int i = 0; i < 10; i++) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				// adding each child node to HashMap key => value
				map.put(KEY_ID, "Event_Title");
				map.put(KEY_TITLE, titles.get(i));
				map.put(KEY_ARTIST, "Distance :"+distance[i]+" Duration :"+duration[i]);
				map.put(KEY_THUMB, images[i].toString());
				// adding HashList to ArrayList
				songsList.add(map);
			}
			

			list=(ListView)findViewById(R.id.list);
			
			// Getting adapter by passing xml data ArrayList
	        adapter=new LazyAdapter(this, songsList);        
	        list.setAdapter(adapter);
	        

	        // Click event for single list row
	        list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
								
					
				}
			});		

	}
	
	
	
	
	  private class mylocationlistener implements LocationListener {
		    @Override
		    public void onLocationChanged(Location location) {
		        if (location != null) {
		        	latitude=location.getLatitude();
		        	longitude=location.getLongitude();
		        }
		    }
		    
		    @Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
	  }	    
	
	public String getLoggedInAccount()
	{
		AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
		Account[] list = manager.getAccounts();
		String gmail = null;

		for(Account account: list)
		{
		    if(account.type.equalsIgnoreCase("com.google"))
		    {
		        gmail = account.name;
		        break;
		    }
		}
		return gmail;
	}
	
	
	
	 public String[] getFreeTimeSlots() {
		    // Fetch a list of all calendars sync'd with the device and their display names
		  Cursor cursor = contentResolver.query(CALENDAR_URI, FIELDS, null, null, null);

		    try {
		      if(cursor.getCount() > 0) {
		        while (cursor.moveToNext()) {
		          String name = cursor.getString(0);
		          String displayName = cursor.getString(1);
		          System.out.println(name+" "+displayName);
		          // This is actually a better pattern:
		          String calId = cursor.getString(cursor.getColumnIndex(
		                  CalendarContract.Calendars._ID));
		          Uri.Builder eventsUriBuilder=CalendarContract.Instances.CONTENT_URI.buildUpon();
		  		
		        ContentUris.appendId(eventsUriBuilder,new Date().getTime() );
		  		ContentUris.appendId(eventsUriBuilder, new Date().getTime()+DateUtils.WEEK_IN_MILLIS);
		  		
		  		Uri eventUri=eventsUriBuilder.build();
		  		String selection1="(("+Events.CALENDAR_ID+"=?))";
		  		String[] selectionArgs1=new String[]{calId};
		  		Cursor eventCursor=contentResolver.query(eventUri, EVENT_PROJECTION, selection1, selectionArgs1, CalendarContract.Instances.BEGIN+" ASC");
		  		String[] eventsArr=new String[eventCursor.getCount()];
		  		if(eventCursor.getCount()>0) {
		  				System.out.println("Here it is ");
		  				while(eventCursor.moveToNext())
		  				{
		  					if(eventCursor.getString(4).equals("0"))
		  					{
		  						startDateTime[i]=Long.parseLong(eventCursor.getString(2));
		  						endDateTime[i]=Long.parseLong(eventCursor.getString(3));
		  						i++;
		  					}
		  				}
		  			}
		  			
		  		}
		      }
		    } catch (AssertionError ex) {
		      // TODO: log exception and bail
		    }
		    
		    
		    String[] freeTimeSlot=new String[256];
		    SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
		    int ind=0;
		    String startDate=getDate(startDateTime[0],"dd/MM/yyyy hh:mm aa").split(" ")[0]+" 12:01 AM";
		    String endDate=getDate(endDateTime[i-1],"dd/MM/yyyy hh:mm aa").split(" ")[0]+" 11:59 PM";
			SimpleDateFormat sdf1=new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
		    long startMillis=0,endMillis = 0;
			
		    
			try {
				Date date1=sdf1.parse(startDate);
				Date date2=sdf1.parse(endDate);
				startMillis=date1.getTime();
				endMillis=date2.getTime();
				
				if(startMillis!=startDateTime[0])
				{
					freeTimeSlot[ind++]=startDate+"->"+getDate(startDateTime[0], "dd/MM/yyyy hh:mm aa");
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    for(int j=0,k=1;k<i;j++,k++)
		    {
		    	if(endDateTime[j]!=startDateTime[k])
		    		freeTimeSlot[ind++]=getDate(endDateTime[j], "dd/MM/yyyy hh:mm aa")+"->"+getDate(startDateTime[k], "dd/MM/yyyy hh:mm aa");
		    }
		    if(endMillis>endDateTime[i-1])
		    {
		    	freeTimeSlot[ind++]=getDate(endDateTime[i-1], "dd/MM/yyyy hh:mm aa")+"->"+endDate;
		    }
		    
		    return freeTimeSlot;
	 }

	 public static String getDate(long milliSeconds, String dateFormat)
		{
		    // Create a DateFormatter object for displaying date in specified format.
		    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

		    // Create a calendar object that will convert the date and time value in milliseconds to date. 
		     Calendar calendar = Calendar.getInstance();
		     calendar.setTimeInMillis(milliSeconds);
		     return formatter.format(calendar.getTime());
		}
	

	

	
}
