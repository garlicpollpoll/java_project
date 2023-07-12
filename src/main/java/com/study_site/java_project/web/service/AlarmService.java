package com.study_site.java_project.web.service;

import com.study_site.java_project.web.entity.Alarm;
import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.entity.User;
import com.study_site.java_project.web.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public void makeAlarm(Member member, User user, StudyRoom room, String message) {
        Alarm alarm = new Alarm(member, user, room, LocalDateTime.now(), message);
        alarmRepository.save(alarm);
    }
}
