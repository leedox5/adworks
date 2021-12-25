package com.practice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HashMapDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", new String("123"));
		map.put("b", new String("456"));
		
		Set<Entry<String, Object>> s = map.entrySet();
		Iterator<Entry<String, Object>> it = s.iterator();
		
		while(it.hasNext()) {
			Map.Entry<String, Object> m = (Map.Entry<String, Object>)it.next();
		
			String key=(String)m.getKey();
			
			String value = (String)m.getValue();
			System.out.println("Key : " + key);
			System.out.println("Val : " + value);
		}
	}
}
