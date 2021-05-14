package com.careydevelopment.ecosystem.email.util;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.http.MediaType;

import com.careydevelopment.ecosystem.email.model.Email;

public class EmailUtil {

    public static MimeMessage convertEmailToMimeMessage(Email email) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage mimeMessage = new MimeMessage(session);

        setMetadata(mimeMessage, email);
        
        mimeMessage.setText(email.getPlainText());

        return mimeMessage;
    }
    
    
    public static MimeMessage convertHtmlEmailToMimeMessage(Email email) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage mimeMessage = new MimeMessage(session);

        setMetadata(mimeMessage, email);
        
        MimeBodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(email.getHtml(), MediaType.TEXT_HTML_VALUE);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(htmlBodyPart);
        
        mimeMessage.setContent(multipart);

        return mimeMessage;
    }
    
    
    private static void setMetadata(MimeMessage mimeMessage, Email email) throws MessagingException {
        mimeMessage.setFrom(new InternetAddress(email.getFrom()));
        mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(email.getTo()));
        mimeMessage.setSubject(email.getSubject());
    }
}
