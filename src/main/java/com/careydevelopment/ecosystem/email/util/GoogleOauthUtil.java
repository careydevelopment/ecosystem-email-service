package com.careydevelopment.ecosystem.email.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.email.model.CredentialCheck;
import com.careydevelopment.ecosystem.email.model.GoogleAuthResponse;
import com.careydevelopment.ecosystem.email.service.GoogleApiDataStoreFactory;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Messages;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;

@Component
public class GoogleOauthUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(GoogleOauthUtil.class);

	@Value("${google.api.oauth2.token.url}") String tokenUrl;
	@Value("${google.api.oauth2.auth.url}") String authUrl;
	@Value("${google.api.oauth2.client.id}") String clientId;
	@Value("${google.api.oauth2.client.secret}") String clientSecret;
	
	private DataStore<StoredCredential> dataStore;
	
	@Autowired
	public GoogleOauthUtil(GoogleApiDataStoreFactory dataStoreFactory) {
		setupDataStore(dataStoreFactory);	
	}

	
	private void setupDataStore(DataStoreFactory dataStoreFactory) {
		try {
			dataStore = dataStoreFactory.getDataStore(Constants.CREDENTIAL_STORE_ID);
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
	
	
	private GoogleAuthorizationCodeFlow getAuthorizationCodeFlow(String userId) {
		List<CredentialRefreshListener> listeners = getListeners(userId);
		
		GoogleAuthorizationCodeFlow authorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(
				new NetHttpTransport(), 
				new JacksonFactory(), 
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
	
	
	public CredentialCheck getCredentialCheck(String id, String baseRedirectUrl) {		
		CredentialCheck check = new CredentialCheck();
		
		LOG.debug("The user id is " + id);
		
		try {
			GoogleAuthorizationCodeFlow acf = getAuthorizationCodeFlow(id);
			Credential credential = acf.loadCredential(id);
			LOG.debug("Credential is " + credential);
			
			if (credential == null) {
				String redirectUrl = RedirectUtil.getEmailRedirect(baseRedirectUrl);
				
				AuthorizationCodeRequestUrl acru = getAuthorizationCodeFlow(id).newAuthorizationUrl();
				acru.setRedirectUri(redirectUrl).build();
				acru.setState("state");
				
				check.setAuthUrl(acru.build());
			} else {
				//if we get here, we know we have an access token, but it might be invalid
				//we'll do a quick peek at the inbox to make sure it's still valid
				if (validCredential(credential, id)) {
					System.err.println("Setting access token");
					check.setAccessToken(credential.getAccessToken());	
				} else {
					LOG.debug("Token looks invalid");
				}
			}
		} catch (Exception e) {
			LOG.error("Problem with credential check!", e);
		}

		return check;
	}

	
	private boolean validCredential(Credential credential, String userId) {
		boolean valid = false;
		
		try {
	        Gmail service = new Gmail.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
	                .setApplicationName(Constants.GMAIL_APPLICATION_NAME)
	                .build();	        
	        
	        Messages messages = service.users().messages();
	        Gmail.Users.Messages.List messageList = messages.list("me");
	        ListMessagesResponse rsp = messageList.execute();
	        
	        //if we get here without an exception, then the token is valid
	        valid = true;
		} catch (Exception e) {
			//if we get here, the token is invalid
			LOG.info("Looks like an invalid credential");
		}

		return valid;
	}
	

	public String getAuthorizationCode(String id, String baseRedirectUrl) {		
		try {
			String redirectUrl = RedirectUtil.getEmailRedirect(baseRedirectUrl);
			
			AuthorizationCodeRequestUrl acru = getAuthorizationCodeFlow(id).newAuthorizationUrl();
			acru.setRedirectUri(redirectUrl).build();
			acru.setState("state");
			
			LOG.debug(acru.toString());
			
			String url = acru.build();
			
			return url;
		} catch (Exception e) {
			LOG.error("Problem getting authorization code!", e);
		}

		return null;
	}

	
	public Credential getAccessToken(String id) {
		LOG.debug("The user id is " + id);
		
		try {
			Credential credential = getAuthorizationCodeFlow(id).loadCredential(id);
			LOG.debug("Credential is " + credential);
			
			if (credential == null) {
				LOG.error("Expected credential but received none!");
				return null;
			} else {
				return credential;
			}
		} catch (Exception e) {
			LOG.error("Problem retrieving token!", e);
			return null;
		}
	}

	
	public Credential getCredentialFromCode(GoogleAuthResponse auth, String id) {
		GoogleAuthorizationCodeFlow acf = getAuthorizationCodeFlow(id);
		
		AuthorizationCodeTokenRequest req = acf.newTokenRequest(auth.getCode());
		req.setRedirectUri(RedirectUtil.getEmailRedirect(auth.getBaseRedirectUrl()));

        Credential credential = null;
		
		try {
			TokenResponse response = req.execute();
			LOG.debug(response.toPrettyString());
			
			credential = acf.createAndStoreCredential(response, id);
		} catch (Exception e ) {
			LOG.error("Problem creating credential!", e);
		}
		
		return credential;
	}
}
