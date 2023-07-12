package com.study_site.java_project;

import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.enums.MemberRole;
import com.study_site.java_project.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class DBInit {

//    private final InitService initService;
//
//    @PostConstruct
//    public void init() {
//        initService.dbInit();
//    }
//
//    @Service
//    @RequiredArgsConstructor
//    @Transactional
//    static class InitService {
//
//        private final MemberRepository memberRepository;
//
//        public void dbInit() {
//            for (int i = 0; i < 100000; i++) {
//                Member member = new Member("username" + i, "password", MemberRole.ROLE_USER);
//                memberRepository.save(member);
//            }
//        }
//    }
}
