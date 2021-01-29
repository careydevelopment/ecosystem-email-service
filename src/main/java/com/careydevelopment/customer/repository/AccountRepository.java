package com.careydevelopment.customer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.careydevelopment.customer.model.Account;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

}
