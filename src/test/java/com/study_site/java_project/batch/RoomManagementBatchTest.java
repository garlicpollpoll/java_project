package com.study_site.java_project.batch;

import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.ParticipateRoom;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.enums.MemberRole;
import com.study_site.java_project.web.enums.MemberStatus;
import com.study_site.java_project.web.enums.ParticipateStatus;
import com.study_site.java_project.web.enums.RoomStatus;
import com.study_site.java_project.web.repository.MemberRepository;
import com.study_site.java_project.web.repository.ParticipateRoomRepository;
import com.study_site.java_project.web.repository.StudyRoomRepository;
import com.study_site.java_project.web.service.batch.RoomManagementService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class RoomManagementBatchTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StudyRoomRepository studyRoomRepository;
    @Autowired
    ParticipateRoomRepository participateRoomRepository;
    @Autowired
    RoomManagementService roomManagementService;
    @Autowired
    EntityManagerFactory emf;

    private static final String roomId = UUID.randomUUID().toString();

    @Test
    @DisplayName("제한시간 안에 못내면 경고가 1회 증가하는지에 대한 테스트")
    public void addWarningTest() throws Exception {
        //given
        Member member = new Member("username", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 1, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom = new ParticipateRoom(room, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now().minusDays(2), null, null, null, null, null, null, null);
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom);

        roomManagementService.roomManagement();

        List<ParticipateRoom> findParticipate = participateRoomRepository.findByUsername(member.getUsername());

        ParticipateRoom participateRoom = findParticipate.get(0);
        //then
        assertThat(participateRoom.getWarning()).isEqualTo(1);
    }

    @Test
    @DisplayName("최대 경고 횟수에 도달하면 강퇴가 되어야 한다.")
    public void expulsionTest() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member = new Member("username3", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 1, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom = new ParticipateRoom(room, member, null, 2, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now().minusDays(2), null, null, null, null, null, null, null);
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom);

        roomManagementService.roomManagement();

        ParticipateRoom findParticipate = participateRoomRepository.findByUsername(member.getUsername()).get(0);
        //then
        assertThat(findParticipate.getParticipateStatus()).isEqualTo(ParticipateStatus.EXPULSION);
    }

    @Test
    @DisplayName("방에 있는 모든 회원이 EXPULSION이면 방이 CLOSE 된다.")
    public void studyRoomCloseTest() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member1 = new Member("username4", "password", MemberRole.ROLE_USER);
        Member member2 = new Member("username4", "password", MemberRole.ROLE_USER);
        Member member3 = new Member("username4", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 1, 1, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom1 = new ParticipateRoom(room, member1, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now().minusDays(2), null, null, null, null, null, null, null);
        ParticipateRoom pRoom2 = new ParticipateRoom(room, member2, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now().minusDays(2), null, null, null, null, null, null, null);
        ParticipateRoom pRoom3 = new ParticipateRoom(room, member3, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now().minusDays(2), null, null, null, null, null, null, null);
        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom1);
        participateRoomRepository.save(pRoom2);
        participateRoomRepository.save(pRoom3);

        roomManagementService.roomManagement();

        StudyRoom findStudyRoom = studyRoomRepository.findByRoomId(roomId);
        //then
        assertThat(findStudyRoom.getStatus()).isEqualTo(RoomStatus.CLOSE);
    }
}
