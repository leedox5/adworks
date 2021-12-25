package com.practice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		
		list.add("one");
		list.add("tow");
		list.add("three");
		list.add("four");
		
		Iterator<String> it = list.iterator();
		
		while(it.hasNext()) {
			String value = (String)it.next();
			System.out.println(value);
			
		}
		
	}
}
