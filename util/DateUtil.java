package com.larcloud.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static Date stringToDate(String dateStr) throws ParseException{
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;

			date = formatter.parse(dateStr);

		return date;
	}

	public static String dateToString(Date date) {
        if (date==null){
            return null;
        }
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

    public static String dateToStringShort(Date date) {
        if (date==null){
            return null;
        }
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}
