package org.example.project_java_webapplication.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 200, message = "Email must not exceed 100 characters")
    private String email;


    @NotBlank(message = "Password is required")
    @Size(
            min = 6,
            max = 200,
            message = "Password must be between 8 and 20 characters"
    )
    private String password;
}