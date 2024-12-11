package com.ctm.contactManager.services.implementation;

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.ctm.contactManager.services.EmailServices;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailServices {

    private final JavaMailSender eMailSender;
    EmailServiceImpl(JavaMailSender eMailSender) {
        this.eMailSender = eMailSender;
    }

    // @Value("${spring.mail.properties.domain_name}")
    // private String domainName;

    @Override
    public void sendEmail(String to, String subject, String body,String userEmail) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setReplyTo(userEmail);

        eMailSender.send(message);
    }

    @Override
    public void sendEmail(String[] to, String subject, String body,String userEmail) {

        
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setReplyTo(userEmail);


        eMailSender.send(message);
        
    }

    @Override
    public void sendEmailWithHtml(String to, String subject, String body,String userEmail) {
        
        MimeMessage message = eMailSender.createMimeMessage();
        
        try{

            MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setReplyTo(userEmail);


            eMailSender.send(message);

        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sendEmailWithAttachment(String to, String subject, String body,File file,String userEmail) {
        
        MimeMessage message = eMailSender.createMimeMessage();

        try{

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setReplyTo(userEmail);


            FileSystemResource fileSystemResource = new FileSystemResource(file);   
            helper.addAttachment(fileSystemResource.getFilename() , file);

            eMailSender.send(message);

        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }

    // for email verification 
    @Override
    public void sendVerificationEmail(String to, String subject, String body) {
        
        MimeMessage message = eMailSender.createMimeMessage();
        
        try{

            MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            eMailSender.send(message);

        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    // for password reset
    @Override
    public void sendEmailForPasswordReset(String to, String subject, String body) {
        
        MimeMessage message = eMailSender.createMimeMessage();
        
        try{

            MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            eMailSender.send(message);

        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}