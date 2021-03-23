package com.careydevelopment.ecosystem.email.util;

public class RedirectUtil {

	private static final String EMAIL_REDIRECT_SUFFIX = "/email/email-redirect";
	
	public static String getEmailRedirect(String baseRedirectUrl) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseRedirectUrl);
		sb.append(EMAIL_REDIRECT_SUFFIX);
		
		return sb.toString();
	}

}
