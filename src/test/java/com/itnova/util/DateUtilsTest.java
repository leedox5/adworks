package com.itnova.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateUtilsTest {

	@Test
	public void getPreviousDateTest() {
		assertEquals("20231129", DateUtils.getPreviousDate("yyyyMMdd", -2));
	}

}
