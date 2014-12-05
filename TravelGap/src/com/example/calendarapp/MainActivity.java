package com.example.calendarapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static final String[] EVENT_PROJECTION = new String[] {
		CalendarContract.Events.TITLE,
		CalendarContract.Events.EVENT_LOCATION,
		CalendarContract.Instances.BEGIN,
		CalendarContract.Instances.END,
		CalendarContract.Events.ALL_DAY
	};
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
	int i=0;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		contentResolver = getContentResolver();
		Uri uri = Calendars.CONTENT_URI;   
		String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND (" 
		                        + Calendars.ACCOUNT_TYPE + " = ?) AND ("
		                        + Calendars.OWNER_ACCOUNT + " = ?))";
		String email=getLoggedInAccount();
		
		String[] selectionArgs = new String[] {email, "com.google",
		        email}; 
		getEvents();
		
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
	
	
	
	 public void getEvents() {
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
		    
		    TextView t1=(TextView)findViewById(R.id.textView1);
		    t1.setText(freeTimeSlot[1]);
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
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
