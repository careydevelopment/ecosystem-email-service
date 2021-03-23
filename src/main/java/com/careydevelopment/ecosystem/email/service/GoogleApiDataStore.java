package com.careydevelopment.ecosystem.email.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.careydevelopment.ecosystem.email.model.GoogleApi;
import com.careydevelopment.ecosystem.email.model.User;
import com.careydevelopment.ecosystem.email.repository.UserRepository;
import com.careydevelopment.ecosystem.email.util.AuthorizationUtil;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.store.AbstractDataStore;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;

public class GoogleApiDataStore<V extends Serializable> extends AbstractDataStore<V> {

	private UserRepository userRepository;
	
	private AuthorizationUtil authorizationUtil;
	
	private final Lock lock = new ReentrantLock();

	
	protected GoogleApiDataStore(DataStoreFactory dataStoreFactory, String id, UserRepository userRepository, AuthorizationUtil authorizationUtil) {
		super(dataStoreFactory, id);
		this.userRepository = userRepository;
		this.authorizationUtil = authorizationUtil;
	}

	
	@Override
	public Set<String> keySet() throws IOException {
		return null;
	}

	
	@Override
	public Collection<V> values() throws IOException {
		return null;
	}

	
	@Override
	public V get(String key) throws IOException {
		if (key != null) {
			lock.lock();
			
			try {
				User user = authorizationUtil.getCurrentUser();
				
				if (user.getId().equals(key)) {
					GoogleApi googleApi = user.getGoogleApi();
					
					if (googleApi != null && googleApi.getStoredCredential() != null) {
						return IOUtils.deserialize(googleApi.getStoredCredential());
					}
				}			
			} finally {
				lock.unlock();
			}
		}
		
		return null;
	}

	
	@Override
	public DataStore<V> set(String key, V value) throws IOException {		
		if (key != null) {
			lock.lock();
			
			try {
				User user = authorizationUtil.getCurrentUser();
				
				if (user.getId().equals(key)) {				
					GoogleApi googleApi = user.getGoogleApi();
					if (googleApi == null ) googleApi = new GoogleApi();
					
					googleApi.setStoredCredential(IOUtils.serialize(value));
					
					user.setGoogleApi(googleApi);
					
					userRepository.save(user);
				} 		
			} finally {
				lock.unlock();
			}
		}
		
		return this;
	}

	
	@Override
	public DataStore<V> clear() throws IOException {
		return null;
	}

	
	@Override
	public DataStore<V> delete(String key) throws IOException {
		return null;
	}

}
