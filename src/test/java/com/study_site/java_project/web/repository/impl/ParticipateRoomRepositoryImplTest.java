package com.study_site.java_project.web.repository.impl;

import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.ParticipateRoom;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.entity.User;
import com.study_site.java_project.web.enums.*;
import com.study_site.java_project.web.repository.MemberRepository;
import com.study_site.java_project.web.repository.ParticipateRoomRepository;
import com.study_site.java_project.web.repository.StudyRoomRepository;
import com.study_site.java_project.web.repository.UserRepository;
import com.study_site.java_project.web.service.ParticipateRoomService;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParticipateRoomRepositoryImplTest {

    @Autowired
    ParticipateRoomRepository participateRoomRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudyRoomRepository studyRoomRepository;
    @Autowired
    ParticipateRoomService participateRoomService;

    /**
     * ParticipateRoomRepositoryImpl 에 있는 QueryDSL findByRoomId 쿼리 잘 나가나 테스트
     */
    @Test
    @DisplayName("QueryDSL findByRoomId check")
    public void checkFindByRoomId() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member = new Member("username", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "TOEIC study", 3, 3, RoomStatus.OPEN, "잘해봅시다.");
        ParticipateRoom participateRoom1 = new ParticipateRoom(
                room, member, null, 0, MemberStatus.OWNER, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null
        );
        ParticipateRoom participateRoom2 = new ParticipateRoom(
                room, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null
        );
        ParticipateRoom participateRoom3 = new ParticipateRoom(
                room, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null
        );
        ParticipateRoom participateRoom4 = new ParticipateRoom(
                room, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null
        );
        ParticipateRoom participateRoom5 = new ParticipateRoom(
                room, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null
        );
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room);
        participateRoomRepository.save(participateRoom1);
        participateRoomRepository.save(participateRoom2);
        participateRoomRepository.save(participateRoom3);
        participateRoomRepository.save(participateRoom4);
        participateRoomRepository.save(participateRoom5);

        List<ParticipateRoom> findByRoomId = participateRoomRepository.findByRoomId(roomId);
        //then
        assertThat(findByRoomId.get(0).getRoom().getRoomName()).isEqualTo("TOEIC study");
        assertThat(findByRoomId.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("findByUsername test")
    public void findByUsername() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member = new Member("username123", "password", MemberRole.ROLE_USER);
        StudyRoom room1 = new StudyRoom(roomId, "roomName1", 3, 3, RoomStatus.OPEN, "content");
        StudyRoom room2 = new StudyRoom(roomId, "roomName2", 3, 3, RoomStatus.OPEN, "content");
        StudyRoom room3 = new StudyRoom(roomId, "roomName3", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom1 = new ParticipateRoom(room1, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        ParticipateRoom pRoom2 = new ParticipateRoom(room2, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        ParticipateRoom pRoom3 = new ParticipateRoom(room3, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room1);
        studyRoomRepository.save(room2);
        studyRoomRepository.save(room3);
        participateRoomRepository.save(pRoom1);
        participateRoomRepository.save(pRoom2);
        participateRoomRepository.save(pRoom3);

        List<ParticipateRoom> findByUsername = participateRoomService.findByUsername(member.getUsername());
        //then
        assertThat(findByUsername.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("findByEmail test")
    public void findByEmail() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        User user = new User("name", "email", "picture", Role.GUEST);
        StudyRoom room1 = new StudyRoom(roomId, "roomName1", 3, 3, RoomStatus.OPEN, "content");
        StudyRoom room2 = new StudyRoom(roomId, "roomName2", 3, 3, RoomStatus.OPEN, "content");
        StudyRoom room3 = new StudyRoom(roomId, "roomName3", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom1 = new ParticipateRoom(room1, null, user, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        ParticipateRoom pRoom2 = new ParticipateRoom(room2, null, user, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        ParticipateRoom pRoom3 = new ParticipateRoom(room3, null, user, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        userRepository.save(user);
        studyRoomRepository.save(room1);
        studyRoomRepository.save(room2);
        studyRoomRepository.save(room3);
        participateRoomRepository.save(pRoom1);
        participateRoomRepository.save(pRoom2);
        participateRoomRepository.save(pRoom3);

        List<ParticipateRoom> findByEmail = participateRoomService.findByEmail(user.getEmail());
        //then
        assertThat(findByEmail.size()).isEqualTo(3);
    }


    @Test
    @DisplayName("findByRoomIdAndUpdateDate")
    public void findByRoomIdAndUpdateDate() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member = new Member("username0", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom = new ParticipateRoom(room, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now().minusDays(3), null, null, null, null, null, null, null);
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom);

        List<ParticipateRoom> findParticipate = participateRoomRepository.findByRoomIdAndUpdateDate(roomId, LocalDateTime.now().minusDays(2));
        //then
        assertThat(findParticipate.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("findByRoomIdAndDeadline")
    public void findByRoomIdAndDeadline() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member = new Member("username6", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom = new ParticipateRoom(room, member, null, 3, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom);

        List<ParticipateRoom> findParticipate = participateRoomRepository.findByRoomIdAndDeadline(room.getRoomId(), room.getDeadline());
        //then
        assertThat(findParticipate.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("내가 속한 스터디를 볼 때 CLOSE 된 스터디나 EXPULSION 된 스터디는 보이지 않게 하기 위한 쿼리 테스트 (Member)")
    public void findByUsernameAndStatus() throws Exception {
        //given
        String roomId1 = UUID.randomUUID().toString();
        String roomId2 = UUID.randomUUID().toString();
        String roomId3 = UUID.randomUUID().toString();
        Member member = new Member("username88", "password", MemberRole.ROLE_USER);
        StudyRoom room1 = new StudyRoom(roomId1, "roomName1", 3, 3, RoomStatus.OPEN, "content");
        StudyRoom room2 = new StudyRoom(roomId2, "roomName2", 3, 3, RoomStatus.CLOSE, "content");
        StudyRoom room3 = new StudyRoom(roomId3, "roomName3", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom1 = new ParticipateRoom(room1, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        ParticipateRoom pRoom2 = new ParticipateRoom(room2, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        ParticipateRoom pRoom3 = new ParticipateRoom(room3, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.EXPULSION, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room1);
        studyRoomRepository.save(room2);
        studyRoomRepository.save(room3);
        participateRoomRepository.save(pRoom1);
        participateRoomRepository.save(pRoom2);
        participateRoomRepository.save(pRoom3);

        List<ParticipateRoom> findParticipate = participateRoomRepository.findByUsernameAndStatus(member.getUsername(), RoomStatus.OPEN, ParticipateStatus.ENTER);

        //then
        assertThat(findParticipate.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("내가 속한 스터디를 볼 때 CLOSE 된 스터디나 EXPULSION 된 스터디는 보이지 않게 하기 위한 쿼리 테스트 (User)")
    public void findByEmailAndStatus() throws Exception {
        //given
        String roomId1 = UUID.randomUUID().toString();
        String roomId2 = UUID.randomUUID().toString();
        String roomId3 = UUID.randomUUID().toString();
        User user = new User("name", "email", "picture", Role.USER);
        StudyRoom room1 = new StudyRoom(roomId1, "roomName1", 3, 3, RoomStatus.OPEN, "content");
        StudyRoom room2 = new StudyRoom(roomId2, "roomName2", 3, 3, RoomStatus.CLOSE, "content");
        StudyRoom room3 = new StudyRoom(roomId3, "roomName3", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom1 = new ParticipateRoom(room1, null, user, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        ParticipateRoom pRoom2 = new ParticipateRoom(room2, null, user, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        ParticipateRoom pRoom3 = new ParticipateRoom(room3, null, user, 0, MemberStatus.NORMAL, ParticipateStatus.EXPULSION, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        userRepository.save(user);
        studyRoomRepository.save(room1);
        studyRoomRepository.save(room2);
        studyRoomRepository.save(room3);
        participateRoomRepository.save(pRoom1);
        participateRoomRepository.save(pRoom2);
        participateRoomRepository.save(pRoom3);

        List<ParticipateRoom> findParticipate = participateRoomRepository.findByEmailAndStatus(user.getEmail(), RoomStatus.OPEN, ParticipateStatus.ENTER);

        //then
        assertThat(findParticipate.size()).isEqualTo(1);
    }
}