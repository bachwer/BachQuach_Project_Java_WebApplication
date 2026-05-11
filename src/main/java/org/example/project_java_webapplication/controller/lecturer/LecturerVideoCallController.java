package org.example.project_java_webapplication.controller.lecturer;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;
import org.example.project_java_webapplication.modules.mentoring.service.MentoringService;
import org.example.project_java_webapplication.modules.videoCall.service.VideoCallService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/lecturer/mentoring")
@RequiredArgsConstructor
public class LecturerVideoCallController {

    private final MentoringService mentoringService;
    private final VideoCallService videoCallService;

    @PostMapping("/video-call/start/{sessionId}")
    public String startVideoCall(@PathVariable Long sessionId, Principal principal) {
        org.example.project_java_webapplication.modules.videoCall.entity.VideoCall call = videoCallService.createCall(sessionId);
        return "redirect:/lecturer/mentoring/video-call/" + call.getRoomId();
    }

    @GetMapping("/video-call/{roomId}")
    public String videoCall(@PathVariable String roomId, Model model, @ModelAttribute("user") User user) {
        if (user == null) return "redirect:/login";
        
        org.example.project_java_webapplication.modules.videoCall.entity.VideoCall call;
        try {
            call = videoCallService.findByRoomId(roomId);
        } catch (Exception e) {
            try {
                Long sessionId = Long.parseLong(roomId);
                call = videoCallService.createCall(sessionId);
                return "redirect:/lecturer/mentoring/video-call/" + call.getRoomId();
            } catch (Exception ex) {
                return "redirect:/lecturer/mentoring/accepted";
            }
        }
        
        MentoringSession session = call.getMentoringSession();
        
        if (!session.getLecturer().getId().equals(user.getId())) {
            return "redirect:/lecturer/dashboard";
        }
        
        model.addAttribute("pageTitle", "Live Mentoring Call");
        model.addAttribute("activePage", "video-call");
        model.addAttribute("session", session);
        model.addAttribute("call", call);
        return "lecturer/mentoring/video-call";
    }

    @PostMapping("/video-call/end/{roomId}")
    public String endVideoCall(@PathVariable String roomId, RedirectAttributes ra) {
        try {
            videoCallService.endCall(roomId);
            ra.addFlashAttribute("successMessage", "Call ended successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error ending call: " + e.getMessage());
        }
        return "redirect:/lecturer/mentoring/accepted";
    }

    @GetMapping("/calls")
    public String videoCalls(Model model, @ModelAttribute("user") User lecturerUser) {
        if (lecturerUser == null) return "redirect:/login";
        model.addAttribute("pageTitle", "Video Call Center");
        model.addAttribute("activePage", "video-call");
        model.addAttribute("sessions", mentoringService.getConfirmedRequest(lecturerUser));
        return "lecturer/mentoring/calls";
    }
}
