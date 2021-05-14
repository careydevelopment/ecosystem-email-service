package com.careydevelopment.ecosystem.email.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.careydevelopment.ecosystem.email.model.EmailIntegration;
import com.careydevelopment.ecosystem.email.model.User;
import com.careydevelopment.ecosystem.email.service.UserService;
import com.careydevelopment.ecosystem.email.util.AuthorizationUtil;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    
    @Autowired
    private AuthorizationUtil authUtil;
    
    @Autowired
    private UserService userService;

    
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody EmailIntegration emailIntegration) {
        User currentUser = authUtil.getCurrentUser();

        if (currentUser != null && currentUser.getId().equals(id)) {
            userService.updateEmailIntegration(id, emailIntegration);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return ResponseEntity.ok().build();
    }
    
}
