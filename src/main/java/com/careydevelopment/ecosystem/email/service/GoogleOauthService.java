package com.careydevelopment.ecosystem.email.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.email.constants.GmailConstants;
import com.careydevelopment.ecosystem.email.model.GoogleAuthResponse;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.services.gmail.GmailScopes;

@Component
public class GoogleOauthService {
	
	private static final Logger LOG = LoggerFactory.getLogger(GoogleOauthService.class);

	@Value("${google.api.oauth2.token.url}") String tokenUrl;
	@Value("${google.api.oauth2.auth.url}") String authUrl;
	@Value("${google.api.oauth2.client.id}") String clientId;
	@Value("${google.api.oauth2.client.secret}") String clientSecret;
	
	private DataStore<StoredCredential> dataStore;
	
	@Autowired
	public GoogleOauthService(GoogleApiDataStoreFactory dataStoreFactory) {
		setupDataStore(dataStoreFactory);	
	}

	
	private void setupDataStore(DataStoreFactory dataStoreFactory) {
		try {
			dataStore = dataStoreFactory.getDataStore(GmailConstants.CREDENTIAL_STORE_ID);
		} catch (Exception e) {
			LOG.error("Problem creating data store for credentials!", e);
		}
	}
	

	private List<CredentialRefreshListener> getListeners(String userId) {
		DataStoreCredentialRefreshListener listener = new DataStoreCredentialRefreshListener(userId, dataStore);
		List<CredentialRefreshListener> listeners = new ArrayList<CredentialRefreshListener>();
		listeners.add(listener);
		
		return listeners;
	}
	
	
	private GoogleAuthorizationCodeFlow getAuthorizationCodeFlow(String userId) throws IOException, GeneralSecurityException {
		List<CredentialRefreshListener> listeners = getListeners(userId);
		
		GoogleAuthorizationCodeFlow authorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), 
				new GsonFactory(), 
				clientId, 
				clientSecret, 
				Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM))
				.setCredentialDataStore(dataStore)
				.setAccessType("offline")
				.setRefreshListeners(listeners)
				.build();		
		
		return authorizationCodeFlow;
	}
	
	
	public Credential getCredential(String id) {
		Credential credential = null;
		
		try {
			GoogleAuthorizationCodeFlow acf = getAuthorizationCodeFlow(id);
			credential = acf.loadCredential(id);
		} catch (Exception e) {
			LOG.error("Problem retrieving credential!", e);
		}
		
		return credential;
	}
	
	
	public String getAuthorizationCodeUrl(String id, String redirectUrl) {		
	    String url = null;
	    
		try {
			AuthorizationCodeRequestUrl acru = getAuthorizationCodeFlow(id).newAuthorizationUrl();
			acru.setRedirectUri(redirectUrl).build();
			acru.setState("state");
			
			LOG.debug(acru.toString());
			
			url = acru.build();
		} catch (Exception e) {
			LOG.error("Problem getting authorization code!", e);
		}

		return url;
	}

	
	public Credential getAccessToken(String id) {
		LOG.debug("The user id is " + id);
		Credential credential = null;
		
		try {
			credential = getAuthorizationCodeFlow(id).loadCredential(id);
			LOG.debug("Credential is " + credential);
			
			if (credential == null) {
				LOG.error("Expected credential but received none!");
			} 
		} catch (Exception e) {
			LOG.error("Problem retrieving token!", e);
		}
		
		return credential;
	}

	
	public Credential getCredentialFromCode(GoogleAuthResponse auth, String id) {		
        Credential credential = null;

	    try {
		    GoogleAuthorizationCodeFlow acf = getAuthorizationCodeFlow(id);
		        
	        AuthorizationCodeTokenRequest req = acf.newTokenRequest(auth.getCode());
	        req.setRedirectUri(auth.getRedirectUrl());

			TokenResponse response = req.execute();
			LOG.debug(response.toPrettyString());
			
			credential = acf.createAndStoreCredential(response, id);
		} catch (Exception e ) {
			LOG.error("Problem creating credential!", e);
		}
		
		return credential;
	}
}
