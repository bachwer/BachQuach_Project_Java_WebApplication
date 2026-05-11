package org.example.project_java_webapplication.modules.videoCall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDTO {

    // =====================================
    // SDP TYPE
    // =====================================

    private String type;

    // =====================================
    // SDP DATA
    // =====================================

    private String sdp;

    // =====================================
    // ROOM ID
    // =====================================

    private String roomId;

    // =====================================
    // FROM USER
    // =====================================

    private String from;

    // =====================================
    // TARGET USER
    // =====================================

    private String target;

}