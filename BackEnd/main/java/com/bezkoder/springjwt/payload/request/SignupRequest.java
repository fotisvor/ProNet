package com.bezkoder.springjwt.payload.request;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Value
public class SignupRequest {

    @NotBlank(message = "username is required")
    @Size(min = 3, max = 20)
    String username;

    @NotBlank(message = "email is required")
    @Size(max = 50)
    @Email
    String email;

    Set<String> roles;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 40)
    String password;

    @NotBlank(message = "name is required")
    @Size(max = 50)
    String name;

    @NotBlank(message = "surname is required")
    @Size(max = 50)
    String surname;

    @NotBlank(message = "phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    String phone;



}
