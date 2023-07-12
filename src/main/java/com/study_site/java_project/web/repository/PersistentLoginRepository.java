package com.study_site.java_project.web.repository;

import com.study_site.java_project.web.entity.PersistentLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersistentLoginRepository extends JpaRepository<PersistentLogin, Long> {

    Optional<PersistentLogin> findBySeries(final String series);

    List<PersistentLogin> findByUsername(final String username);
}
