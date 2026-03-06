package com.demo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest {

    @NotBlank(message = "Username is required!")
    @Size(min = 3, max = 32, message = "username length must be between 3 to 32!")
    private String username;

    @NotBlank(message = "Password is required!")
    private String password;
    @Email(message = "Email id is required!!")
    private String email;

}
