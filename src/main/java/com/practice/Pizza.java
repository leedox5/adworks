package com.practice;

public abstract class Pizza {
	String name;

	public void prepare() {
		System.out.println("Preparing " + name);
	}
	
	public void box() {
		System.out.println("Boxing " + name);
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
