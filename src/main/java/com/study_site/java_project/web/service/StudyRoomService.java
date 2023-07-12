package com.study_site.java_project.web.service;

import com.study_site.java_project.web.dto.study.MakeStudyRoomDto;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.enums.RoomStatus;
import com.study_site.java_project.web.repository.StudyRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudyRoomService {

    private final StudyRoomRepository studyRoomRepository;

    @Transactional
    public StudyRoom save(MakeStudyRoomDto dto) {
        String roomId = UUID.randomUUID().toString();
        StudyRoom room = new StudyRoom(roomId, dto.getRoomName(), dto.getDeadline(), dto.getWarning(), RoomStatus.OPEN, dto.getContent());

        studyRoomRepository.save(room);

        return room;
    }

    public boolean isCheckEnterStudy(String roomId) {
        StudyRoom findStudyRoom = studyRoomRepository.findByRoomId(roomId);

        if (findStudyRoom.getStatus() == RoomStatus.CLOSE) {
            return false;
        }

        return true;
    }
}
