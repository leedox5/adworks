package com.practice;

import java.util.Vector;

public class VectorDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Vector<User> vector = new Vector<User>();
		
		User user1 = new User();
		user1.setUserid("user1");
		user1.setUsername("hongkildong");
		
		vector.add(user1);
		
		User user2 = (User)vector.elementAt(0);
		
		System.out.println(user2.getUserid());
	}
}
