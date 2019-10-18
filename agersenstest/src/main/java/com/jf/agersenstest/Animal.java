package com.jf.agersenstest;

import javax.validation.ValidationException;

public class Animal {

	private String name;
	private Integer months;
	private Location location;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMonths() {
		return months;
	}
	public void setMonths(int months) {
		this.months = months;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public void validate(String id) throws ValidationException {
		
		if (name == null || name.isBlank())
			throw new ValidationException("animal "+id+" name not set");
		if (months == null)
			throw new ValidationException("animal "+id+" months not set");
		if (months.intValue() < 0)
			throw new ValidationException("animal "+id+" months illegal value");
		if (location == null)
			throw new ValidationException("animal "+id+" location  not set");
		
		location.validate(id);
		
	}
	
	
}
