package com.calc;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {

	private Calculator calc;

	@Before
	public void before()
	{
		calc = new CalculatorImpl();
	}
	
	@Test
	public void testAdd() {
		assertNotNull("calc is not null", calc);
		assertEquals(101, calc.add(23,78));
		
		try
		{
			calc.add(Integer.MAX_VALUE, 100);
			fail(OverflowException.class.getName() + " is expected");
		}
		catch(OverflowException e)
		{
		}

		try
		{
			calc.add(Integer.MIN_VALUE, -1);
			fail(UnderflowException.class.getName() + " is expected");
		}
		catch(UnderflowException e)
		{
			
		}
		assertEquals(55, calc.add("1-10"));
		assertEquals(66, calc.add("1-11"));
	}
	
	@Test
	public void CommaDelimeter()
	{
		assertEquals(calc.add("1,2,3"), 6);
	}
	
	@Test
	public void testMul() {
		assertEquals(calc.mul(3,4), 12);
	}
	
	@Test
	public void testSub()
	{
		assertEquals(100, calc.sub(300,200));
		assertTrue(calc.sub(10, 10) == 0);
		try {
			calc.sub(Integer.MIN_VALUE, 1);
			fail(UnderflowException.class.getName() + " is exptected");
		} catch(UnderflowException e) {
			
		}
	}
}
