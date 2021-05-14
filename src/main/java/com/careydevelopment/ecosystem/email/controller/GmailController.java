package com.careydevelopment.ecosystem.email.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.careydevelopment.ecosystem.email.model.Email;
import com.careydevelopment.ecosystem.email.model.GoogleApiError;
import com.careydevelopment.ecosystem.email.model.GoogleAuthResponse;
import com.careydevelopment.ecosystem.email.model.User;
import com.careydevelopment.ecosystem.email.service.GmailService;
import com.careydevelopment.ecosystem.email.service.GoogleApiException;
import com.careydevelopment.ecosystem.email.service.GoogleOauthService;
import com.careydevelopment.ecosystem.email.util.AuthorizationUtil;
import com.google.api.client.auth.oauth2.Credential;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/email")
public class GmailController {

    private static final Logger LOG = LoggerFactory.getLogger(GmailController.class);

    @Autowired
    private GmailService gmailService;
    
    @Autowired
    private GoogleOauthService googleOauthService;
    
    @Autowired
    private AuthorizationUtil authUtil;

    
    @GetMapping("/authorizationCode")
    public ResponseEntity<?> getAuthorizationCodeUrl(@RequestParam("redirectUrl") String redirectUrl) {
        User currentUser = authUtil.getCurrentUser();
        
        String url = googleOauthService.getAuthorizationCodeUrl(currentUser.getId(), redirectUrl);            
        return ResponseEntity.ok(url);
    }
    
    
    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody GoogleAuthResponse auth) {
        LOG.debug("In createToken " + auth);

        User currentUser = authUtil.getCurrentUser();
        
        try {
            Credential credential = googleOauthService.getCredentialFromCode(auth, currentUser.getId());           
            return ResponseEntity.ok(credential.getAccessToken());
        } catch (Exception e) {
            LOG.error("Problem retrieving credential!", e);
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem creating token!");
    }   
    
    
    @GetMapping("/inbox")
    public ResponseEntity<?> inbox() {
        LOG.debug("In inbox");

        try {
            List<Email> inbox = new ArrayList<>();
            User currentUser = authUtil.getCurrentUser();
            
            Credential credential = googleOauthService.getCredential(currentUser.getId());
            System.err.println("Current user is " + currentUser);
            System.err.println("credential is " + credential);
            
            inbox = gmailService.getInbox(credential);
            
            return ResponseEntity.ok(inbox);
        } catch (GoogleApiException ge) {
            System.err.println("caught " + ge.getError());
            return ResponseEntity.status(ge.getStatusCode()).body(new GoogleApiError(ge.getError(), ge.getMessage()));
        } 
    }
    
    
    @GetMapping("/messages/{id}")
    public ResponseEntity<?> fetchEmailMessage(@PathVariable String id) {
        LOG.debug("Fetch email message " + id);

        try {
            User currentUser = authUtil.getCurrentUser();            
            Credential credential = googleOauthService.getCredential(currentUser.getId());
            
            Email email = gmailService.getSingleEmailMessageById(id, credential);
            
            return ResponseEntity.ok(email);
        } catch (IOException ie) {
            LOG.error("Problem retrieving email!", ie);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ie.getMessage());
        } catch (GeneralSecurityException ge) {
            LOG.error("Security issue!", ge);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ge.getMessage());
        }
    }
    
    
    @PostMapping("/messages")
    public ResponseEntity<?> sendEmailMessage(@RequestBody Email email) {
        LOG.debug("Sending message " + email);

        try {
            User currentUser = authUtil.getCurrentUser();            
            Credential credential = googleOauthService.getCredential(currentUser.getId());
            
            Email sentEmail = gmailService.sendEmail(email, credential);
            
            return ResponseEntity.ok(sentEmail);
        } catch (IOException ie) {
            LOG.error("Problem retrieving email!", ie);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ie.getMessage());
        } catch (GeneralSecurityException ge) {
            LOG.error("Security issue!", ge);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ge.getMessage());
        } catch (MessagingException me) {
            LOG.error("Messaging issue!", me);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(me.getMessage());            
        }
    }
}
