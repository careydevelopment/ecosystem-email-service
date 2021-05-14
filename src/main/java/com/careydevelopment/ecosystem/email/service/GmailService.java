package com.careydevelopment.ecosystem.email.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.email.constants.GmailConstants;
import com.careydevelopment.ecosystem.email.model.Email;
import com.careydevelopment.ecosystem.email.util.DateUtil;
import com.careydevelopment.ecosystem.email.util.EmailUtil;
import com.careydevelopment.ecosystem.email.util.StringUtil;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponseException;
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
            email.setSnippet(StringUtil.removeZeroWidthNonJoiners(snippet));
        }
        
        setValuesFromHeaders(email, message);
        
        return email;
    }

    
    private void setEmailBody(Email email, Message message) {
        email.setHtml(getBody(message, MediaType.TEXT_HTML_VALUE));
        email.setPlainText(getBody(message, MediaType.TEXT_PLAIN_VALUE));
        
        if (StringUtils.isEmpty(email.getHtml()) && StringUtils.isEmpty(email.getPlainText())) {
            email.setPlainText(getData(message));
        }
    }
    
    
    private List<Message> getMessageList(Gmail service) throws GoogleApiException {
        try {
            Messages messages = service.users().messages();
            Gmail.Users.Messages.List messageList = messages
                                                    .list(GmailConstants.CURRENT_USER)
                                                    .setQ(GmailConstants.INBOX_QUERY)
                                                    .setMaxResults(GmailConstants.INBOX_EMAIL_COUNT);
            
            ListMessagesResponse rsp = messageList.execute();
            List<Message> list = rsp.getMessages();
    
            return list;
        } catch (TokenResponseException ie) {
            LOG.error(ie.getDetails().getErrorDescription(), ie);
            throw new GoogleApiException(ie.getDetails().getError(), ie.getDetails().getErrorDescription(), ie.getStatusCode());
        } catch (IOException ie) {
            LOG.error("Problem retrieving emails!", ie);
            throw new GoogleApiException("unexpected_error", "Problem retrieving emails", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
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
    
    
    public Email sendEmail(Email email, Credential credential) throws IOException, GeneralSecurityException, MessagingException {
        Gmail service = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory(), credential)
                .setApplicationName(GmailConstants.GMAIL_APPLICATION_NAME)
                .build(); 
         
        Message message = createGmailMessageFromEmail(email);
        
        Message sentMessage = service
                                .users()
                                .messages()
                                .send(GmailConstants.CURRENT_USER, message)
                                .execute();
        
        Email sentEmail = getEmail(sentMessage, true);
        return sentEmail;
    }
    
    
    private Message createGmailMessageFromEmail(Email email) throws MessagingException, IOException {
       MimeMessage mimeMessage = EmailUtil.convertHtmlEmailToMimeMessage(email);
       
       ByteArrayOutputStream buffer = new ByteArrayOutputStream();
       mimeMessage.writeTo(buffer);
       
       byte[] bytes = buffer.toByteArray();
       String encodedEmail = Base64.getEncoder().encodeToString(bytes);
       
       Message message = new Message();
       message.setRaw(encodedEmail);
       
       return message;
    }
    
    
    public List<Email> getInbox(Credential credential) throws GoogleApiException {
        try {
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
        } catch (IOException ie) {
            LOG.error("Problem accessing Gmail!", ie);
            throw new GoogleApiException("unexpected_error", ie.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (GeneralSecurityException ge) {
            LOG.error("Security problem", ge);
            throw new GoogleApiException("security_error", ge.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
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
