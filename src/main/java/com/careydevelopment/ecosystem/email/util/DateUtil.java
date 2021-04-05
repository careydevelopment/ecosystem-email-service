package com.careydevelopment.ecosystem.email.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {

	private static final Logger LOG = LoggerFactory.getLogger(GoogleOauthUtil.class);
	
	//Tue, 28 Jul 2020 16:27:19 +0000
	private static DateFormat GMAIL_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ");
	
	public static Long getLongValueFromGmailDateFormat(String dateString) {
		Long dateVal = 0l;
		
		try {
			Date date = GMAIL_DATE_FORMAT.parse(dateString);
			dateVal = date.getTime();
		} catch (ParseException pe) {
			LOG.error("Problem parsing date: " + dateString, pe);
		}
		
		return dateVal;
	}
}
