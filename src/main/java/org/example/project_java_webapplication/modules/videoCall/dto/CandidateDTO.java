package org.example.project_java_webapplication.modules.videoCall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateDTO {

    // =====================================
    // ICE CANDIDATE
    // =====================================

    private String candidate;

    // =====================================
    // SDP MID
    // =====================================

    private String sdpMid;

    // =====================================
    // SDP M LINE INDEX
    // =====================================

    private Integer sdpMLineIndex;

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