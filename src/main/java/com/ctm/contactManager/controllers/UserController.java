package com.ctm.contactManager.controllers;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;
import com.ctm.contactManager.entities.user;
import com.ctm.contactManager.forms.SendMailForm;
import com.ctm.contactManager.forms.UserUpdateForm;
import com.ctm.contactManager.helper.Helper;
import com.ctm.contactManager.helper.Message;
import com.ctm.contactManager.helper.MessageType;
import com.ctm.contactManager.services.EmailServices;
import com.ctm.contactManager.services.ImageService;
import com.ctm.contactManager.services.UserServices;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.var;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServices userService;

    org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    EmailServices emailService;

    @Autowired
    ImageService imageService;

    @GetMapping("/dashboard")
    public String getUserDashboard() {
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String getUserProfile(Model model, Authentication authentication) {
        return "user/profile";
    }

    @GetMapping("/profile/edit/{userEmail}")
    public String userUpdateFormView(@PathVariable("userEmail") String userEmail, Model model) {
        user user1 = userService.getUserByEmail(userEmail);

        UserUpdateForm userForm = new UserUpdateForm();
        userForm.setName(user1.getName());
        userForm.setEmail(user1.getEmail());
        userForm.setAbout(user1.getAbout());
        userForm.setPhoneNumber(user1.getPhoneNumber());

        model.addAttribute("userForm", userForm);
        model.addAttribute("userEmail", userEmail);
        return "user/update_user_form";
    }

    @PostMapping("/profile/edit/{userEmail}")
    public String updateUser(@PathVariable("userEmail") String userEmail,
            @Valid @ModelAttribute UserUpdateForm userForm, BindingResult rBindingResult, Model model,
            HttpSession session) {
        if (rBindingResult.hasErrors()) {
            
            model.addAttribute("userForm", userForm);
            model.addAttribute("userEmail", userEmail);
            session.setAttribute("message",
                    Message.builder().content("Please correct the following errors").type(MessageType.red).build());
            return "user/update_user_form";
        }

        user user1 = userService.getUserByEmail(userEmail);
        user1.setName(userForm.getName());
        user1.setEmail(userForm.getEmail());
        user1.setAbout(userForm.getAbout());
        user1.setPhoneNumber(userForm.getPhoneNumber());

        // process image
        if (userForm.getProfilePic() != null && !userForm.getProfilePic().isEmpty()) {
            // publicId of the image for cloudinary
            String filename = UUID.randomUUID().toString();
            String fileURL = imageService.uploadImage(userForm.getProfilePic(), filename);
            user1.setProfilePic(fileURL);
        }

        var user2 = userService.updateUser(user1);
        Message message = Message.builder().content("You have successfully updated Your Profile").type(MessageType.blue)
                .build();
        session.setAttribute("message", message);

        return "redirect:/user/profile";
    }

    @GetMapping("/mail")
    public String directMail(Model model) {

        SendMailForm mailForm = new SendMailForm();

        model.addAttribute("mailForm", mailForm);
        return "user/send_mail";
    }

    @PostMapping("/mail")
    public String sendMail(@ModelAttribute SendMailForm mailForm, BindingResult rBindingResult, Model model,
            Authentication authentication) {
        
        String userEmail =Helper.getEmailOfLoggedinUser(authentication);

        if (rBindingResult.hasErrors()) {
            model.addAttribute("mailForm", mailForm);
            return "user/send_mail"; // Return the form if validation errors exist
        }

    try
    {
        switch (mailForm.getEmailType()) {
            case "single":
                emailService.sendEmail(mailForm.getTo(), mailForm.getSubject(), mailForm.getBody(),userEmail);
                break;
    
            case "multiple":
                String[] recipients = mailForm.getTo().split(","); // Split comma-separated emails
                emailService.sendEmail(recipients, mailForm.getSubject(), mailForm.getBody(),userEmail);
                break;
    
            case "html":
                emailService.sendEmailWithHtml(mailForm.getTo(), mailForm.getSubject(), mailForm.getBody(),userEmail);
                break;
    
            case "file":
                if (mailForm.getFile() != null && !mailForm.getFile().isEmpty()) {
                    File convertedFile = null;
                    try {
                        convertedFile = convertMultipartFileToFile(mailForm.getFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    emailService.sendEmailWithAttachment(mailForm.getTo(), mailForm.getSubject(), mailForm.getBody(), convertedFile, userEmail);
                } else {
                    logger.error("No file uploaded for email with attachment.");
                }
                break;
    
            default:
                logger.error("Invalid email type selected.");
        }

    }
    catch(Exception e)
    {
        logger.error("Error sending email: {}", e.getMessage());
        throw new RuntimeException(e);
    }

        return "redirect:/user/mail";
    }




    // Utility method to convert MultipartFile to File
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException 
    {
        File convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(convertedFile);
        return convertedFile;
    }

}