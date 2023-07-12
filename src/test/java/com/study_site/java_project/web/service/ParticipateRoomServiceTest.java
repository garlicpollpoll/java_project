package com.study_site.java_project.web.service;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParticipateRoomServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ParticipateRoomService participateRoomService;
    @Autowired
    ParticipateRoomRepository participateRoomRepository;
    @Autowired
    StudyRoomRepository studyRoomRepository;


    private static final String roomId = UUID.randomUUID().toString();

    @Test
    @DisplayName("요일테스트 (Member)")
    public void dateTestByMember() throws Exception {
        //given
        String today = LocalDateTime.now().getDayOfWeek().toString();
        String roomId1 = UUID.randomUUID().toString();
        String roomId2 = UUID.randomUUID().toString();
        Member member = new Member("username", "password", MemberRole.ROLE_USER);
        StudyRoom room1 = new StudyRoom(roomId1, "roomName1", 3, 3, RoomStatus.OPEN, "content");
        StudyRoom room2 = new StudyRoom(roomId2, "roomName2", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom1 = new ParticipateRoom(room1, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        ParticipateRoom pRoom2 = new ParticipateRoom(room2, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room1);
        studyRoomRepository.save(room2);
        participateRoomRepository.save(pRoom1);
        participateRoomRepository.save(pRoom2);

        List<ParticipateRoom> findParticipate = participateRoomService.findByUsername(member.getUsername());
        for (ParticipateRoom participateRoom : findParticipate) {
            if (participateRoom.getRoom().getRoomId().equals(roomId1)) {
               participateRoomService.checkToday(today, participateRoom);
                System.out.println("participateRoom = " + participateRoom.getMonday());
            }
        }
        //then
    }

    @Test
    @DisplayName("이미 참여된 Member 인 경우 '이미 참가되어있는 스터디입니다. 를 반환'")
    public void isCheckEnteredMember() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member = new Member("username", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom = new ParticipateRoom(room, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom);

        String checkEnteredMember = participateRoomService.isCheckEnteredMember(roomId, member);
        //then
        assertThat(checkEnteredMember).isEqualTo("이미 참가되어있는 스터디입니다.");
    }

    @Test
    @DisplayName("이미 참여된 User 인 경우 '이미 참가되어있는 스터디입니다. 를 반환'")
    public void isCheckEnteredUser() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        User user = new User("name", "email", "picture", Role.GUEST);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "Content");
        ParticipateRoom pRoom = new ParticipateRoom(room, null, user, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        userRepository.save(user);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom);

        String checkEnterUser = participateRoomService.isCheckEnterUser(roomId, user);
        //then
        assertThat(checkEnterUser).isEqualTo("이미 참가되어있는 스터디입니다.");
    }

    @Test
    @DisplayName("해당 스터디에 참가하고 있지 않아 '환영합니다.' 를 반환 (Member)")
    public void test() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member1 = new Member("username1", "password", MemberRole.ROLE_USER);
        Member member2 = new Member("username2", "password", MemberRole.ROLE_USER);
        Member member3 = new Member("username3", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom1 = new ParticipateRoom(room, member1, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        ParticipateRoom pRoom2 = new ParticipateRoom(room, member2, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom1);
        participateRoomRepository.save(pRoom2);

        String checkEnteredMember = participateRoomService.isCheckEnteredMember(roomId, member3);
        //then
        assertThat(checkEnteredMember).isEqualTo("환영합니다.");
    }

    @Test
    @DisplayName("해당 스터디에 참가하고 있지 않아 '환영합니다.' 를 반환 (User)")
    public void isCheckEnteredUserTrue() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        User user1 = new User("name", "email1", "picture", Role.GUEST);
        User user2 = new User("name", "email2", "picture", Role.GUEST);
        User user3 = new User("name", "email3", "picture", Role.GUEST);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom1 = new ParticipateRoom(room, null, user1, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        ParticipateRoom pRoom2 = new ParticipateRoom(room, null, user2, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom1);
        participateRoomRepository.save(pRoom2);

        String checkEnterUser = participateRoomService.isCheckEnterUser(roomId, user3);
        //then
        assertThat(checkEnterUser).isEqualTo("환영합니다.");
    }

    @Test
    @DisplayName("강퇴된 회원은 방에 입장하려 할 때 '강퇴당한 스터디에는 참여할 수 없습니다.' 를 반환 (Member)")
    public void checkMemberEnterWithExpulsion() throws Exception {
        //given
        Member member = new Member("username", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom = new ParticipateRoom(room, member, null, 0, MemberStatus.NORMAL, ParticipateStatus.EXPULSION, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom);

        String checkEnteredMember = participateRoomService.isCheckEnteredMember(room.getRoomId(), member);
        //then
        assertThat(checkEnteredMember).isEqualTo("강퇴당한 스터디에는 참여할 수 없습니다.");
    }

    @Test
    @DisplayName("강퇴된 회원은 방에 입장하려 할 때 '강퇴당한 스터디에는 참여할 수 없습니다.' 를 반환 (User)")
    public void checkUserEnterWithExpulsion() throws Exception {
        //given
        User user = new User("name", "email", "picture", Role.USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom = new ParticipateRoom(room, null, user, 0, MemberStatus.NORMAL, ParticipateStatus.EXPULSION, LocalDateTime.now(), null, null, null, null, null, null, null);
        //when
        userRepository.save(user);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom);

        String checkEnteredMember = participateRoomService.isCheckEnterUser(room.getRoomId(), user);
        //then
        assertThat(checkEnteredMember).isEqualTo("강퇴당한 스터디에는 참여할 수 없습니다.");
    }
}