package com.example.calendarapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
 
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
 
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class DistanceAndTimeActivity extends FragmentActivity {
 
    GoogleMap map;
   //ArrayList<LatLng> markerPoints;
    TextView tvDistanceDuration;
    Location origin;
    LatLng point;
    //
    RadioButton rbDriving;
    RadioButton rbBiCycling;
    RadioButton rbWalking;
    RadioGroup rgModes;
    int mMode=0;
    final int MODE_DRIVING=0;
    final int MODE_BICYCLING=1;
    final int MODE_WALKING=2;
    String url;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        
         rbDriving = (RadioButton) findViewById(R.id.rb_driving);
 
        // Getting reference to rb_bicylcing
        rbBiCycling = (RadioButton) findViewById(R.id.rb_bicycling);
 
        // Getting reference to rb_walking
        rbWalking = (RadioButton) findViewById(R.id.rb_walking);
 
        // Getting Reference to rg_modes
        rgModes = (RadioGroup) findViewById(R.id.rg_modes);
        
 
        tvDistanceDuration = (TextView) findViewById(R.id.tv_distance_time);
 
        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
 
        // Getting Map for the SupportMapFragment
        map = fm.getMap();
 
        // Enable MyLocation Button in the Map
        map.setMyLocationEnabled(true);
        
        // Getting latlong of the event selected from previous event list
     		Intent i = getIntent();
     		
     		point = (LatLng)i.getExtras().get("latlng");
 
                       // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();
 
                // Setting the position of the marker
                options.position(point);
 
                /**
                * For the start location, the color of marker is GREEN and
                * for the end location, the color of marker is RED.
                */
                //if(markerPoints.size()==1){
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                //}
                
            
                // Add new marker to the Google Map Android API V2
                map.addMarker(options);
 
                // Checks, whether  end location is  captured
               //if(markerPoints.size()==1){
                    //LatLng origin = markerPoints.get(0);
                	
                	 //geo location
                  
                    //LatLng dest = markerPoints.get(0);
                    //LatLng dest2 = markerPoints.get(1);
 
                    // Getting URL to the Google Directions API
                    
                    new ShowLocation().execute();
                    
                       }
    
    
   
    protected String getCarUrl(LatLng ori, LatLng dest) {
		// TODO Auto-generated method stub
    	
    	String url = "http://api.sandbox.amadeus.com/v1.2/cars/search-circle?pick_up=2014-12-22&drop_off=2014-12-23&latitude=12.971599&longitude=77.594563&radius=50&providers=ZI&currency=USD&apikey=G3uHAhdGYFOVwv17t7iK0BLcwlCQavyh";
    	//Date dateStart = new Date(2014-12-30);
    	//Date dateEnd = new Date(2014-12-31);
    	
		return url;
	}





	private String getDirectionsUrl(LatLng origin,LatLng dest){
 
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
 
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
 
        // Sensor enabled
        String sensor = "sensor=false";
        
        
        // Travelling Mode
        String mode = "mode=driving";
 
        if(rbDriving.isChecked()){
            mode = "mode=driving";
            mMode = 0 ;
        }else if(rbBiCycling.isChecked()){
            mode = "mode=transit";
            mMode = 1;
        }else if(rbWalking.isChecked()){
            mode = "mode=walking";
            mMode = 2;
        }
 
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode;
 
        // Output format
        String output = "json";
 
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
 
        return url;
    }
 
	
    /** A method to download json data from url */
    private void downloadUrl2() throws IOException{
        String data = "http://127001:8001?regId=343534643567&time=45324";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(data);
 
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
 
            // Connecting to url
            urlConnection.connect();
 
            // Reading data from url
            
          ;
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        
    }
    
	
	
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
 
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
 
            // Connecting to url
            urlConnection.connect();
 
            // Reading data from url
            iStream = urlConnection.getInputStream();
 
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
 
            StringBuffer sb  = new StringBuffer();
 
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
 
            data = sb.toString();
 
            br.close();
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    
    private class ShowLocation extends AsyncTask<String, Void, String>{

		public ShowLocation() {
			// TODO Auto-generated constructor stub
			int f=0;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			origin = map.getMyLocation();
			return null;
		}
    	
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			url = getDirectionsUrl(new LatLng(origin.getLatitude(), origin.getLongitude()), point);
			DownloadTask downloadTask = new DownloadTask();
            
            
			 
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
		}
    	
    }
    
    
 
    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{
 
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
 
            // For storing data from web service
            String data = "";
 
            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
 
        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
 
            ParserTask parserTask = new ParserTask();
 
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
 
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
 
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
 
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
 
            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
 
                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }
 
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            String distance = "";
            String duration = "";
 
            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }
 
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
 
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
 
                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
 
                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }
 
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
 
                    points.add(position);
                }
 
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }
 
            tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);
 
            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
