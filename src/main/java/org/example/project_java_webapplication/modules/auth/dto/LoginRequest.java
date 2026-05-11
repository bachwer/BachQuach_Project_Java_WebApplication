package org.example.project_java_webapplication.modules.auth.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String email;

    private String password;
}