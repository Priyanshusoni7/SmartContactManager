package com.ctm.contactManager.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    //google & git se login kiya to userId aa rahi he 
    //ishiye alag function bananna padega
    public static String getEmailOfLoggedinUser(Authentication authentication)
    {
        if(authentication instanceof OAuth2AuthenticationToken)
        {
            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User)authentication.getPrincipal();

            String username ="";

            if(clientId.equalsIgnoreCase("google"))
            {
                //login by google
                System.out.println("LoggedIn by google");
                username = oauth2User.getAttribute("email").toString();

            }
            else if(clientId.equalsIgnoreCase("github"))
            {
                //login by github
                System.out.println("LoggedIn by github");
                username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString() 
                    : oauth2User.getAttribute("login").toString() + "@gmail.com";
            }
            
            return username;
        }
        else //agar OAuth nhi to fir regular login 
        {
            System.out.println("LoggedIn by username and password");
            return authentication.getName();
        }
    }


    // public static String getLinkForEmailVerification(String token)
    // {
    //     String link = "http://localhost:8081/auth/verify-email?token="+token;
    //     return link;
    // }


    public static String getLinkForEmailVerification(String token) {
        String link = "http://localhost:8081/auth/verify-email?token=" + token;
        return 
            "<html>" +
            "    <body style=\"font-family: Arial, sans-serif; line-height: 1.6; background-color: #f9fafb; padding: 20px; color: #374151;\">" +
            "        <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\">" +
            "            <div style=\"background-color: #4CAF50; padding: 20px; text-align: center;\">" +
            "                <h2 style=\"color: #ffffff; font-size: 24px; margin: 0;\">Welcome to Smart Contact Management (SCM)!</h2>" +
            "            </div>" +
            "            <div style=\"padding: 20px;\">" +
            "                <p style=\"font-size: 16px; margin-bottom: 20px;\">Dear User,</p>" +
            "                <p style=\"font-size: 16px; margin-bottom: 20px;\">Thank you for registering with SCM! We are excited to have you onboard. To complete your registration and start managing your contacts efficiently, please verify your email address by clicking the button below:</p>" +
            "                <div style=\"text-align: center; margin-bottom: 20px;\">" +
            "                    <a href=\"" + link + "\"" +
            "                       style=\"display: inline-block; padding: 12px 24px; font-size: 16px; color: #ffffff; background-color: #4CAF50; text-decoration: none; border-radius: 8px;\">" +
            "                       Verify My Email" +
            "                    </a>" +
            "                </div>" +
            "                <p style=\"font-size: 16px; margin-bottom: 20px;\">If the above button doesn’t work, copy and paste the following URL into your browser:</p>" +
            "                <p style=\"font-size: 16px; word-wrap: break-word;\">" +
            "                    <a href=\"" + link + "\" style=\"color: #4CAF50;\">" + link + "</a>" +
            "                </p>" +
            "                <p style=\"font-size: 16px; margin-bottom: 20px;\">Once verified, you will gain access to features like:</p>" +
            "                <ul style=\"list-style-type: disc; padding-left: 20px; font-size: 16px; margin-bottom: 20px;\">" +
            "                    <li>Secure contact management.</li>" +
            "                    <li>Effortless email communication directly from the platform.</li>" +
            "                    <li>Responsive design for all devices.</li>" +
            "                    <li>Export contacts to Word documents.</li>" +
            "                </ul>" +
            "                <p style=\"font-size: 16px; margin-bottom: 20px;\">If you didn’t register for an SCM account, please ignore this email.</p>" +
            "                <p style=\"font-size: 16px; margin-bottom: 20px;\">For any questions, feel free to contact our support team.</p>" +
            "                <p style=\"font-size: 16px;\">Best regards,</p>" +
            "                <p style=\"font-size: 16px; font-weight: bold;\">Smart Contact Management Team</p>" +
            "            </div>" +
            "        </div>" +
            "    </body>" +
            "</html>";
    }
    

    public static String EmailVerificationForPasswordReset(String email) {
        String link = "http://localhost:8081/auth/reset-password?email=" + email;
        return 
        "<html>" +
        "    <body style=\"font-family: Arial, sans-serif; line-height: 1.6; background-color: #f9fafb; padding: 20px; color: #374151;\">" +
        "        <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\">" +
        "            <div style=\"background-color: #4CAF50; padding: 20px; text-align: center;\">" +
        "                <h2 style=\"color: #ffffff; font-size: 24px; margin: 0;\">Password Reset Request for SCM</h2>" +
        "            </div>" +
        "            <div style=\"padding: 20px;\">" +
        "                <p style=\"font-size: 16px; margin-bottom: 20px;\">Dear User,</p>" +
        "                <p style=\"font-size: 16px; margin-bottom: 20px;\">We received a request to reset the password for your Smart Contact Management (SCM) account. If you did not make this request, please ignore this email. To reset your password, please click the button below:</p>" +
        "                <div style=\"text-align: center; margin-bottom: 20px;\">" +
        "                    <a href=\"" + link + "\"" +
        "                       style=\"display: inline-block; padding: 12px 24px; font-size: 16px; color: #ffffff; background-color: #4CAF50; text-decoration: none; border-radius: 8px;\">" +
        "                       Reset My Password" +
        "                    </a>" +
        "                </div>" +
        "                <p style=\"font-size: 16px; margin-bottom: 20px;\">If the above button doesn’t work, copy and paste the following URL into your browser:</p>" +
        "                <p style=\"font-size: 16px; word-wrap: break-word;\">" +
        "                    <a href=\"" + link + "\" style=\"color: #4CAF50;\">" + link + "</a>" +
        "                </p>" +
        "                <p style=\"font-size: 16px; margin-bottom: 20px;\">Once you reset your password, you will be able to log in and start using your SCM account again. If you have any issues, feel free to contact our support team.</p>" +
        "                <p style=\"font-size: 16px; margin-bottom: 20px;\">If you didn’t request a password reset, please ignore this email.</p>" +
        "                <p style=\"font-size: 16px; margin-bottom: 20px;\">For any questions, feel free to contact our support team.</p>" +
        "                <p style=\"font-size: 16px;\">Best regards,</p>" +
        "                <p style=\"font-size: 16px; font-weight: bold;\">Smart Contact Management Team</p>" +
        "            </div>" +
        "        </div>" +
        "    </body>" +
        "</html>";
    }

}
