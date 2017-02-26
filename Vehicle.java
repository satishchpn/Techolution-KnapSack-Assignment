package com.test;

import java.time.LocalTime;
public class Vehicle implements Comparable<Vehicle>{
	private String companyName;
	private LocalTime departureTime;
	private LocalTime arrivalTime;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public LocalTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(LocalTime departureTime) {
		this.departureTime = departureTime;
	}

	public LocalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	@Override
	public int compareTo(Vehicle vehicle) {
		return departureTime.compareTo((vehicle).departureTime);
	}

	@Override
	public String toString() {
		return companyName + " " + departureTime + " " + arrivalTime;
	}
}
