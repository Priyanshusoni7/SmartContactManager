package com.ctm.contactManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ctm.contactManager.entities.user;
import com.ctm.contactManager.forms.UserForm;
import com.ctm.contactManager.helper.Helper;
import com.ctm.contactManager.helper.Message;
import com.ctm.contactManager.helper.MessageType;
import com.ctm.contactManager.services.EmailServices;
import com.ctm.contactManager.services.UserServices;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private EmailServices emailServices;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("activePage", "home");
        return "home";
    }

    @GetMapping("/about")
    public String aboutpage(Model model) {
        model.addAttribute("activePage", "about");

        return "about";
    }

    @GetMapping("/service")
    public String servicePage(Model model) {
        model.addAttribute("activePage", "service");

        return "service";
    }

    // this is showing login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/tips")
    public String tipsPage() {
        return "tips";
    }

    @GetMapping("/faqs")
    public String faqsPage() {
        return "faqs";
    }



    @GetMapping("/reset_password_Form")
    public String resetPasswordForm()
    {
        return "reset_passwordform";
    }

    @GetMapping("/forgot_password")
    public String forgotPassword() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, HttpSession session) {

        if(!userServices.checkUserExistByEmail(email))
        {
            Message message = Message.builder()
            .content("User Doesn't exists, Please register your self")
            .type(MessageType.blue)
            .build();
            session.setAttribute("message", message);
            return "redirect:/forgot_password";
        }

        try{

            emailServices.sendEmailForPasswordReset(email, "Reset Password for SCM Account" , Helper.EmailVerificationForPasswordReset(email));

        }
        catch(Exception e){
            Message message = Message.builder()
            .content("Something went wrong while sending Email, Please try again")
            .type(MessageType.red)
            .build();
            session.setAttribute("message", message);
            return "redirect:/forgot_password";
        }


        Message message = Message.builder()
            .content("A password reset link has been sent to your email address. Please check your email.")
            .type(MessageType.green)
            .build();
            session.setAttribute("message", message);
        return "redirect:/forgot_password";
    }
    
    
    // this is registration page
    @GetMapping("/register")
    public String registerPage(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "register";
    }

    // this is process registration
    @PostMapping("/do-register")
    public String registerUser(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,
            HttpSession session, Model model) {

        // validation of data
        if (rBindingResult.hasErrors()) {
            // it will carry errors with it in registerPage.
            return "register";
        }

        // check if user already exists
        try {
            if (userServices.checkUserExistByEmail(userForm.getEmail())) {
                Message message = Message.builder()
                        .content("User already exists, Please register with another email")
                        .type(MessageType.red)
                        .build();
                session.setAttribute("message", message);
                return "redirect:/register";
            }
        } catch (Exception e) {
            Message message = Message.builder()
                    .content("An error occurred while checking user existence")
                    .type(MessageType.red)
                    .build();
            session.setAttribute("message", message);
            return "redirect:/register";
        }

        // userForm ---to---> user
        // user user1 = user.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .about(userForm.getAbout())
        // .phoneNumber(userForm.getPhoneNumber())
        // .profilePic("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.istockphoto.com%2Fphotos%2Fdefault-profile-picture&psig=AOvVaw1GQxn_te0vk3TxJUN7MrLK&ust=1725988258476000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCIDRu7qttogDFQAAAAAdAAAAABAI")
        // .build();

        // userForm ---to---> user

        try {

            user user1 = new user();
            user1.setName(userForm.getName());
            user1.setEmail(userForm.getEmail());
            user1.setPassword(userForm.getPassword());
            user1.setAbout(userForm.getAbout());
            user1.setPhoneNumber(userForm.getPhoneNumber());
            user1.setEnabled(false);
            user1.setProfilePic("");

            userServices.saveUser(user1);

            // add a Message(User Registered Successfully)
            Message message = Message.builder()
                    .content("Registered Successfully, Verify your Account by clicking the link sent to your email")
                    .type(MessageType.blue).build();

            session.setAttribute("message", message);

            model.addAttribute("userEmail", userForm.getEmail());

            return "resend_email";

        } catch (Exception e) {

            Message message = Message.builder()
                    .content("An error occurred while registering user")
                    .type(MessageType.red)
                    .build();
            session.setAttribute("message", message);
            return "redirect:/register";
        }

    }

}