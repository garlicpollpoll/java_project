package com.study_site.java_project.web.repository;

import com.study_site.java_project.web.entity.StudyRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {

    StudyRoom findByRoomId(String roomId);
}
