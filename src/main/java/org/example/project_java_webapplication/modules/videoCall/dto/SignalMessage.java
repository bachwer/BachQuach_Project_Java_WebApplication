package org.example.project_java_webapplication.modules.videoCall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignalMessage {

    private String action;

    private String username;

    private String target;

    private Object data;

}