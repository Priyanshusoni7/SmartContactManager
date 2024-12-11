package com.ctm.contactManager.controllers;

import java.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ctm.contactManager.entities.contact;
import com.ctm.contactManager.entities.user;
import com.ctm.contactManager.forms.ContactForm;
import com.ctm.contactManager.forms.ContactSearchForm;
import com.ctm.contactManager.helper.AppConstants;
import com.ctm.contactManager.helper.Helper;
import com.ctm.contactManager.helper.Message;
import com.ctm.contactManager.helper.MessageType;
import com.ctm.contactManager.services.ContactServices;
import com.ctm.contactManager.services.ImageService;
import com.ctm.contactManager.services.UserServices;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ContactServices contactServices;

    @Autowired
    UserServices userServices;

    @Autowired
    ImageService imageService;

    @GetMapping("/add")
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add-contact";
    }

    @PostMapping("/add")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult rBindingResult,
            Authentication authentication, HttpSession session) {

        if (rBindingResult.hasErrors()) {

            rBindingResult.getAllErrors().forEach(errors -> logger.info(errors.toString()));

            session.setAttribute("message",
                    Message.builder().content("Please correct the following errors").type(MessageType.red).build());
            // it will carry errors with it in add page...
            return "user/add-contact";
        }

        // for geting the user bcz to know the -- whose contact is of ?
        String username = Helper.getEmailOfLoggedinUser(authentication);
        user user = userServices.getUserByEmail(username);

        // process the add-contact form data..
        // Form ---> contact Entity
        contact contact = new contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedin(contactForm.getLinkedin());
        contact.setDescription(contactForm.getDescription());
        contact.setFavorite(contactForm.isFavorite());

        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            // filename -> public_id of the image to save in clodinary
            String filename = UUID.randomUUID().toString();

            // image process -- getting url of image
            String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);

            contact.setPicture(fileURL);
            // image public_Id
            contact.setCloudinaryImagePublicId(filename);

        }

        // to get user --> logic upside
        contact.setUser(user);

        // save to db
        contactServices.saveContact(contact);

        // message for submission of contact..
        session.setAttribute("message",
                Message.builder().content("You have successfully added a contact").type(MessageType.blue).build());

        return "redirect:/user/contacts/add";
    }

    @GetMapping
    public String viewContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication) 
            {
                
        String username = Helper.getEmailOfLoggedinUser(authentication);

        user user = userServices.getUserByEmail(username);

        Page<contact> pageContacts = contactServices.getByUser(user, page, size, sortBy, direction);

        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        model.addAttribute("ContactSearchForm", new ContactSearchForm());

        return "user/view-contacts";
    }

    @GetMapping("/search")
    public String searchHandler(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication) 
            {
                
        var user = userServices.getUserByEmail(Helper.getEmailOfLoggedinUser(authentication));

        Page<contact> pageContacts;
        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContacts = contactServices.searchByName(contactSearchForm.getKeyword(), size, page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
            pageContacts = contactServices.searchByPhone(contactSearchForm.getKeyword(), size, page, sortBy, direction,
                    user);
        } else {
            pageContacts = contactServices.searchByEmail(contactSearchForm.getKeyword(), size, page, sortBy, direction,
                    user);
        }

        model.addAttribute("ContactSearchForm", contactSearchForm);

        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "user/search-contacts";
    }

    @GetMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") String contactId, HttpSession session) {

        contactServices.deleteContact(contactId);

        Message message = Message.builder().content("You have successfully deleted a contact").type(MessageType.green)
                .build();

        session.setAttribute("message", message);

        return "redirect:/user/contacts";
    }

    // update contact form view
    @GetMapping("/updateform/{contactId}")
    public String updateContactFormView(@PathVariable("contactId") String contactId, Model model) {

        contact contact = contactServices.getById(contactId);

        ContactForm contactDetails = new ContactForm();

        contactDetails.setName(contact.getName());
        contactDetails.setEmail(contact.getEmail());
        contactDetails.setPhoneNumber(contact.getPhoneNumber());
        contactDetails.setAddress(contact.getAddress());
        contactDetails.setWebsiteLink(contact.getWebsiteLink());
        contactDetails.setLinkedin(contact.getLinkedin());
        contactDetails.setDescription(contact.getDescription());
        contactDetails.setFavorite(contact.isFavorite());
        contactDetails.setPicture(contact.getPicture());

        model.addAttribute("contactForm", contactDetails);
        model.addAttribute("contactId", contactId);

        return "user/update_contact_form";
    }

    @PostMapping("/update/{contactId}")
    public String updateContact(@PathVariable("contactId") String contactId,
            @Valid @ModelAttribute ContactForm contactForm, BindingResult rBindingResult, Model model,
            HttpSession session) {

        if (rBindingResult.hasErrors()) {

            session.setAttribute("message",
                    Message.builder().content("Please correct the following errors").type(MessageType.red).build());
            return "user/update_contact_form";
        }

        var contact = contactServices.getById(contactId);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedin(contactForm.getLinkedin());
        contact.setDescription(contactForm.getDescription());
        contact.setFavorite(contactForm.isFavorite());

        // process image
        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            // publicId of the image for cloudinary
            String filename = UUID.randomUUID().toString();
            String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);
            contact.setPicture(fileURL);
            contact.setCloudinaryImagePublicId(filename);
        }

        var updatedCon = contactServices.updateContact(contact);

        Message message = Message.builder().content("You have successfully updated a contact").type(MessageType.blue)
                .build();
        session.setAttribute("message", message);

        return "redirect:/user/contacts";
    }
}