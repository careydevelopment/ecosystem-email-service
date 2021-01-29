package com.careydevelopment.ecosystem.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/product")
public class ProductController {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);
        

}
