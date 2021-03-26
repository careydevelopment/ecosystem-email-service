package com.careydevelopment.ecosystem.email.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.careydevelopment.ecosystem.email.model.Email;
import com.careydevelopment.ecosystem.email.model.GoogleAuthResponse;
import com.careydevelopment.ecosystem.email.model.User;
import com.careydevelopment.ecosystem.email.util.AuthorizationUtil;
import com.careydevelopment.ecosystem.email.util.GmailUtil;
import com.careydevelopment.ecosystem.email.util.GoogleOauthUtil;
import com.google.api.client.auth.oauth2.Credential;

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
    public ResponseEntity<?> getAuthorizationCodeUrl(@RequestParam("redirectUrl") String redirectUrl) {
        User currentUser = authUtil.getCurrentUser();
        
        String url = googleOauthUtil.getAuthorizationCodeUrl(currentUser.getId(), redirectUrl);            
        return ResponseEntity.ok(url);
    }
    
    
    @PostMapping("/email/token")
    public ResponseEntity<?> createToken(@RequestBody GoogleAuthResponse auth) {
        LOG.debug("In createToken " + auth);

        User currentUser = authUtil.getCurrentUser();
        
        try {
            Credential credential = googleOauthUtil.getCredentialFromCode(auth, currentUser.getId());           
            return ResponseEntity.ok(credential.getAccessToken());
        } catch (Exception e) {
            LOG.error("Problem retrieving credential!", e);
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem creating token!");
    }   
    
    
    @GetMapping("/email/inbox")
    public ResponseEntity<?> inbox() {
        LOG.debug("In inbox");

        List<Email> inbox = new ArrayList<Email>();
        User currentUser = authUtil.getCurrentUser();
        
        Credential credential = googleOauthUtil.getCredential(currentUser.getId());
        inbox = gmailUtil.getInbox(credential);    
        
        return ResponseEntity.ok(inbox);
    }
}
