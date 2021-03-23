package com.careydevelopment.ecosystem.email.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class CredentialCheck {

	private String authUrl;
	private String accessToken;

	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
}
