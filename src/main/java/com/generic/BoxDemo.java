package com.generic;

public class BoxDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Box<Integer> box = new Box<>();
		box.set(20);
		
		System.out.println(box.get());

		Pair<String, String> pair = new OrderedPair<String, String>("1", "ONE");
		System.out.println(pair.getValue());
		
		Pair<String, Box<String>> pair1 = new OrderedPair<>("1", new Box<String>());
		System.out.println(pair1.getValue());
		
		Pair<Integer, String> p1 = new OrderedPair<Integer, String>(1,"ONE");
		Pair<Integer, String> p2 = new OrderedPair<Integer, String>(2,"TWO");
		
		boolean same = BoxDemo.<Integer, String>compare(p1, p2);
		
		System.out.println(same);
	}
	
	private static <K, V> boolean compare(Pair<K,V> p1, Pair<K,V> p2) {
		
		return p1.getKey().equals(p2.getKey()) && p1.getValue().equals(p2.getValue());
	}
}
