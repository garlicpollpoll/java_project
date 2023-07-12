package com.study_site.java_project.web.repository;

import com.study_site.java_project.web.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"member", "user"})
    List<Comment> findByBoardId(Long id);
}
