package org.example.project_java_webapplication.controller.student;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.videoCall.entity.VideoCall;
import org.example.project_java_webapplication.modules.videoCall.service.VideoCallService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/student/mentoring")
@RequiredArgsConstructor
public class StudentVideoCallController {

    private final VideoCallService videoCallService;

    @GetMapping("/video-call/{roomId}")
    public String videoCall(@PathVariable String roomId, Model model, Principal principal) {
        if (principal == null) return "redirect:/login";
        
        VideoCall call = videoCallService.findByRoomId(roomId);
            
        model.addAttribute("call", call);
        model.addAttribute("session", call.getMentoringSession());
        
        return "student/mentoring/video-call";
    }

    @PostMapping("/video-call/leave")
    public String leaveVideoCall() {
        return "redirect:/student/mentoring/my-sessions";
    }
}
