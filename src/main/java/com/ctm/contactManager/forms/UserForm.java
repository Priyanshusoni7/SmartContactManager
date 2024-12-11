package com.ctm.contactManager.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {
    //registration form
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3,message = "Name must be at least 3 characters long")
    private String name;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6,message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "About cannot be blank")
    private String about;

    @Size(min = 10,message = "Invalid Number")
    private String phoneNumber;
    

}
