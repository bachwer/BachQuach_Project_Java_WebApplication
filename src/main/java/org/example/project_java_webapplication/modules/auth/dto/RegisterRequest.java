package org.example.project_java_webapplication.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(
            min = 8,
            max = 200,
            message = "Password must be between 8 and 20 characters"
    )
//    @Pattern(
//            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
//            message = "Password must contain uppercase, lowercase, number and special character"
//    )
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ỹ\\s]+$",
            message = "Full name must not contain numbers or special characters"
    )
    private String fullName;

    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^(0|\\+84)[0-9]{9}$",
            message = "Invalid Vietnamese phone number"
    )
    private String phone;

    @NotBlank(message = "Gender is required")
    @Pattern(
            regexp = "^(MALE|FEMALE|OTHER)$",
            message = "Gender must be MALE, FEMALE or OTHER"
    )
    private String gender;

    @NotBlank(message = "Date of birth is required")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "Date of birth must follow yyyy-MM-dd format"
    )
    private String dob;

    @NotBlank(message = "Address is required")
    @Size(
            min = 5,
            max = 255,
            message = "Address must be between 5 and 255 characters"
    )
    private String address;
}