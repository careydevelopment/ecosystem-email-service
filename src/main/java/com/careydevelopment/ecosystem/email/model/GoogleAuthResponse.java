package com.careydevelopment.ecosystem.email.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class GoogleAuthResponse {

	private String code;
	private String redirectUrl;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
