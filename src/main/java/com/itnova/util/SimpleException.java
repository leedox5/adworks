package com.itnova.util;

public class SimpleException extends Exception {
	/**
	 *  한글
	 */
	private static final long serialVersionUID = -932522379533378489L;
	private Throwable nestedException;
	
	public SimpleException(String s) {
		super(s);
	}
	
	public SimpleException(String s, Exception nestedException) {
		super(s);
		this.nestedException = nestedException;
	}
	
	public Throwable getNestedException() {
		return this.nestedException;
	}
}
