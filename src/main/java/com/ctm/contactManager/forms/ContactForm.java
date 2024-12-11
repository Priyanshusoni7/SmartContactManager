package com.ctm.contactManager.forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid Number")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be blank")
    private String address;
    private String description;
    private boolean favorite;

    private String websiteLink;
    private String Linkedin;

    //Annotation create karenge jo file ko validate karega 
    //size
    //resolution
    private MultipartFile contactImage;

    private String picture;
}
