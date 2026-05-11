package org.example.project_java_webapplication.modules.videoCall.util;

import java.util.UUID;

public class RoomIdGenerator {

    // =====================================
    // GENERATE ROOM ID
    // =====================================

    public static String generateRoomId() {

        return "room-"
                + UUID.randomUUID()
                .toString()
                .replace("-", "");

    }

    // =====================================
    // GENERATE SHORT ROOM ID
    // =====================================

    public static String generateShortRoomId() {

        return "room-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8);

    }

    // =====================================
    // GENERATE SESSION ROOM
    // =====================================

    public static String generateSessionRoom(

            Long mentoringSessionId,

            Long lecturerId,

            Long studentId

    ) {

        return "mentor-"
                + mentoringSessionId
                + "-"
                + lecturerId
                + "-"
                + studentId
                + "-"
                + System.currentTimeMillis();

    }
}