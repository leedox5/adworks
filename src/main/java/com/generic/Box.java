package com.generic;

public class Box<T> {
	private T t;

	public T get() {
		return t;
	}

	public void set(T t) {
		this.t = t;
	}
	
	public <U extends Number> void inspect(U u) {
		System.out.println("T:" + t.getClass().getName());
		
	}
	
	public static void main(String[] args) {
		Box<Integer> ibox = new Box<Integer>();
		ibox.set(new Integer(10));
		ibox.inspect(20);
	}
}
