package com.ctm.contactManager.services.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.ctm.contactManager.entities.contact;
import com.ctm.contactManager.entities.user;
import com.ctm.contactManager.helper.ResourceNotFoundException;
import com.ctm.contactManager.repositories.ContactRepo;
import com.ctm.contactManager.services.ContactServices;

@Service
public class ContactServiceImpl implements ContactServices {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public contact saveContact(contact contact) {

        String Id = UUID.randomUUID().toString();
        contact.setId(Id);
       
        return contactRepo.save(contact);
    }

    @Override
    public contact updateContact(contact contact) {
        
        
        contact contactOld = contactRepo.findById(contact.getId()).orElseThrow(()-> new ResourceNotFoundException("Contact not found with id "+ contact.getId()));

        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setDescription(contact.getDescription());
        contactOld.setLinkedin(contact.getLinkedin());
        contactOld.setFavorite(contact.isFavorite());
        contactOld.setPicture(contact.getPicture());
        contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        return contactRepo.save(contactOld);
    }

    @Override
    public List<contact> getAllContact() {
        List<contact> contactList = contactRepo.findAll();
        return contactList;
    }

    @Override
    public contact getById(String id) {
        return contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found with id "+ id));
    }

    @Override
    public void deleteContact(String id) {
        var contact = contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found with id "+ id));

        contactRepo.delete(contact);
    }

    @Override
    public List<contact> getByUserId(String userId) {
       
        return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<contact> getByUser(user user, int page, int size, String sortBy, String direction) {
        
        Sort sort = direction.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        var pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUser(user, pageable);
    }

    @Override
    public Page<contact> searchByName(String name, int size, int page, String sortBy, String order,user user) {
        
        Sort sort = order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        var pageable = PageRequest.of(page, size, sort);  

        return contactRepo.findByUserAndNameContaining(user, name, pageable);
    }

    @Override
    public Page<contact> searchByPhone(String phone, int size, int page, String sortBy, String order,user user) {
        Sort sort = order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        var pageable = PageRequest.of(page, size, sort);  

        return contactRepo.findByUserAndPhoneNumberContaining(user, phone, pageable);
    }

    @Override
    public Page<contact> searchByEmail(String email, int size, int page, String sortBy, String order,user user) {
        Sort sort = order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        var pageable = PageRequest.of(page, size, sort);  

        return contactRepo.findByUserAndEmailContaining(user, email, pageable);
    }

  
}