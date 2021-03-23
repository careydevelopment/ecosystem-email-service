package com.careydevelopment.ecosystem.email.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.email.model.User;

@Component
public class AuthorizationUtil {

	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User)authentication.getPrincipal();
		
		return currentUser;
	}

	
	public boolean authorized(String expectedId) {
		boolean auth = false;
		
		User currentUser = getCurrentUser();
		
		if (currentUser.getId().equals(expectedId)) {
			auth = true;
		}
		
		return auth;
	}
}
