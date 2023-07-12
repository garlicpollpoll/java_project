package com.study_site.java_project.web.repository;

import com.study_site.java_project.web.entity.ParticipateRoom;
import com.study_site.java_project.web.enums.ParticipateStatus;
import com.study_site.java_project.web.repository.custom.ParticipateCustomRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipateRoomRepository extends JpaRepository<ParticipateRoom, Long>, ParticipateCustomRepository {

    @EntityGraph(attributePaths = {"room", "member", "user"})
    List<ParticipateRoom> findByRoomRoomIdAndParticipateStatus(String roomId, ParticipateStatus status);

    @EntityGraph(attributePaths = {"room", "member", "user"})
    ParticipateRoom findByRoomRoomIdAndMemberUsername(String roomId, String username);

    @EntityGraph(attributePaths = {"room", "member", "user"})
    ParticipateRoom findByRoomRoomIdAndUserEmail(String roomId, String email);
}
