package com.example.zotrides;

public class Car {
	// track number of missing, required tags
	public static int numMissing = 0;

	// private member variables
	private String make;
	private String model;
	private int year;
	private String category;
	private int id;

	// default constructor
	public Car(){
		this.year = -1;
		this.id = -1;
	}

	public Car(String make, String model, int year, String category, int id) {
		this.make = make;
		this.model = model;
		this.year = year;
		this.category = category;
		this.id  = id;
	}

	// accessor & mutator methods
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getYear() { return year; }
	public void setYear(int year) {
		this.year = year;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getId() { return id; }
	public void setId(int id) {
		this.id = id;
	}

	// check if Car is valid to add to database
	public boolean isInconsistent() {
		boolean isInconsistent = false;
		if (category == null) {
			System.out.println("Missing required tag <category>");
			isInconsistent = true;
			++numMissing;
		}
		if (make == null) {
			System.out.println("Missing required tag <make>");
			isInconsistent = true;
			++numMissing;
		}
		if (model == null) {
			System.out.println("Missing required tag <model>");
			isInconsistent = true;
			++numMissing;
		}
		if (year == -1) {
			System.out.println("Missing required tag <year>");
			isInconsistent = true;
			++numMissing;
		}
		if (id == -1) {
			System.out.println("Missing required tag <id>");
			isInconsistent = true;
			++numMissing;
		}
		return isInconsistent || category.isEmpty() || make.isEmpty() || model.isEmpty() || year == -2 || id == -2;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Employee Details - ");
		sb.append("Make:" + getMake());
		sb.append(", ");
		sb.append("Model:" + getModel());
		sb.append(", ");
		sb.append("Year:" + getYear());
		sb.append(", ");
		sb.append("Category:" + getCategory());
		sb.append(", ");
		sb.append("ID:" + getId());
		sb.append(".");
		
		return sb.toString();
	}

	public static int getNumMissing(){
		return numMissing;
	}

	public static void resetMissing() {
		numMissing = 0;
	}
}
