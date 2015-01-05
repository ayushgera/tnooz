package com.example.calendarapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;



public class CarMainActivity extends Activity {
	TextView json;
	TextView cost1;
	TextView currency1;
	TextView category1;
	TextView cost2;
	TextView currency2;
	TextView category2;
	ArrayList<CarPojo> arrayList;
	private LazyAdapterCars adaptor;
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cars_main);
		
		//JSONObject jsonObject = parseCars();
		adaptor = new LazyAdapterCars(new ArrayList<CarPojo>(),this);
		ListView lView = (ListView) findViewById(R.id.list);
        lView.setAdapter(adaptor);
        String url = getDirectionsUrl();
		DownloadTask downloadTask = new DownloadTask();
		downloadTask.execute(url);
		
		
	/*	list=(ListView)findViewById(R.id.list);
        adapter = new LazyAdapter(this, arrayList);
	     list.setAdapter(adapter);
		Log.d("cars", arrayList.toString());
		String carurl = "http://www.avis.co.in";*/
		//System.out.println(arrayList);
	}

	private String getDirectionsUrl() {
		// TODO Auto-generated method stub
		String url = "http://api.sandbox.amadeus.com/v1.2/cars/search-circle?pick_up=2014-12-22&drop_off=2014-12-23&latitude=12.971599&longitude=77.594563&radius=50&providers=ZI&currency=USD&apikey=G3uHAhdGYFOVwv17t7iK0BLcwlCQavyh";
		return url;
	}

	/*JSONObject parseCars() {
		try {
			String url = "http://api.sandbox.amadeus.com/v1.2/cars/search-circle?pick_up=2014-12-22&drop_off=2014-12-23&latitude=12.971599&longitude=77.594563&radius=50&providers=ZI&currency=USD&apikey=G3uHAhdGYFOVwv17t7iK0BLcwlCQavyh";
			URLConnection urlConn = new URL(url).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));
			StringBuffer sbf = new StringBuffer();
			String inLine;
			while ((inLine = in.readLine()) != null) {
				sbf.append(inLine);
			}
			sbf.toString();
			// json = (TextView) findViewById(R.id.textView);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}*/
	
	
	private class DownloadTask extends AsyncTask<String, Void, ArrayList<CarPojo>> {
		 private final ProgressDialog dialog = new ProgressDialog(CarMainActivity.this);
	    protected void onPreExecute() {       
	        super.onPreExecute();
	        dialog.setMessage("Showing Cars...");
	        dialog.show();           
	    }

		// Downloading data in non-ui thread
		@Override
		protected ArrayList<CarPojo> doInBackground(String... url) {

			// For storing data from web service
			String data = "";
			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			
			String[] array = {data};
			Parser parserTask = new Parser();
			ArrayList<CarPojo> cars = null;

			// Invokes the thread for parsing the JSON data
			cars = parserTask.background(array);
			return cars;
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


		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(ArrayList<CarPojo> result) {
			super.onPostExecute(result);
			dialog.dismiss();
			CarPojo carPojo3 = new CarPojo();
			carPojo3.setCategory("sdas");
			carPojo3.setCost("ds");
			carPojo3.setCurrency("USD");
			
			CarPojo carPojo4 = new CarPojo();
			carPojo4.setCategory("sdas");
			carPojo4.setCost("ds");
			carPojo3.setCurrency("USD");
			result.add(carPojo3);
			result.add(carPojo4);
			adaptor.setItemList(result);
	        adaptor.notifyDataSetChanged();
			//Intent intent_name = new Intent();
			//intent_name.setClass(getApplicationContext(),CarView.class);
			//intent_name.putExtra("cars",result);
			//startActivity(intent_name);
			
			//Intent intent=new Intent(getApplicationContext(),CarView.class);
			//intent.putExtra("cars",result);
			//startActivity(intent);
			
			
			
		}
	}
	
	
	/** A class to parse the Google Places in JSON format */
	private class Parser {

		protected ArrayList<CarPojo> background(String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(jsonData[0]);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DirectionsJSONParserCars directionsJSONParserCars = new DirectionsJSONParserCars();
				arrayList = directionsJSONParserCars.parse(jsonObject);
				//getCars(arrayList);
				// Starts parsing data
				//routes = parser.parse(jObject);
				/*list=(ListView)findViewById(R.id.list);
		        adapter = new LazyAdapter(getParent(), arrayList);
			     list.setAdapter(adapter);
				Log.d("cars", arrayList.toString());
				String carurl = "http://www.avis.co.in";*/
			
			return arrayList;
		}

		/*private void getCars(ArrayList<CarPojo> arrayList) {
			CarPojo carPojo1 = arrayList.get(0);
			CarPojo carPojo2 = arrayList.get(1);
			String[] carNames = new String[] {
					carPojo1.getCategory(),
					carPojo2.getCategory()
			    };
			
			String[] currency = new String[] {
					carPojo1.getCurrency(),
					carPojo2.getCategory()
			    };
			
			String[] cost = new String[] {
					carPojo1.getCost(),
					carPojo2.getCost()
			    };
			
			int[] carsPic = new int[]{
					R.drawable.big,
					R.drawable.small
			};
			Log.d("cars", arrayList.toString());
			String carurl = "http://www.avis.co.in";
			//System.out.println(arrayList);
			 List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
			 for(int i=0;i<2;i++){
		            HashMap<String, String> hm = new HashMap<String,String>();
		            hm.put("txt", "Country : " + carNames[i]);
		            hm.put("cur","Currency : " + currency[i]);
		            hm.put("flag", Integer.toString(carsPic[i]) );
		            aList.add(hm);
		        }
			 // Keys used in Hashmap
		        String[] from = { "flag","txt","cur" };
		 
		        // Ids of views in listview_layout
		        int[] to = { R.id.flag,R.id.txt,R.id.cur};
		 
		        // Instantiating an adapter to store each items
		        // R.layout.listview_layout defines the layout of each item
		        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_layout, from, to);
		 
		        // Getting a reference to listview of main.xml layout file
		        ListView listView = (ListView)findViewById(R.id.listview);
		 
		        // Setting the adapter to the listView
		        listView.setAdapter(adapter);
			
		}
*/
		// Executes in UI thread, after the parsing process
		/*@Override
		protected void onPostExecute(ArrayList<CarPojo>  result) {
			
			CarPojo carPojo1 = arrayList.get(0);
			CarPojo carPojo2 = arrayList.get(1);
			String[] carNames = new String[] {
					carPojo1.getCategory(),
					carPojo2.getCategory()
			    };
			
			String[] currency = new String[] {
					carPojo1.getCurrency(),
					carPojo2.getCategory()
			    };
			
			String[] cost = new String[] {
					carPojo1.getCost(),
					carPojo2.getCost()
			    };
			
			int[] carsPic = new int[]{
					R.drawable.big,
					R.drawable.small
			};
			*/
			
			
			/* list=(ListView)findViewById(R.id.list);
	          adapter = new LazyAdapter(parserTask, arrayList);
		      list.setAdapter(adapter);
			Log.d("cars", arrayList.toString());
			String carurl = "http://www.avis.co.in";*/
			//System.out.println(arrayList);
			
	}

	}

