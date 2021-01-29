package com.careydevelopment.contact.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.careydevelopment.contact.model.Contact;
import com.careydevelopment.contact.repository.AccountRepository;
import com.careydevelopment.contact.repository.ContactRepository;
import com.careydevelopment.contact.service.ContactService;
import com.careydevelopment.contact.service.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@Component
public class ApplicationListenerInitialize implements ApplicationListener<ApplicationReadyEvent>  {
    
    @Autowired
    private ContactService contactService;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private ContactRepository contactRepository;
    
    private String data = "";
    
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            List<Contact> list = (List<Contact>)objectMapper.readValue(data, typeFactory.constructCollectionType(List.class, Contact.class));
            //System.err.println(list);
            for (Contact contact : list) {
                this.contactRepository.save(contact);
            }
        } catch (ServiceException e) {
            System.err.println(e.getMessage() + " " + e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}