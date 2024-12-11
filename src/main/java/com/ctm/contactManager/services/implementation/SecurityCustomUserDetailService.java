package com.ctm.contactManager.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ctm.contactManager.repositories.UserRepo;

@Service
public class SecurityCustomUserDetailService implements UserDetailsService
{

    @Autowired
    private UserRepo repo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //user to load karana hai    
        return repo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not found with this email "+ username));
    }



}
