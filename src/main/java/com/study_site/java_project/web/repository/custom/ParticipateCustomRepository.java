package com.study_site.java_project.web.repository.custom;

import com.study_site.java_project.web.entity.ParticipateRoom;
import com.study_site.java_project.web.enums.ParticipateStatus;
import com.study_site.java_project.web.enums.RoomStatus;
import com.study_site.java_project.web.repository.StudyRoomRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ParticipateCustomRepository {

    List<ParticipateRoom> findByRoomId(String roomId);

    List<ParticipateRoom> findByUsername(String username);

    List<ParticipateRoom> findByEmail(String email);

    List<ParticipateRoom> findByRoomIdAndUpdateDate(String roomId, LocalDateTime updateDate);

    List<ParticipateRoom> findByRoomIdAndDeadline(String roomId, long deadline);

    List<ParticipateRoom> findByUsernameAndStatus(String username, RoomStatus status, ParticipateStatus pStatus);

    List<ParticipateRoom> findByEmailAndStatus(String email, RoomStatus status, ParticipateStatus pStatus);
}
