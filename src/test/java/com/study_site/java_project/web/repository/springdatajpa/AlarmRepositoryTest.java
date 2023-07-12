package com.study_site.java_project.web.repository.springdatajpa;

import com.study_site.java_project.web.entity.Alarm;
import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.entity.User;
import com.study_site.java_project.web.enums.MemberRole;
import com.study_site.java_project.web.enums.Role;
import com.study_site.java_project.web.enums.RoomStatus;
import com.study_site.java_project.web.repository.AlarmRepository;
import com.study_site.java_project.web.repository.MemberRepository;
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
public class AlarmRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AlarmRepository alarmRepository;
    @Autowired
    StudyRoomRepository studyRoomRepository;

    @Test
    @DisplayName("7일이 지난 알람은 보이지 않게 하는 테스트 (Member)")
    public void findByMemberUsernameAndCreateDateAfter() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member = new Member("username", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        Alarm alarm1 = new Alarm(member, null, room, LocalDateTime.now().minusDays(8), "message");
        Alarm alarm2 = new Alarm(member, null, room, LocalDateTime.now().minusDays(3), "message1");
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room);
        alarmRepository.save(alarm1);
        alarmRepository.save(alarm2);

        List<Alarm> findAlarm = alarmRepository.findByMemberUsernameAndCreateDateAfter(member.getUsername(), LocalDateTime.now().minusDays(7));
        //then
        assertThat(findAlarm.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("7일이 지난 알람은 보이지 않게 하는 테스트 (User)")
    public void findByUserEmailAndCreateDateAfter() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        User user = new User("name", "email", "picture", Role.USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        Alarm alarm1 = new Alarm(null, user, room, LocalDateTime.now().minusDays(8), "message");
        Alarm alarm2 = new Alarm(null, user, room, LocalDateTime.now().minusDays(3), "message1");
        //when
        userRepository.save(user);
        studyRoomRepository.save(room);
        alarmRepository.save(alarm1);
        alarmRepository.save(alarm2);

        List<Alarm> findAlarm = alarmRepository.findByUserEmailAndCreateDateAfter(user.getEmail(), LocalDateTime.now().minusDays(7));
        //then
        assertThat(findAlarm.size()).isEqualTo(1);
    }
}
