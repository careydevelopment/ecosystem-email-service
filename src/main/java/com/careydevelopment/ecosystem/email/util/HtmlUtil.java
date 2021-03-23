package com.careydevelopment.ecosystem.email.util;

public class HtmlUtil {

	public static String stripTitle(String html) {
		HtmlReader reader = new HtmlReader(html);
		reader.stripTag("title");
		
		//System.err.println("After title tag strip, html is " + reader.getContents());
		
		return reader.getContents();
	}

}
