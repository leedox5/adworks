package com.practice;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class HelloWorldTest {
	private HelloWorld hello;

	@Before
	public void before()
	{
		hello = new HelloWorldImpl();
	}
	@Test
	public void test() {
		assertEquals("Hello", hello.Say());
	}
}
