package com.study_site.java_project.web.service;

import com.study_site.java_project.web.entity.*;
import com.study_site.java_project.web.enums.MemberStatus;
import com.study_site.java_project.web.enums.ParticipateStatus;
import com.study_site.java_project.web.enums.RoomStatus;
import com.study_site.java_project.web.repository.ParticipateRoomRepository;
import com.study_site.java_project.web.repository.StudyRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipateRoomService {

    private final ParticipateRoomRepository participateRoomRepository;

    @Transactional
    public void save(StudyRoom room, Member member, User user) {
        ParticipateRoom pRoom = new ParticipateRoom(room, member, user, 0, MemberStatus.OWNER, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);

        participateRoomRepository.save(pRoom);
    }

    @Transactional
    public void enter(StudyRoom room, Member member, User user) {
        ParticipateRoom pRoom = new ParticipateRoom(room, member, user, 0, MemberStatus.NORMAL, ParticipateStatus.ENTER, LocalDateTime.now(), null, null, null, null, null, null, null);

        participateRoomRepository.save(pRoom);
    }

    @Transactional
    public List<ParticipateRoom> findByRoomId(String roomId) {
        return participateRoomRepository.findByRoomId(roomId);
    }

    public String isCheckEnteredMember(String roomId, Member member) {
        List<ParticipateRoom> findParticipateRoom = participateRoomRepository.findByRoomId(roomId);

        for (ParticipateRoom participateRoom : findParticipateRoom) {
            if (participateRoom.getMember() != null && participateRoom.getUser() != null) {
                if (participateRoom.getParticipateStatus() == ParticipateStatus.EXPULSION) {
                    return "강퇴당한 스터디에는 참여할 수 없습니다.";
                }
                else if (participateRoom.getMember().getUsername().equals(member.getUsername())) {
                    return "이미 참가되어있는 스터디입니다.";
                }
            }
        }

        return "환영합니다.";
    }

    public String isCheckEnterUser(String roomId, User user) {
        List<ParticipateRoom> findParticipateRoom = participateRoomRepository.findByRoomId(roomId);

        for (ParticipateRoom participateRoom : findParticipateRoom) {
            if (participateRoom.getParticipateStatus() == ParticipateStatus.EXPULSION) {
                return "강퇴당한 스터디에는 참여할 수 없습니다.";
            }
            else if (participateRoom.getUser().getEmail().equals(user.getEmail())) {
                return "이미 참가되어있는 스터디입니다.";
            }
        }

        return "환영합니다.";
    }

    @Transactional
    public List<ParticipateRoom> findByUsername(String username) {
        return participateRoomRepository.findByUsername(username);
    }

    @Transactional
    public List<ParticipateRoom> findByEmail(String email) {
        return participateRoomRepository.findByEmail(email);
    }

    @Transactional
    public void checkToday(String day, ParticipateRoom participateRoom) {
        if (day.equals("MONDAY")) {
            participateRoom.setMonday(day);
        } else if (day.equals("TUESDAY")) {
            participateRoom.setTuesday(day);
        } else if (day.equals("WEDNESDAY")) {
            participateRoom.setWednesday(day);
        } else if (day.equals("THURSDAY")) {
            participateRoom.setThursday(day);
        } else if (day.equals("FRIDAY")) {
            participateRoom.setFriday(day);
        } else if (day.equals("SATURDAY")) {
            participateRoom.setSaturday(day);
        } else if (day.equals("SUNDAY")) {
            participateRoom.setSunday(day);
        }
    }
}
