package com.example.calendarapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DirectionsJSONParserCars {


	public ArrayList<CarPojo> parse(JSONObject jObject) {
		ArrayList<CarPojo> routes = new ArrayList<CarPojo>();
		// TODO Auto-generated method stub
		
		 JSONArray jResult= null;
		 JSONObject jsonCar1 = null; 
		 JSONObject jsonCar2 = null;
		 JSONObject totalPrice1= null;
		 JSONObject totalPrice2= null;
		 String cost1 = null;
		 String cost2 = null;
		 String category1 = null;
		 String category2 = null;
		 String currency1 = null;
		 String currency2 = null;
		 
		 //JSONArray jCars= null;
        try {
        	jResult = jObject.getJSONArray("results");
        	for(int i=0;i<jResult.length();i++)
        	{
        		jObject=(JSONObject)jResult.get(i);
        		JSONArray cars=jObject.getJSONArray("cars");
        		jsonCar1 = (JSONObject) cars.get(0);
        		totalPrice1 = (JSONObject) jsonCar1.get("estimated_total");
        		cost1 = totalPrice1.getString("amount");
        		currency1 =  totalPrice1.getString("currency");
        		category1 = "Compact";
        		jsonCar2 = (JSONObject) cars.get(1);
        		totalPrice2 = (JSONObject) jsonCar2.get("estimated_total");
        		cost2 = totalPrice2.getString("amount");
        		currency2 =  totalPrice2.getString("currency");
        		category2 = "Intermediate";
        		
        		CarPojo carPojo1 = new CarPojo();
        		carPojo1.setCategory(category1);
        		carPojo1.setCost(cost1);
        		carPojo1.setCurrency(currency1);
        		
        		CarPojo carPojo2 = new CarPojo();
        		carPojo2.setCategory(category2);
        		carPojo2.setCost(cost2);
        		carPojo2.setCurrency(currency2);
        		
        		routes.add(carPojo1);
        		routes.add(carPojo2);
        		
        		
        	}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return routes;
	}
}