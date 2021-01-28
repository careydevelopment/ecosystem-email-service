package com.careydevelopment.contact.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.careydevelopment.contact.model.Account;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

}
