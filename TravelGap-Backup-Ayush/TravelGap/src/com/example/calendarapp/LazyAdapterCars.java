package com.example.calendarapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapterCars extends ArrayAdapter<CarPojo> {
	private Context context;
	public LazyAdapterCars(ArrayList<CarPojo> itemList, Context ctx) {
		super(ctx, android.R.layout.list_content, itemList);
		this.itemList = itemList;
		this.context = ctx;		
	}
	private Activity activity;
	 private ArrayList data;
	 private static LayoutInflater inflater=null;
	 private ArrayList<CarPojo> itemList;
	 private int i=0;
 

	/* LazyAdapter(MainActivity parserTask, ArrayList<CarPojo> carList) {
		// TODO Auto-generated constructor stub
		 activity = parserTask;
		 data=carList;
		 inflater = (LayoutInflater)parserTask.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}*/

	@Override
	public int getCount() {
		if (itemList != null)
			return itemList.size();
		return 0;
	}

	@Override
	public CarPojo getItem(int position) {
		if (itemList != null)
			return itemList.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (itemList != null)
			return itemList.get(position).hashCode();
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 View vi=convertView;
	        if(convertView==null){
	        	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				vi = inflater.inflate(R.layout.list_row1, null);
	        }
	       
	        TextView name = (TextView)vi.findViewById(R.id.carName); // title
	        TextView currency = (TextView)vi.findViewById(R.id.currency); // artist name
	        TextView cost = (TextView)vi.findViewById(R.id.cost); // duration
	        //ImageView thumb_image=(ImageView)vi.findViewById(R.drawable.big); // thumb image
	        
	      // ArrayList<CarPojo> cars = new ArrayList<CarPojo>();
	       CarPojo carPojo1 = (CarPojo) itemList.get(0);
	       CarPojo carPojo2 = (CarPojo) itemList.get(1);
			//CarPojo carPojo2 = (CarPojo) data.get(1);
			/*String[] carNames = new String[] {
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
			};*/
			
	        // Setting all values in listview
	       if(i==0){
	    	   name.setText(carPojo1.getCategory());
		        currency.setText(carPojo1.getCurrency());
		        cost.setText(carPojo1.getCost());
	    	   i++;
	       }
	       
	       else if(i==1){
	    	   name.setText(carPojo1.getCategory());
		        currency.setText(carPojo1.getCurrency());
		        cost.setText(carPojo1.getCost());
	    	   i++;
	       }
	       
	       else if(i==2){
	    	   name.setText(carPojo2.getCategory());
		        currency.setText(carPojo2.getCurrency());
		        cost.setText(carPojo2.getCost());
	       }
	       
	       else if(i==3){
	    	   name.setText("Standard");
		        currency.setText("USD");
		        cost.setText("56.05");
	    	   i++;
	       }
	       
	       else if(i==3){
	    	   name.setText("Intermediate");
		        currency.setText("USD");
		        cost.setText("76.05");
	    	   i++;
	       }
	       
	       
	     
	       
	       
	        //thumb_image.setId(R.drawable.big);
	       
	       
	        return vi;
	}
	 public void setItemList(ArrayList<CarPojo> itemList) {
	        this.itemList = itemList;
	    }


}

