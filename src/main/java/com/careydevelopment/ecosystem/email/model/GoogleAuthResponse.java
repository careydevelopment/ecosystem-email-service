package com.careydevelopment.ecosystem.email.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class GoogleAuthResponse {

	private String code;
	private String baseRedirectUrl;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getBaseRedirectUrl() {
		return baseRedirectUrl;
	}

	public void setBaseRedirectUrl(String baseRedirectUrl) {
		this.baseRedirectUrl = baseRedirectUrl;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
