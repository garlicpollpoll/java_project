package com.study_site.java_project.web.service;

import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.enums.MemberRole;
import com.study_site.java_project.web.enums.RoomStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.UUID;

@SpringBootTest
@Transactional
public class StudyRoomServiceTest {


    private static final String roomId = UUID.randomUUID().toString();

    @Test
    @DisplayName("모든 회원이 강퇴당해 스터디가 닫히는 테스트 (Member)")
    public void checkMemberWithCloseStudy() throws Exception {
        //given
        Member member = new Member("username", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.CLOSE, "content");
        //when

        //then
    }
}
