package com.careydevelopment.ecosystem.email.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {

	private static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);
	
	//Tue, 28 Jul 2020 16:27:19 +0000
	private static DateFormat GMAIL_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ");

	//10 May 2021 13:48:17 -0400
	private static DateFormat ALT_GMAIL_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss ZZZ");

	   
	public static Long getLongValueFromGmailDateFormat(String dateString) {
		Long dateVal = 0l;
		
		try {
			Date date = GMAIL_DATE_FORMAT.parse(dateString);
			dateVal = date.getTime();
		} catch (ParseException pe) {
			LOG.error("Problem parsing date: " + dateString);
			return parseAlternativeFormat(dateString);
		}
		
		return dateVal;
	}
	
	
	private static Long parseAlternativeFormat(String dateString) {
	    Long dateVal = 0l;
	    
	    try {
            Date date = ALT_GMAIL_DATE_FORMAT.parse(dateString);
            dateVal = date.getTime();
	    } catch (ParseException pe) {
	        LOG.error("Unable to parse alt date: " + dateString, pe);
	    }
	    
	    return dateVal;
	}
}
