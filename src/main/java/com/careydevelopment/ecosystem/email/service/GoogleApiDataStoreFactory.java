package com.careydevelopment.ecosystem.email.service;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.email.repository.UserRepository;
import com.careydevelopment.ecosystem.email.util.AuthorizationUtil;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.DataStore;

@Component
public class GoogleApiDataStoreFactory extends AbstractDataStoreFactory {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthorizationUtil authorizationUtil;
	

	@Override
	protected <V extends Serializable> DataStore<V> createDataStore(String id) throws IOException {
		return new GoogleApiDataStore<V>(this, id, userRepository, authorizationUtil);
	}


}
