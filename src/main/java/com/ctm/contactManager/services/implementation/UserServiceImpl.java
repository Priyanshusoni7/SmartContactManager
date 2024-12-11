package com.ctm.contactManager.services.implementation;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ctm.contactManager.entities.user;
import com.ctm.contactManager.helper.AppConstants;
import com.ctm.contactManager.helper.Helper;
import com.ctm.contactManager.helper.ResourceNotFoundException;
import com.ctm.contactManager.repositories.UserRepo;
import com.ctm.contactManager.services.EmailServices;
import com.ctm.contactManager.services.UserServices;

@Service
public class UserServiceImpl implements UserServices 
{
    @Autowired
    private EmailServices emailServices;

    // @Autowired
    // JavaMailSender javaMailSender;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public user saveUser(user user) 
    {
        //userId have to generate automatically 
        String userId = UUID.randomUUID().toString();   
        user.setUserId(userId);

        //encoded password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //set user_role
        user.setRoleList(List.of(AppConstants.ROLE_USER));


        //email verification process ->

        String emailToken  = UUID.randomUUID().toString();

        //save EmailToken in DB
        user.setEmailToken(emailToken);
        user savedUser = userRepo.save(user);

        String verificationLink = Helper.getLinkForEmailVerification(emailToken);

        // sending mail function
        emailServices.sendVerificationEmail(savedUser.getEmail(),"SCM Account Verification", verificationLink);

        return savedUser;

    }

    @Override
    public Optional<user> getUserById(String id) {
       
        return userRepo.findById(id);
        
    }

    @Override
    public Optional<user> updateUser(user user) {
        
        user user1 = userRepo.findById(user.getUserId()).orElseThrow(()-> new ResourceNotFoundException("Resource not found"));
        //update karenge user1(jo DB se aya h) and user(form se aya h)
        user1.setName(user.getName());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        user1.setAbout(user.getAbout());
        user1.setProfilePic(user.getProfilePic());
        user1.setPhoneNumber(user.getPhoneNumber());
        user1.setProvider(user.getProvider());
        user1.setProviderUserId(user.getProviderUserId());
        user1.setEnabled(user.isEnabled());
        user1.setEmailVerified(user.isEmailVerified());
        user1.setPhoneVerified(user.isPhoneVerified());

        //save the user in DB
       user save = userRepo.save(user1);

       return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        user user1 = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Resource not found"));
        userRepo.delete(user1);
    }

    @Override
    public boolean checkUserExist(String userId) {
        user user1 = userRepo.findById(userId).orElse(null);
        if(user1 != null)
            return true;
        return false;
    }

    @Override
    public boolean checkUserExistByEmail(String email) {
            user user1 = userRepo.findByEmail(email).orElse(null);
           return user1 != null ? true : false;
    }

    @Override
    public List<user> getAllUsers() {
       List<user> userList = userRepo.findAll();
       return userList;
    }

    @Override
    public user getUserByEmail(String email) {
        
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    public void resendVerificationEmail(user user) {
        
        String verificationLink = Helper.getLinkForEmailVerification(user.getEmailToken());
        emailServices.sendVerificationEmail(user.getEmail(), "SCM Account Verification", verificationLink);
    }

}
