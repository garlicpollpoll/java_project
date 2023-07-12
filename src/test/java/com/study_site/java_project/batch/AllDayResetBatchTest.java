package com.study_site.java_project.batch;

import com.study_site.java_project.config.batch.batch.DayResetBatch;
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
import com.study_site.java_project.web.service.batch.AllDayResetService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class AllDayResetBatchTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StudyRoomRepository studyRoomRepository;
    @Autowired
    ParticipateRoomRepository participateRoomRepository;
    @Autowired
    DayResetBatch config;

    // Job 관련
    @Autowired
    AllDayResetService allDayResetService;

    @Test
    @DisplayName("일주일이 지나면 월화수목금토일 의 제출 내역이 null 이 되어야 한다.")
    public void allDayRestBatchTest() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member1 = new Member("username1", "password", MemberRole.ROLE_USER);
        Member member2 = new Member("username2", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        ParticipateRoom pRoom1 = new ParticipateRoom(room, member1, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY");
        ParticipateRoom pRoom2 = new ParticipateRoom(room, member2, null, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), "MONDAY", "TUESDAY", null, null, null, null, null);
        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        studyRoomRepository.save(room);
        participateRoomRepository.save(pRoom1);
        participateRoomRepository.save(pRoom2);

        allDayResetService.allDayReset();

        //then
        List<ParticipateRoom> findAll = participateRoomRepository.findAll();
        for (ParticipateRoom participateRoom : findAll) {
            Assertions.assertThat(participateRoom.getMonday()).isEqualTo(null);
            Assertions.assertThat(participateRoom.getTuesday()).isEqualTo(null);
            Assertions.assertThat(participateRoom.getWednesday()).isEqualTo(null);
            Assertions.assertThat(participateRoom.getThursday()).isEqualTo(null);
            Assertions.assertThat(participateRoom.getFriday()).isEqualTo(null);
            Assertions.assertThat(participateRoom.getSaturday()).isEqualTo(null);
            Assertions.assertThat(participateRoom.getSunday()).isEqualTo(null);
        }
    }
}
