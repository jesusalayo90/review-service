package com.mservices.review.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private final static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    public final static String DATE_TIME_ZONE = "yyyy-MM-dd HH:mm:ss.SSSZ";

    public static String fromDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date parseDate(String dateString, String format) {
        try {
            return new SimpleDateFormat(format).parse(dateString);
        } catch (ParseException e) {
            logger.error(String.format("Could not parse '%s' to date format: %s", dateString, format));
        }
        return null;
    }

}
