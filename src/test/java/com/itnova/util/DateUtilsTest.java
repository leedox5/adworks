package com.itnova.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.itnova.util.DateUtils;

public class DateUtilsTest {

	@Test
	public void getPreviousDateTest() {
		//fail("Not yet implemented");
		assertEquals("20101028", DateUtils.getPreviousDate("yyyyMMdd", -2));
	}

}
