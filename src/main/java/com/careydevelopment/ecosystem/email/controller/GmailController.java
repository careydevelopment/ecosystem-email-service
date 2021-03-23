package com.careydevelopment.ecosystem.email.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.careydevelopment.ecosystem.email.model.User;
import com.careydevelopment.ecosystem.email.util.AuthorizationUtil;
import com.careydevelopment.ecosystem.email.util.GmailUtil;
import com.careydevelopment.ecosystem.email.util.GoogleOauthUtil;

@Controller
public class GmailController {

    private static final Logger LOG = LoggerFactory.getLogger(GmailController.class);

    @Autowired
    private GmailUtil gmailUtil;
    
    @Autowired
    private GoogleOauthUtil googleOauthUtil;
    
    @Autowired
    private AuthorizationUtil authUtil;

    
    
    @GetMapping("/email/authorizationCode")
    public ResponseEntity<?> getAuthorizationCode(@RequestParam("baseRedirectUrl") String baseRedirectUrl) {
        LOG.debug("Got in here " + baseRedirectUrl);    
        
        User currentUser = authUtil.getCurrentUser();
        
        String url = googleOauthUtil.getAuthorizationCode(currentUser.getId(), baseRedirectUrl);            
        return ResponseEntity.ok(url);
    }
}
