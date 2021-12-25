package com.practice;

import org.junit.*;
import static org.junit.Assert.*;

public class SubscriptionTest
{
	@BeforeClass
	public static void oneTimeSetUp()
	{
	}

	@Test
	public void test_returnEuro()
	{
		Subscription s = new Subscription(200,2);
		assertTrue(s.pricePerMonth() == 1.0);
	}
}