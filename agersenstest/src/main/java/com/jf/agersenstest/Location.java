package com.jf.agersenstest;

import javax.validation.ValidationException;

public class Location {

	Double lat;
	Double lng;
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public void validate(String id) throws ValidationException {
		
		if (lat == null)
			new ValidationException("animal "+id+" location latitude not set");
		if (lng == null)
			new ValidationException("animal "+id+" location longitude not set");
		//lat +90 to -90 long +180 to -180
		if (lat.doubleValue() < -90.0 || lat.doubleValue() > 90.0)
			new ValidationException("animal "+id+" location latitude invalid value");
		if (lng.doubleValue() < -90.0 || lng.doubleValue() > 90.0)
			new ValidationException("animal "+id+" location longitude invalid value");
	}
	
	
}
