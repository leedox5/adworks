package com.itnova.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

	public static String getPreviousDate(String format, int i) {
		return format(moveFromNow(0, 0, i), format);
	}

	private static String format(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
		return formatter.format(date);
	}

	private static Date moveFromNow(int y, int m, int d) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, d);
		return c.getTime();
	}

}
