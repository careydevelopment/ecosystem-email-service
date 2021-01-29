package com.careydevelopment.ecosystem.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.careydevelopment.ecosystem.product.model.LineOfBusiness;


@Repository
public interface LineOfBusinessRepository extends MongoRepository<LineOfBusiness, String> {

}
