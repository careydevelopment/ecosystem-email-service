package com.careydevelopment.ecosystem.email.util;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.careydevelopment.ecosystem.email.model.Email;

public class EmailUtil {

    public static MimeMessage convertEmailToMimeMessage(Email email) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage mimeMessage = new MimeMessage(session);

        mimeMessage.setFrom(new InternetAddress(email.getFrom()));
        mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(email.getTo()));
        mimeMessage.setSubject(email.getSubject());
        mimeMessage.setText(email.getPlainText());

        return mimeMessage;
    }
}
