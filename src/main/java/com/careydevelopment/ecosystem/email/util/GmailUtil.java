package com.careydevelopment.ecosystem.email.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.email.model.Email;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Messages;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;

@Component
public class GmailUtil {

	private static final Logger LOG = LoggerFactory.getLogger(GmailUtil.class);
	
    private static final String SUBJECT_HEADER = "Subject";
    private static final String FROM_HEADER = "From";
    private static final String TO_HEADER = "To";
    private static final String DATE_HEADER = "Date";

    
    public Email getEmail(Message message) {
        Email email = new Email();
        
        email.setHtml(getHtmlBody(message));
        email.setPlainText(getPlainText(message));
        
        if (StringUtils.isEmpty(email.getHtml()) && StringUtils.isEmpty(email.getPlainText())) {
            email.setPlainText(getData(message));
        }
        
        email.setId(message.getId());
        
        setValuesFromHeaders(email, message);
        
        return email;
    }
    
    
    private List<Message> getMessageList(Gmail service) {
        try {
            Messages messages = service.users().messages();
            Gmail.Users.Messages.List messageList = messages.list("me").setQ("in:inbox -category:{social promotions forums}").setMaxResults(20l);
            ListMessagesResponse rsp = messageList.execute();
            List<Message> list = rsp.getMessages();
    
            return list;
        } catch (IOException ie) {
            LOG.error("Problem retrieving Gmail messages!", ie);
        }
        
        return null;
    }
    

    private Email getSingleEmailMessageById(String id, Gmail service) {
        try {
            Message retrievedMessage = service.users().messages().get("me", id).setFormat("full").execute();
            //System.err.println(message);
            Email email = getEmail(retrievedMessage);
            System.err.println("Email is " + email.getSubject() + " " + email.getId());
            return email;
        } catch (IOException ie) {
            LOG.error("Problem retrieving individual message!");
        }   
        
        return null;
    }
    
    
    public List<Email> getInbox(Credential credential) {
        Gmail service = new Gmail.Builder(new NetHttpTransport(), new GsonFactory(), credential)
                .setApplicationName(Constants.GMAIL_APPLICATION_NAME)
                .build();           
        
        List<Message> list = getMessageList(service);
        List<Email> emails = new ArrayList<Email>();
        
        if (list != null) {
            list.forEach(message -> {
                Email email = getSingleEmailMessageById(message.getId(), service);
                if (email != null) emails.add(email);
            });
        }
        
        return emails;
    }
    
    
    private void setValuesFromHeaders(Email email, Message message) {
        List<MessagePartHeader> headers = message.getPayload().getHeaders();
        
        headers.forEach(header -> {
            setValueFromHeader(header, email);
        });
    }
    
    
    private void setValueFromHeader(MessagePartHeader header, Email email) {
        if (header.getName() != null) {
            switch (header.getName()) {
                case  SUBJECT_HEADER:
                    email.setSubject(header.getValue());
                    break;
                case  FROM_HEADER:
                    email.setFrom(header.getValue());
                    break;
                case  TO_HEADER:
                    email.setTo(header.getValue());
                    break;
                case  DATE_HEADER:
                    email.setDate(DateUtil.getLongValueFromGmailDateFormat(header.getValue()));
                    break;
            }
        }
    }
    
    
    private String getHtmlBody(Message message) {
        StringBuilder sb = new StringBuilder();
        
        if (message.getPayload() != null && message.getPayload().getParts() != null) {
            for (MessagePart msgPart : message.getPayload().getParts()) {
                if (msgPart.getMimeType().contains("text/html"))
                    sb.append((new String(Base64.getUrlDecoder().decode(msgPart.getBody().getData()))));
            }           
        }

        String body = sb.toString();
        
        return body;
    }


    private String getPlainText(Message message) {
        StringBuilder sb = new StringBuilder();
        
        if (message.getPayload() != null && message.getPayload().getParts() != null) {
            for (MessagePart msgPart : message.getPayload().getParts()) {
                if (msgPart.getMimeType().contains("text/plain"))
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
