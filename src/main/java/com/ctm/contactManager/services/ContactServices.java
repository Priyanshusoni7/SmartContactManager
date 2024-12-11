package com.ctm.contactManager.services;

import java.util.List;
import org.springframework.data.domain.Page;
import com.ctm.contactManager.entities.contact;
import com.ctm.contactManager.entities.user;

public interface ContactServices {

    contact saveContact(contact contact);

    contact updateContact(contact contact);

    List<contact> getAllContact();

    contact getById(String id); 

    void deleteContact(String id);

    Page<contact> searchByName(String name, int size, int page, String sortBy, String order, user user);

    Page<contact> searchByPhone(String phone, int size, int page, String sortBy, String order, user user);

    Page<contact> searchByEmail(String email, int size, int page, String sortBy, String order, user user);

    //get contacts by UserId
    List<contact> getByUserId(String userId);

    Page<contact> getByUser(user user, int page, int size, String sortField, String sortDirection);

}
