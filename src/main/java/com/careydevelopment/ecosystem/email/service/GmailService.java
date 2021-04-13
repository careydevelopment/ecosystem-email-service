package com.careydevelopment.ecosystem.email.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.email.constants.GmailConstants;
import com.careydevelopment.ecosystem.email.model.Email;
import com.careydevelopment.ecosystem.email.util.DateUtil;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Messages;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;

@Component
public class GmailService {

	private static final Logger LOG = LoggerFactory.getLogger(GmailService.class);
	    
    
    private Email getEmail(Message message, boolean lightweight) {
        Email email = new Email();
 
        if (!lightweight) {
            setEmailBody(email, message);
        }
        
        email.setId(message.getId());
        if (message.getSnippet() != null) {
            String snippet = message.getSnippet();
            email.setSnippet(snippet.replaceAll("[\\p{Cf}]", "").trim());
        }
        
        setValuesFromHeaders(email, message);
        
        return email;
    }

    
    private void setEmailBody(Email email, Message message) {
        email.setHtml(getBody(message, "text/html"));
        email.setPlainText(getBody(message, "text/body"));
        
        if (StringUtils.isEmpty(email.getHtml()) && StringUtils.isEmpty(email.getPlainText())) {
            email.setPlainText(getData(message));
        }
    }
    
    
    private List<Message> getMessageList(Gmail service) {
        try {
            Messages messages = service.users().messages();
            Gmail.Users.Messages.List messageList = messages
                                                    .list(GmailConstants.CURRENT_USER)
                                                    .setQ(GmailConstants.INBOX_QUERY)
                                                    .setMaxResults(GmailConstants.INBOX_EMAIL_COUNT);
            
            ListMessagesResponse rsp = messageList.execute();
            List<Message> list = rsp.getMessages();
    
            return list;
        } catch (IOException ie) {
            LOG.error("Problem retrieving Gmail messages!", ie);
        }
        
        return null;
    }
    

    private Email getSingleEmailMessageById(String id, Gmail service, boolean lightweight) {
        try {
            Message retrievedMessage = service
                                        .users()
                                        .messages()
                                        .get(GmailConstants.CURRENT_USER, id)
                                        .setFormat(GmailConstants.FULL_FORMAT)
                                        .execute();
            
            Email email = getEmail(retrievedMessage, lightweight);
            return email;
        } catch (IOException ie) {
            LOG.error("Problem retrieving individual message!");
        }   
        
        return null;
    }
    
    
    public Email getSingleEmailMessageById(String id, Credential credential) throws IOException, GeneralSecurityException {
        Gmail service = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory(), credential)
                .setApplicationName(GmailConstants.GMAIL_APPLICATION_NAME)
                .build(); 
        
        return this.getSingleEmailMessageById(id, service, false);
    }
    
    
    public List<Email> getInbox(Credential credential) throws IOException, GeneralSecurityException {
        Gmail service = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory(), credential)
                .setApplicationName(GmailConstants.GMAIL_APPLICATION_NAME)
                .build();           
        
        List<Message> list = getMessageList(service);
        List<Email> emails = new ArrayList<Email>();
        
        if (list != null) {
            list.forEach(message -> {
                Email email = getSingleEmailMessageById(message.getId(), service, true);
                if (email != null) emails.add(email);
            });
        }
        
        return emails;
    }
    
    
    private void setValuesFromHeaders(Email email, Message message) {
        if (message.getPayload() != null) {
            List<MessagePartHeader> headers = message.getPayload().getHeaders();
            
            headers.forEach(header -> {
                setValueFromHeader(header, email);
            });            
        }
    }
    
    
    private void setValueFromHeader(MessagePartHeader header, Email email) {
        if (header.getName() != null) {
            switch (header.getName()) {
                case  GmailConstants.SUBJECT_HEADER:
                    email.setSubject(header.getValue());
                    break;
                case  GmailConstants.FROM_HEADER:
                    email.setFrom(header.getValue());
                    break;
                case  GmailConstants.TO_HEADER:
                    email.setTo(header.getValue());
                    break;
                case  GmailConstants.DATE_HEADER:
                    email.setDate(DateUtil.getLongValueFromGmailDateFormat(header.getValue()));
                    break;
            }
        }
    }
    
    
    private String getBody(Message message, String type) {
        StringBuilder sb = new StringBuilder();
        
        if (message.getPayload() != null && message.getPayload().getParts() != null) {
            for (MessagePart msgPart : message.getPayload().getParts()) {
                if (msgPart.getMimeType().contains(type))
                    sb.append((new String(Base64.getUrlDecoder().decode(msgPart.getBody().getData()))));
            }           
        }

        String body = sb.toString();
        
        return body;
    }
    
    
    private String getData(Message message) {
        StringBuilder sb = new StringBuilder();
        
        if (message.getPayload() != null && message.getPayload().getBody() != null && message.getPayload().getBody().getData() != null) {
            sb.append((new String(Base64.getUrlDecoder().decode(message.getPayload().getBody().getData()))));
        }

        String body = sb.toString();
        
        return body;
    }
}
