package com.study_site.java_project.web.repository;

import com.study_site.java_project.web.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = {"member", "user"})
    List<Board> findByRoomRoomId(String roomId, Pageable pageable);

    List<Board> findByRoomRoomId(String roomId);

    @Override
    @EntityGraph(attributePaths = "room")
    Optional<Board> findById(Long aLong);
}
