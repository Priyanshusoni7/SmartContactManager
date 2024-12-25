package com.ctm.contactManager.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.util.*;
import com.ctm.contactManager.entities.Providers;
import com.ctm.contactManager.entities.UserRoleList;
import com.ctm.contactManager.entities.user;
import com.ctm.contactManager.helper.AppConstants;
import com.ctm.contactManager.repositories.UserRepo;
import com.ctm.contactManager.repositories.UserRoleListRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserRoleListRepo userRoleListRepo;

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
       
                
                //Identify the Provider
                var oAuth2AuthenticationToken =(OAuth2AuthenticationToken)authentication;
                String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
                
                //fetching data from provider
                var oauthUser =(DefaultOAuth2User)authentication.getPrincipal();

                // oauthUser.getAttributes().forEach((k,v)->{
                //     logger.info("{} => {}",k,v);
                // });

                //making user for storing in DB
                user user1 = new user();
                user1.setUserId(UUID.randomUUID().toString());
                user1.setEnabled(true);
                user1.setEmailVerified(true);
                user1.setPassword("dummy");

                if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
                    //google
                    user1.setEmail(oauthUser.getAttribute("email").toString());
                    user1.setProfilePic(oauthUser.getAttribute("picture").toString());
                    user1.setName(oauthUser.getAttribute("name").toString());
                    user1.setProviderUserId(oauthUser.getName());
                    user1.setProvider(Providers.GOOGLE);
                    user1.setAbout("THis Account is created using google");

                    logger.info("Email: {}",user1.getProfilePic());

                }
                else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){
                    
                    //gihub
                    String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString() 
                    : oauthUser.getAttribute("login").toString() + "@gmail.com";
                    String picture = oauthUser.getAttribute("avatar_url").toString();
                    String name = oauthUser.getAttribute("name").toString();
                    String providerUserId = oauthUser.getName();

                    user1.setEmail(email);
                    user1.setProfilePic(picture);
                    user1.setName(name);
                    user1.setProviderUserId(providerUserId);
                    user1.setProvider(Providers.GITHUB);
                    user1.setAbout("THis Account is created using github");
                    
                }
                else
                {
                    logger.info("unknown provider");
                }

                //save the user 
                user checkUser = userRepo.findByEmail(user1.getEmail()).orElse(null);

                if(checkUser==null){
                    user savedUser =userRepo.save(user1);

                    UserRoleList userRoleList = new UserRoleList();
                    userRoleList.setRoleName(AppConstants.ROLE_USER);
                    userRoleList.setUser(savedUser);
                    userRoleListRepo.save(userRoleList);
                }
                new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");

                
                /*
                    DefaultOAuth2User user =(DefaultOAuth2User)authentication.getPrincipal();
                    
                    // logger.info(user.getName());

                    // user.getAttributes().forEach((k,v)->{
                    //     logger.info("{} => {}",k,v);
                    // });

                    logger.info(user.getAuthorities().toString());


                    //->before redirecting to xyz page(after login using OAuth)
                    //save the data in DB

                    user2.setPassword("password");
                    
                    user2.setProviderUserId(user.getName());
                    user2.setAbout("This account created using Google....");

                    
                 */

                // response.sendRedirect("/home");  or 

                
    }



}
