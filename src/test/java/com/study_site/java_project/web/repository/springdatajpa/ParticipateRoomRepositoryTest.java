package com.study_site.java_project.web.repository.springdatajpa;

import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.ParticipateRoom;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.entity.User;
import com.study_site.java_project.web.enums.*;
import com.study_site.java_project.web.repository.MemberRepository;
import com.study_site.java_project.web.repository.ParticipateRoomRepository;
import com.study_site.java_project.web.repository.StudyRoomRepository;
import com.study_site.java_project.web.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ParticipateRoomRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StudyRoomRepository studyRoomRepository;
    @Autowired
    ParticipateRoomRepository participateRoomRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("roomId 와 participateStatus 를 가지고 스터디에서 강퇴당한 인원의 수를 구할때 쓰는 쿼리 검증")
    public void findByRoomRoomIdAndParticipateStatus() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member1 = new Member("username1", "password", MemberRole.ROLE_USER);
        Member member2 = new Member("username2", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom1 = new ParticipateRoom(room, member1, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        ParticipateRoom pRoom2 = new ParticipateRoom(room, member2, null, 0, MemberStatus.NORMAL, ParticipateStatus.EXPULSION, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom1);
        participateRoomRepository.save(pRoom2);

        List<ParticipateRoom> findParticipateRoom = participateRoomRepository.findByRoomRoomIdAndParticipateStatus(roomId, ParticipateStatus.ENTER);
        //then
        assertThat(findParticipateRoom.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("roomId 와 username 을 가지고 강퇴당한 인원인지 확인하는 테스트")
    public void findByRoomRoomIdAndMemberUsername() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member = new Member("username", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom = new ParticipateRoom(room, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.EXPULSION, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom);

        ParticipateRoom findParticipate = participateRoomRepository.findByRoomRoomIdAndMemberUsername(roomId, member.getUsername());

        //then
        assertThat(findParticipate.getParticipateStatus()).isEqualTo(ParticipateStatus.EXPULSION);
    }

    @Test
    @DisplayName("roomId 와 email 을 가지고 강퇴당한 인원인지 확인하는 테스트")
    public void findByRoomRoomIdAndUserEmail() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        User user = new User("name", "email", "picture", Role.USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom = new ParticipateRoom(room, null, user, 0, MemberStatus.NORMAL, ParticipateStatus.EXPULSION, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        userRepository.save(user);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom);

        ParticipateRoom findParticipate = participateRoomRepository.findByRoomRoomIdAndUserEmail(roomId, user.getEmail());
        //then
        assertThat(findParticipate.getParticipateStatus()).isEqualTo(ParticipateStatus.EXPULSION);
    }
}
