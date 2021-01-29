package com.careydevelopment.customer.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.careydevelopment.customer.model.Account;
import com.careydevelopment.customer.model.AccountLightweight;
import com.careydevelopment.customer.model.Contact;
import com.careydevelopment.customer.repository.AccountRepository;
import com.careydevelopment.customer.repository.ContactRepository;
import com.careydevelopment.customer.service.ContactService;
import com.careydevelopment.customer.service.ServiceException;
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
    
    private String data = "[ {\r\n"
            + "  \"firstName\" : \"Jorge\",\r\n"
            + "  \"lastName\" : \"Smitty\",\r\n"
            + "  \"email\" : \"smitty@xyzmail.com\",\r\n"
            + "  \"source\" : \"WEBSITE_FORM\",\r\n"
            + "  \"status\" : \"ACTIVE\",\r\n"
            + "  \"statusChange\" : 0,\r\n"
            + "  \"linesOfBusiness\" : [ \"ANGULAR\" ],\r\n"
            + "  \"title\" : \"President\",\r\n"
            + "  \"authority\" : true,\r\n"
            + "  \"salesOwner\" : {\r\n"
            + "   \"id\": \"6014081e221e1b534a8aa432\",\r\n"
            + "   \"firstName\":\"Milton\",\r\n"
            + "   \"lastName\":\"Jones\",\r\n"
            + "   \"email\":\"milton@xyzmail.com\",\r\n"
            + "   \"phoneNumber\":\"474-555-1212\",\r\n"
            + "   \"username\":\"milton\"\r\n"
            + "  }\r\n"
            + "}, {\r\n"
            + "  \"firstName\" : \"Lucy\",\r\n"
            + "  \"lastName\" : \"Cheng\",\r\n"
            + "  \"email\" : \"cheng@xyzmail.com\",\r\n"
            + "  \"addresses\" : [ {\r\n"
            + "    \"street1\" : \"1400 Plum Way\",\r\n"
            + "    \"city\" : \"Onisius\",\r\n"
            + "    \"state\" : \"NM\",\r\n"
            + "    \"zip\" : \"80909\",\r\n"
            + "    \"addressType\" : \"HOME\"\r\n"
            + "  } ],\r\n"
            + "  \"source\" : \"WALKIN\",\r\n"
            + "  \"status\" : \"CONTACTED\",\r\n"
            + "  \"statusChange\" : 0,\r\n"
            + "  \"linesOfBusiness\" : [ \"FULL_STACK\" ],\r\n"
            + "  \"title\" : \"Comic Relief\",\r\n"
            + "  \"authority\" : true,\r\n"
            + "  \"salesOwner\" : {\r\n"
            + "   \"id\": \"6014081e221e1b534a8aa432\",\r\n"
            + "   \"firstName\":\"Milton\",\r\n"
            + "   \"lastName\":\"Jones\",\r\n"
            + "   \"email\":\"milton@xyzmail.com\",\r\n"
            + "   \"phoneNumber\":\"474-555-1212\",\r\n"
            + "   \"username\":\"milton\"\r\n"
            + "  } \r\n"
            + "}, {\r\n"
            + "  \"firstName\" : \"Mercy\",\r\n"
            + "  \"lastName\" : \"Windsor\",\r\n"
            + "  \"email\" : \"windsor@xyzmail.com\",\r\n"
            + "  \"phones\" : [ {\r\n"
            + "    \"phone\" : \"(555) 555-5555\",\r\n"
            + "    \"phoneType\" : \"WORK\",\r\n"
            + "    \"countryCode\" : \"us\"\r\n"
            + "  } ],\r\n"
            + "  \"source\" : \"INBOUND_SALES_CALL\",\r\n"
            + "  \"status\" : \"CONTACTED\",\r\n"
            + "  \"statusChange\" : 0,\r\n"
            + "  \"linesOfBusiness\" : [ \"ANGULAR\", \"DEV_OPS\" ],\r\n"
            + "  \"title\" : \"Friend\",\r\n"
            + "  \"authority\" : true,\r\n"
            + "  \"salesOwner\" : {\r\n"
            + "   \"id\": \"6014081e221e1b534a8aa432\",\r\n"
            + "   \"firstName\":\"Milton\",\r\n"
            + "   \"lastName\":\"Jones\",\r\n"
            + "   \"email\":\"milton@xyzmail.com\",\r\n"
            + "   \"phoneNumber\":\"474-555-1212\",\r\n"
            + "   \"username\":\"milton\"\r\n"
            + "  }\r\n"
            + "}, {\r\n"
            + "  \"firstName\" : \"Blinky\",\r\n"
            + "  \"lastName\" : \"Scene\",\r\n"
            + "  \"email\" : \"scene@xmail.com\",\r\n"
            + "  \"source\" : \"EMAIL\",\r\n"
            + "  \"status\" : \"ACTIVE\",\r\n"
            + "  \"statusChange\" : 0,\r\n"
            + "  \"linesOfBusiness\" : [ \"JAVA_ENTERPRISE\" ],\r\n"
            + "  \"title\" : \"Co-Founder\",\r\n"
            + "  \"authority\" : true,\r\n"
            + "  \"salesOwner\" : {\r\n"
            + "   \"id\": \"6014081e221e1b534a8aa432\",\r\n"
            + "   \"firstName\":\"Milton\",\r\n"
            + "   \"lastName\":\"Jones\",\r\n"
            + "   \"email\":\"milton@xyzmail.com\",\r\n"
            + "   \"phoneNumber\":\"474-555-1212\",\r\n"
            + "   \"username\":\"milton\"\r\n"
            + "  } \r\n"
            + "}, {\r\n"
            + "  \"firstName\" : \"Mother\",\r\n"
            + "  \"lastName\" : \"Since\",\r\n"
            + "  \"email\" : \"since@xyzmail.com\",\r\n"
            + "  \"phones\" : [ {\r\n"
            + "    \"phone\" : \"(555) 555-5555\",\r\n"
            + "    \"phoneType\" : \"HOME\",\r\n"
            + "    \"countryCode\" : \"us\"\r\n"
            + "  } ],\r\n"
            + "  \"addresses\" : [ {\r\n"
            + "    \"street1\" : \"1222 Galaxy Way\",\r\n"
            + "    \"city\" : \"Alterion\",\r\n"
            + "    \"state\" : \"AR\",\r\n"
            + "    \"zip\" : \"22222\",\r\n"
            + "    \"country\" : \"US\",\r\n"
            + "    \"addressType\" : \"HOME\"\r\n"
            + "  } ],\r\n"
            + "  \"source\" : \"INBOUND_SALES_CALL\",\r\n"
            + "  \"status\" : \"CONTACTED\",\r\n"
            + "  \"statusChange\" : 0,\r\n"
            + "  \"linesOfBusiness\" : [ \"DEV_OPS\" ],\r\n"
            + "  \"title\" : \"Bounty Hunter\",\r\n"
            + "  \"authority\" : false,\r\n"
            + "  \"salesOwner\" : {\r\n"
            + "   \"id\": \"6014081e221e1b534a8aa432\",\r\n"
            + "   \"firstName\":\"Milton\",\r\n"
            + "   \"lastName\":\"Jones\",\r\n"
            + "   \"email\":\"milton@xyzmail.com\",\r\n"
            + "   \"phoneNumber\":\"474-555-1212\",\r\n"
            + "   \"username\":\"milton\"\r\n"
            + "  } \r\n"
            + "}, {\r\n"
            + "  \"firstName\" : \"Opus\",\r\n"
            + "  \"lastName\" : \"Mei\",\r\n"
            + "  \"email\" : \"mei@xyzmail.com\",\r\n"
            + "  \"source\" : \"EMAIL\",\r\n"
            + "  \"status\" : \"NEW\",\r\n"
            + "  \"statusChange\" : 0,\r\n"
            + "  \"linesOfBusiness\" : [ \"ANGULAR\" ],\r\n"
            + "  \"title\" : \"Large\",\r\n"
            + "  \"authority\" : true,\r\n"
            + "  \"salesOwner\" : {\r\n"
            + "   \"id\": \"6014081e221e1b534a8aa432\",\r\n"
            + "   \"firstName\":\"Milton\",\r\n"
            + "   \"lastName\":\"Jones\",\r\n"
            + "   \"email\":\"milton@xyzmail.com\",\r\n"
            + "   \"phoneNumber\":\"474-555-1212\",\r\n"
            + "   \"username\":\"milton\"\r\n"
            + "  }\r\n"
            + "}, {\r\n"
            + "  \"firstName\" : \"Yeezu\",\r\n"
            + "  \"lastName\" : \"Joy\",\r\n"
            + "  \"email\" : \"joy@xyzmail.com\",\r\n"
            + "  \"phones\" : [ {\r\n"
            + "    \"phone\" : \"(555) 555-5555\",\r\n"
            + "    \"phoneType\" : \"WORK\",\r\n"
            + "    \"countryCode\" : \"us\"\r\n"
            + "  } ],\r\n"
            + "  \"source\" : \"WALKIN\",\r\n"
            + "  \"status\" : \"INTERESTED\",\r\n"
            + "  \"statusChange\" : 0,\r\n"
            + "  \"linesOfBusiness\" : [ \"JAVA_ENTERPRISE\", \"ANGULAR\" ],\r\n"
            + "  \"title\" : \"Princess\",\r\n"
            + "  \"authority\" : true,\r\n"
            + "  \"salesOwner\" : {\r\n"
            + "   \"id\": \"6014081e221e1b534a8aa432\",\r\n"
            + "   \"firstName\":\"Milton\",\r\n"
            + "   \"lastName\":\"Jones\",\r\n"
            + "   \"email\":\"milton@xyzmail.com\",\r\n"
            + "   \"phoneNumber\":\"474-555-1212\",\r\n"
            + "   \"username\":\"milton\"\r\n"
            + "  }\r\n"
            + "}, {\r\n"
            + "  \"firstName\" : \"Frum\",\r\n"
            + "  \"lastName\" : \"Lezilia\",\r\n"
            + "  \"email\" : \"frum@xyzmail.com\",\r\n"
            + "  \"source\" : \"EMAIL\",\r\n"
            + "  \"sourceDetails\" : \"He emailed me\",\r\n"
            + "  \"status\" : \"NEW\",\r\n"
            + "  \"statusChange\" : 0,\r\n"
            + "  \"linesOfBusiness\" : [ \"ANGULAR\" ],\r\n"
            + "  \"title\" : \"President\",\r\n"
            + "  \"authority\" : true,\r\n"
            + "  \"salesOwner\" : {\r\n"
            + "   \"id\": \"6014081e221e1b534a8aa432\",\r\n"
            + "   \"firstName\":\"Milton\",\r\n"
            + "   \"lastName\":\"Jones\",\r\n"
            + "   \"email\":\"milton@xyzmail.com\",\r\n"
            + "   \"phoneNumber\":\"474-555-1212\",\r\n"
            + "   \"username\":\"milton\"\r\n"
            + "  } \r\n"
            + "}, {\r\n"
            + "  \"firstName\" : \"Bert\",\r\n"
            + "  \"lastName\" : \"Simmz\",\r\n"
            + "  \"email\" : \"bert@xyzmail.com\",\r\n"
            + "  \"addresses\" : [ {\r\n"
            + "    \"street1\" : \"111 Millennium Way\",\r\n"
            + "    \"city\" : \"Nessy\",\r\n"
            + "    \"state\" : \"CO\",\r\n"
            + "    \"addressType\" : \"HOME\"\r\n"
            + "  } ],\r\n"
            + "  \"source\" : \"EMAIL\",\r\n"
            + "  \"status\" : \"ACTIVE\",\r\n"
            + "  \"statusChange\" : 0,\r\n"
            + "  \"title\" : \"Pirate\",\r\n"
            + "  \"authority\" : false,\r\n"
            + "  \"salesOwner\" : {\r\n"
            + "   \"id\": \"6014081e221e1b534a8aa432\",\r\n"
            + "   \"firstName\":\"Milton\",\r\n"
            + "   \"lastName\":\"Jones\",\r\n"
            + "   \"email\":\"milton@xyzmail.com\",\r\n"
            + "   \"phoneNumber\":\"474-555-1212\",\r\n"
            + "   \"username\":\"milton\"\r\n"
            + "  }\r\n"
            + "}, {\r\n"
            + "  \"firstName\" : \"Governor\",\r\n"
            + "  \"lastName\" : \"Rover\",\r\n"
            + "  \"email\" : \"rover@xyzmail.com\",\r\n"
            + "  \"phones\" : [ {\r\n"
            + "    \"phone\" : \"(555) 555-5555\",\r\n"
            + "    \"phoneType\" : \"CELL\"\r\n"
            + "  } ],\r\n"
            + "  \"addresses\" : [ {\r\n"
            + "    \"city\" : \"Home City\",\r\n"
            + "    \"state\" : \"MN\",\r\n"
            + "    \"addressType\" : \"HOME\"\r\n"
            + "  } ],\r\n"
            + "  \"source\" : \"EMAIL\",\r\n"
            + "  \"status\" : \"ACTIVE\",\r\n"
            + "  \"linesOfBusiness\" : [ \"FULL_STACK\" ],\r\n"
            + "  \"title\" : \"Governor\",\r\n"
            + "  \"authority\" : true,\r\n"
            + "  \"salesOwner\" : {\r\n"
            + "   \"id\": \"6014081e221e1b534a8aa432\",\r\n"
            + "   \"firstName\":\"Milton\",\r\n"
            + "   \"lastName\":\"Jones\",\r\n"
            + "   \"email\":\"milton@xyzmail.com\",\r\n"
            + "   \"phoneNumber\":\"474-555-1212\",\r\n"
            + "   \"username\":\"milton\"\r\n"
            + "  }\r\n"
            + "} ]\r\n";
    
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
//          ObjectMapper objectMapper = new ObjectMapper();
//          TypeFactory typeFactory = objectMapper.getTypeFactory();
//          List<Account> list = (List<Account>)objectMapper.readValue(data, typeFactory.constructCollectionType(List.class, Account.class));
//
//          list.forEach(account -> {
//              this.accountRepository.save(account);
//          });
            
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            List<Contact> list = (List<Contact>)objectMapper.readValue(data, typeFactory.constructCollectionType(List.class, Contact.class));
            
            List<Account> accounts = accountRepository.findAll();
            
            int accountIndex = 0;
            
            System.err.println(list);
            for (Contact contact : list) {
                Account account = accounts.get(accountIndex);
                AccountLightweight light = new AccountLightweight();
                light.setId(account.getId());
                light.setName(account.getName());
                
                contact.setAccount(light);
                
                accountIndex++;
                if (accountIndex == accounts.size()) accountIndex = 0;
                
                this.contactRepository.save(contact);
            }
            
            System.err.println("done");
        } catch (ServiceException e) {
            System.err.println(e.getMessage() + " " + e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}