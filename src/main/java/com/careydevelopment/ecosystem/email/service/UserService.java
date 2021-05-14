package com.careydevelopment.ecosystem.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.careydevelopment.ecosystem.email.model.EmailIntegration;
import com.careydevelopment.ecosystem.email.model.User;

@Service
public class UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    
    //using this method to update just one field in the user object
    //without needing to worry about all the other fields in the "master" user object
    //in the ecosystem-user-service
    public void updateEmailIntegration(String id, EmailIntegration emailIntegration) {
        Query query = new Query(new Criteria("id").is(id));
        Update update = new Update().set("emailIntegration", emailIntegration);
        mongoTemplate.updateFirst(query, update, User.class);
    }
}
