package com.ctm.contactManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ctm.contactManager.entities.user;
import com.ctm.contactManager.helper.Message;
import com.ctm.contactManager.helper.MessageType;
import com.ctm.contactManager.repositories.UserRepo;
import com.ctm.contactManager.services.UserServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserServices userServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/auth/verify-email")
    public String checkEmailVerified(@RequestParam("token") String token, HttpSession session) {
        user user = userRepo.findByEmailToken(token).orElse(null);

        if (user != null) {
            user.setEmailVerified(true);
            user.setEnabled(true);
            userRepo.save(user);

            Message message = Message.builder().content("Email has been verified").type(MessageType.blue).build();
            session.setAttribute("message", message);
            return "success_page";
        }

        Message message = Message.builder().content("Something went wrong!!").type(MessageType.red).build();
        session.setAttribute("message", message);

        return "error_page";
    }

    


    @GetMapping("user/auth/resendVerificationEmail/{userEmail}")
    public String resendVerificationEmail(@PathVariable("userEmail") String userEmail, HttpSession session,Model model) {
        var user = userServices.getUserByEmail(userEmail);

        userServices.resendVerificationEmail(user);

        Message message = Message.builder().content("Verification email has been sent Again").type(MessageType.green).build();
        session.setAttribute("message", message);

        return "resend_email";
    }

    @GetMapping("/auth/reset-password")
    public String resetPassword(@RequestParam("email") String email, HttpSession session ,Model model) 
    {
        if(userServices.checkUserExistByEmail(email))
        {
                model.addAttribute("userEmail", email);
            return "reset_passwordform";    

        }

        Message message = Message.builder().content("Email doesn't exist, resgister yourself").type(MessageType.red).build();
        session.setAttribute("message", message);
        return "redirect:/register";    

    }

    @PostMapping("/auth/do-reset-password")
    public String doResetPassword(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session)
    {
        if(userServices.checkUserExistByEmail(email))
        {
            user user = userServices.getUserByEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            userServices.updateUser(user);
            Message message = Message.builder().content("Password changed successfully").type(MessageType.green).build();
            session.setAttribute("message", message);

            return "login";
        }

        Message message = Message.builder().content("Email doesn't exist, resgister yourself").type(MessageType.red).build();
        session.setAttribute("message", message);
        return "register";
    }
}
