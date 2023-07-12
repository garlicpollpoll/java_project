package com.study_site.java_project.web.repository.impl;

import com.study_site.java_project.web.entity.CheckToDoList;
import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.entity.User;
import com.study_site.java_project.web.enums.MemberRole;
import com.study_site.java_project.web.enums.Role;
import com.study_site.java_project.web.enums.RoomStatus;
import com.study_site.java_project.web.repository.CheckToDoListRepository;
import com.study_site.java_project.web.repository.MemberRepository;
import com.study_site.java_project.web.repository.StudyRoomRepository;
import com.study_site.java_project.web.repository.UserRepository;
import com.study_site.java_project.web.service.CheckToDoListService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CheckToDoListRepositoryImplTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CheckToDoListRepository checkToDoListRepository;
    @Autowired
    CheckToDoListService checkToDoListService;
    @Autowired
    StudyRoomRepository studyRoomRepository;

    @Test
    public void toDoListFindByRoomId() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member1 = new Member("username1", "password", MemberRole.ROLE_USER);
        Member member2 = new Member("username2", "password", MemberRole.ROLE_USER);
        Member member3 = new Member("username3", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        CheckToDoList toDoList1 = new CheckToDoList(room, member1, null, null, null, null, null, null, null, null);
        CheckToDoList toDoList2 = new CheckToDoList(room, member2, null, null, null, null, null, null, null, null);
        CheckToDoList toDoList3 = new CheckToDoList(room, member3, null, null, null, null, null, null, null, null);
        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        studyRoomRepository.save(room);
        checkToDoListRepository.save(toDoList1);
        checkToDoListRepository.save(toDoList2);
        checkToDoListRepository.save(toDoList3);

        List<CheckToDoList> checkListByRoomId = checkToDoListRepository.findByRoomRoomId(roomId);
        //then
        assertThat(checkListByRoomId.size()).isEqualTo(3);
        for (CheckToDoList toDoList : checkListByRoomId) {
            String username = toDoList.getMember().getUsername();
            System.out.println("username = " + username);
        }
    }

    @Test
    @DisplayName("username으로 체크리스트 찾기")
    public void findCheckListByUsername() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member = new Member("username", "password", MemberRole.ROLE_USER);
        StudyRoom room1 = new StudyRoom(roomId, "roomName1", 3, 3, RoomStatus.OPEN, "content");
        StudyRoom room2 = new StudyRoom(roomId, "roomName2", 3, 3, RoomStatus.OPEN, "content");
        StudyRoom room3 = new StudyRoom(roomId, "roomName3", 3, 3, RoomStatus.OPEN, "content");
        CheckToDoList check1 = new CheckToDoList(room1, member, null, null, null, null, null, null, null, null);
        CheckToDoList check2 = new CheckToDoList(room2, member, null, null, null, null, null, null, null, null);
        CheckToDoList check3 = new CheckToDoList(room3, member, null, null, null, null, null, null, null, null);
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room1);
        studyRoomRepository.save(room2);
        studyRoomRepository.save(room3);
        checkToDoListRepository.save(check1);
        checkToDoListRepository.save(check2);
        checkToDoListRepository.save(check3);

        List<CheckToDoList> findCheckByUsername = checkToDoListRepository.findByUsername(member.getUsername());
        //then
        assertThat(findCheckByUsername.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("email로 체크리스트 찾기")
    public void findCheckListByEmail() throws Exception {
        String roomId = UUID.randomUUID().toString();
        User user = new User("name", "email", "picture", Role.USER);
        StudyRoom room1 = new StudyRoom(roomId, "roomName1", 3, 3, RoomStatus.OPEN, "content");
        StudyRoom room2 = new StudyRoom(roomId, "roomName2", 3, 3, RoomStatus.OPEN, "content");
        StudyRoom room3 = new StudyRoom(roomId, "roomName3", 3, 3, RoomStatus.OPEN, "content");
        CheckToDoList check1 = new CheckToDoList(room1, null, user, null, null, null, null, null, null, null);
        CheckToDoList check2 = new CheckToDoList(room2, null, user, null, null, null, null, null, null, null);
        CheckToDoList check3 = new CheckToDoList(room3, null, user, null, null, null, null, null, null, null);
        //when
        userRepository.save(user);
        studyRoomRepository.save(room1);
        studyRoomRepository.save(room2);
        studyRoomRepository.save(room3);
        checkToDoListRepository.save(check1);
        checkToDoListRepository.save(check2);
        checkToDoListRepository.save(check3);

        List<CheckToDoList> findCheckByUsername = checkToDoListRepository.findByEmail(user.getEmail());
        //then
        assertThat(findCheckByUsername.size()).isEqualTo(3);
    }
}