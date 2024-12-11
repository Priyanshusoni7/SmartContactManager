package com.ctm.contactManager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.ctm.contactManager.entities.user;
import com.ctm.contactManager.helper.Helper;
import com.ctm.contactManager.services.UserServices;


//This Method will run for 'Every Request'
@ControllerAdvice
public class RootController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserServices userService;

    //This Method will run for 'Every Request'
    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {

        if(authentication == null)
        {
            return;
        }

        //fetching username from login.
        String username = Helper.getEmailOfLoggedinUser(authentication);

        logger.info("User name: {}", username);

        //fetching user details by this username
        user user1 = userService.getUserByEmail(username);

        System.out.println(user1);

        System.out.println(user1.getName());
        System.out.println(user1.getEmail());
        model.addAttribute("loggedInUser", user1);
        
    }
}
