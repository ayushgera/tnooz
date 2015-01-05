package com.example.calendarapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateUtils;



public class CalendarService {
	
	public static void readCalendar(Context context) {
		readCalendar(context, 1, 0);
	}

	public static void readCalendar(Context context, int days, int hours) {
		
		ContentResolver contentResolver = context.getContentResolver();
		 
		Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/events"), 
		 new String[]{ "calendar_id", "title", "description", "dtstart", "dtend", "eventLocation" }, 
		 null, null, null);
		
		
		
		HashSet<String> calendarIds = getCalenderIds(cursor);
		
		HashMap<String, List<CalendarEvent>> eventMap = new HashMap<String, List<CalendarEvent>>();
		
		for (String id : calendarIds) {
			
			Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
	        long now = new Date().getTime();

	        ContentUris.appendId(builder, now - (DateUtils.DAY_IN_MILLIS * days) - (DateUtils.HOUR_IN_MILLIS * hours));
	        ContentUris.appendId(builder, now + (DateUtils.DAY_IN_MILLIS * days) + (DateUtils.HOUR_IN_MILLIS * hours));
	        
	        Cursor eventCursor = contentResolver.query(builder.build(),
	                new String[]  { "title", "begin", "end", "allDay"}, "Calendars._id=" + id,
	                null, "startDay ASC, startMinute ASC");
	        
	        System.out.println("eventCursor count="+eventCursor.getCount());
	        
	        if(eventCursor.getCount()>0)
	        {
	        	
	        	List<CalendarEvent> eventList = new ArrayList<CalendarEvent>();
	        			
	        	eventCursor.moveToFirst();
	            
	            CalendarEvent ce = loadEvent(eventCursor);
	            
	            eventList.add(ce);

	            System.out.println(ce.toString());

	            while (eventCursor.moveToNext())
	            {
	            	
	            	ce = loadEvent(eventCursor);
	            	eventList.add(ce);
	            	
	            	System.out.println(ce.toString());
	            	
	            }
	            
	    	    Collections.sort(eventList);
	    	    eventMap.put(id, eventList);
	    	    
	    	    System.out.println(eventMap.keySet().size() + " " + eventMap.values());
	    	    
	        }
		}
	}
	
	private static CalendarEvent loadEvent(Cursor csr) {
		return new CalendarEvent(csr.getString(0), 
						new Date(csr.getLong(1)),
						new Date(csr.getLong(2)), 
						!csr.getString(3).equals("0"));
	}
	
	private static HashSet<String> getCalenderIds(Cursor cursor) {
		
		HashSet<String> calendarIds = new HashSet<String>();
		
		try
	    {
	        
			if(cursor.getCount() > 0)
	        {
	        	
	        	while (cursor.moveToNext()) {
	
		             String _id = cursor.getString(0);
		             String displayName = cursor.getString(1);
		             Boolean selected = !cursor.getString(2).equals("0");
	
		            System.out.println("Id: " + _id + " Display Name: " + displayName + " Selected: " + selected);
		            calendarIds.add(_id);
		            
		        	}
	        } 
	    }
		
	    catch(AssertionError ex)
	    {
	        ex.printStackTrace();
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
		
		return calendarIds;
		
	}
}
