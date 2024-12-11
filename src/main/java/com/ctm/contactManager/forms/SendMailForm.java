package com.ctm.contactManager.forms;


import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SendMailForm {

    private String to;
    private String subject;
    private String body;
    private MultipartFile file; 
    private String emailType; 

}
