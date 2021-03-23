package com.careydevelopment.ecosystem.email.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.careydevelopment.ecosystem.email.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	public User findByUsername(String username);
	
	public User findByEmail(String email);
	
}
