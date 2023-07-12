package com.study_site.java_project.web.controller.home;

import com.study_site.java_project.web.entity.Alarm;
import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.User;
import com.study_site.java_project.web.repository.AlarmRepository;
import com.study_site.java_project.web.repository.MemberRepository;
import com.study_site.java_project.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");

        List<Alarm> findAlarms = new ArrayList<>();

        if (loginId != null && userEmail == null) {
            findAlarms = alarmRepository.findByMemberUsernameAndCreateDateAfter(loginId, LocalDateTime.now().minusDays(7));
        }

        if (loginId == null && userEmail != null) {
            findAlarms = alarmRepository.findByUserEmailAndCreateDateAfter(userEmail, LocalDateTime.now().minusDays(7));
        }

        model.addAttribute("alarm", findAlarms.size());
        model.addAttribute("alarmContent", findAlarms);

        return "home/home";
    }
}
