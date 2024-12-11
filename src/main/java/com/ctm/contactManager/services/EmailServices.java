package com.ctm.contactManager.services;

import java.io.File;

public interface EmailServices  {


    void sendVerificationEmail(String to, String subject, String body);

    void sendEmail(String to, String subject, String body,String userEmail);

    void sendEmail(String[] to, String subject, String body,String userEmail);

    void sendEmailWithHtml(String to, String subject, String htmlContent,String userEmail);

    void sendEmailWithAttachment(String to, String subject, String body, File file,String userEmail);

    void sendEmailForPasswordReset(String to, String subject, String body);
}
