package com.pattern;

public abstract class Pizza {

	protected String name;

	void prepare() {
		System.out.println("Prepareing " + name);
	}

	public void box() {
		
	}

	public String getName() {
		return name;
	}
	
}
