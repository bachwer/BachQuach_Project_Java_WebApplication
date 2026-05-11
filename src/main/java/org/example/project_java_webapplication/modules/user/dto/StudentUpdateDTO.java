package org.example.project_java_webapplication.modules.user.dto;

import lombok.Data;

@Data
public class StudentUpdateDTO {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private Long departmentId;
    private String studentCode;
    private String avatarUrl;
}
