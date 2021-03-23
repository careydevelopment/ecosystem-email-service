package com.careydevelopment.ecosystem.email.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GmailController {

    private static final Logger LOG = LoggerFactory.getLogger(GmailController.class);

    
    @GetMapping("/{id}/email/getAuthorizationCode")
    public ResponseEntity<?> getAuthorizationCode(@PathVariable("id") String id, @RequestParam("baseRedirectUrl") String baseRedirectUrl) {
        LOG.debug("Got in here " + baseRedirectUrl);    
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();
        
        String url = googleOauthUtil.getAuthorizationCode(currentUser.getId(), baseRedirectUrl);            
        return ResponseEntityUtil.createResponseEntity(url , HttpStatus.OK);
    }
}
