package org.example.project_java_webapplication.modules.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RegisterRequest {


    private String email;

    private String password;

    private String fullName;

    private String phone;

    private String gender;

    private String dob;

    private String address;
}