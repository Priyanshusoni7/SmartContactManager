package com.ctm.contactManager.forms;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserUpdateForm {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3,message = "Name must be at least 3 characters long")
    private String name;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "About cannot be blank")
    private String about;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;
    
    private MultipartFile profilePic;

}
