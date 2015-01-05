package com.example.calendarapp;

public class Event {
	
	private String longitude;
	private String latitude;
	private String city_name;
	private String id;
	private String title;
	private String description;
	private String start_time;
	private String stop_time;
	private String venu_address;
	private String venu_name;
	private String imageUrl;
	private double duration;
	private double distance;
	private int all_day;
	private String category;
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getStop_time() {
		return stop_time;
	}
	public double getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public void setStop_time(String stop_time) {
		this.stop_time = stop_time;
	}
	
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getVenu_address() {
		return venu_address;
	}
	public void setVenu_address(String venu_address) {
		this.venu_address = venu_address;
	}
	public String getVenu_name() {
		return venu_name;
	}
	public void setVenu_name(String venu_name) {
		this.venu_name = venu_name;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public int getAll_day() {
		return all_day;
	}
	public void setAll_day(int all_day) {
		this.all_day = all_day;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	
	

}
