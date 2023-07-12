package com.study_site.java_project.web.service;

import com.study_site.java_project.web.dto.join.JoinDto;
import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.enums.MemberRole;
import com.study_site.java_project.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    public void save(JoinDto dto) {
        String encode = encoder.encode(dto.getLoginPw());
        Member member = new Member(dto.getLoginId(), encode, MemberRole.ROLE_USER);

        memberRepository.save(member);
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public boolean duplicateUsername(String username) {
        Member findMember = memberRepository.findByUsername(username);

        if (findMember == null) {
            return true;
        }
        else {
            return false;
        }
    }
}
