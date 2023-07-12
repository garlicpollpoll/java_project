package com.study_site.java_project.web.controller.study;

import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.ParticipateRoom;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.entity.User;
import com.study_site.java_project.web.repository.MemberRepository;
import com.study_site.java_project.web.repository.ParticipateRoomRepository;
import com.study_site.java_project.web.repository.StudyRoomRepository;
import com.study_site.java_project.web.repository.UserRepository;
import com.study_site.java_project.web.service.AlarmService;
import com.study_site.java_project.web.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class StudyManagementController {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final ParticipateRoomRepository participateRoomRepository;
    private final AlertService alertService;
    private final AlarmService alarmService;

    @GetMapping("/kick_out/member/{username}/{roomId}")
    @Transactional
    public String kickOutMember(@PathVariable("username") String username, @PathVariable("roomId") String roomId,
                                HttpSession session, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {
        String loginId = (String) session.getAttribute("loginId");
        Member findMemberByLoginId = memberRepository.findByUsername(loginId);
        Member findMemberByUsername = memberRepository.findByUsername(username);

        if (loginId != null) {
            if (findMemberByUsername.getUsername().equals(findMemberByLoginId.getUsername())) {
                alertService.alertAndRedirect(response, "본인은 추방할 수 없습니다.", "/submit_study/" + roomId);
                return "room/submit_study";
            }
        }

        StudyRoom findStudyRoom = studyRoomRepository.findByRoomId(roomId);
        ParticipateRoom findParticipate = participateRoomRepository.findByRoomRoomIdAndMemberUsername(roomId, findMemberByUsername.getUsername());

        findParticipate.participateStatusEnterToExpulsion();

        alarmService.makeAlarm(findMemberByUsername, null, findStudyRoom, findStudyRoom.getRoomName() + "에서 추방되었습니다.");

        alertService.alertAndRedirect(response, findMemberByUsername.getUsername() + "을/를 추방했습니다.", "/submit_study/" + roomId);

        return "room/submit_study";
    }

    @GetMapping("/kick_out/user/{email}/{roomId}")
    @Transactional
    public String kickOutUser(@PathVariable("email") String email, @PathVariable("roomId") String roomId,
                              HttpSession session, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {
        String userEmail = (String) session.getAttribute("userEmail");
        User findUserByUserEmail = userRepository.findByEmail(userEmail).orElse(null);
        User findUserByEmail = userRepository.findByEmail(email).orElse(null);

        if (userEmail != null) {
            if (findUserByUserEmail.getEmail().equals(findUserByEmail.getEmail())) {
                alertService.alertAndRedirect(response, "본인은 추방할 수 없습니다.", "/submit_study/" + roomId);
                return "room/submit_study";
            }
        }

        StudyRoom findStudyRoom = studyRoomRepository.findByRoomId(roomId);
        ParticipateRoom findParticipate = participateRoomRepository.findByRoomRoomIdAndUserEmail(roomId, findUserByEmail.getEmail());

        findParticipate.participateStatusEnterToExpulsion();

        alarmService.makeAlarm(null, findUserByEmail, findStudyRoom, findStudyRoom.getRoomName() + "에서 추방되었습니다.");

        alertService.alertAndRedirect(response, findUserByEmail.getName() + "을/를 추방했습니다.", "/submit_study/" + roomId);

        return "room/submit_study";
    }
}
