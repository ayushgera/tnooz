package com.example.calendarapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.distanceandtime.R;
import com.google.android.gms.games.event.Event;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EventInformationView extends Activity {
	Button btn2;
	ImageView img;
	String picName;
	TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_description);
		
		btn2 = (Button)findViewById(R.id.button1);
		img = (ImageView)findViewById(R.id.imageView1);
		title= (TextView)findViewById(R.id.textView2);
		// The server says, it should be *.png
		
		Intent i= getIntent();
		TextView t1= (TextView)findViewById(R.id.textView1);
		
		int index =2;//(Integer)i.getExtras().get("index");
		if(index%2==0){
			if(index!=0){
				picName = "m"+(index);
				t1.setText(StaticData.getDescriptions().get(index));	
			}else{
				picName = "m9";
				t1.setText(StaticData.getDescriptions().get(9));
			}
			
		}else {
			picName="f"+(index);
			t1.setText(StaticData.getDescriptions().get(index));	;
		}
		
		
		//picName = picName.replace(".png", "");

		Resources r = getResources();
		int picId = r.getIdentifier(picName, "drawable", "com.example.distanceandtime");

		//ImageView image = (ImageView)v.findViewById(R.id.relativeLayout);
		//image.setBackgroundResource(picId);
		
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
		//Resources res = getResources(); //resource handle
		//Drawable drawable = res.getDrawable(R.drawable.newImage); //new Image that was added to the res folder
		rLayout.setBackgroundResource(picId);
		//rLayout.setBackgroundDrawable(drawable);
		
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
					
				// Launch Main Activity
				Intent i = new Intent(getApplicationContext(), CarMainActivity.class);
				
				startActivity(i);
				finish();
					
			}
				
		});
		
		img.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				// Launch Main Activity
				Intent i = new Intent(getApplicationContext(), DistanceAndTimeActivity.class);
				
				// Registering user on our server					
				// Sending registraiton details to MainActivity
				LatLng ori = new LatLng(79.989,10.56);
				i.putExtra("latlng", ori);
				startActivity(i);
				finish();
				
					
			}
				
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
