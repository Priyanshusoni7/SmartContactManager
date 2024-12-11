package com.ctm.contactManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ctm.contactManager.entities.contact;
import com.ctm.contactManager.services.ContactServices;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactServices contactServices;

    @GetMapping("/contacts/{contactId}")
    public contact getSingleContact(@PathVariable String contactId) {

        return contactServices.getById(contactId);
    }
    
}