package com.study_site.java_project.web.controller.member;

import com.study_site.java_project.web.dto.join.JoinDto;
import com.study_site.java_project.web.dto.member.DuplicateIdDto;
import com.study_site.java_project.web.repository.MemberRepository;
import com.study_site.java_project.web.repository.UserRepository;
import com.study_site.java_project.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "home/login";
    }

    @GetMapping("/join")
    public String join(Model model) {
        JoinDto dto = new JoinDto();
        model.addAttribute("join", dto);
        return "home/join";
    }

    @PostMapping("/join")
    public String joinPost(@Validated @ModelAttribute("join") JoinDto dto, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "home/join";
        }

        String checkId = (String) session.getAttribute("checkId");

        if (checkId != null) {
            if (checkId.equals("true")) {
                memberService.save(dto);
            }
            else {
                bindingResult.reject("duplicate");
                return "home/join";
            }
        }
        else {
            bindingResult.reject("duplicate_check");
            return "home/join";
        }

        return "redirect:/login";
    }

    @PostMapping("/duplicate_id")
    @ResponseBody
    public Map<String, String> duplicate(@RequestBody DuplicateIdDto dto, HttpSession session) {
        boolean isDuplicate = memberService.duplicateUsername(dto.getCheckId());
        Map<String, String> message = new HashMap<>();

        if (isDuplicate) {
            message.put("message", "사용 가능한 아이디입니다.");
            session.setAttribute("checkId", "true");
        }
        else {
            message.put("message", "사용 불가능한 아이디입니다.");
            session.setAttribute("checkId", "false");
        }

        return message;
    }
}
