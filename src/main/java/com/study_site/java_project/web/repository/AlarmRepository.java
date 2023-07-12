package com.study_site.java_project.web.repository;

import com.study_site.java_project.web.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByMemberUsernameAndCreateDateAfter(String username, LocalDateTime createDate);

    List<Alarm> findByUserEmailAndCreateDateAfter(String email, LocalDateTime createDate);
}
