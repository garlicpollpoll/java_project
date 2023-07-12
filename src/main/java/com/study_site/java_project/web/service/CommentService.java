package com.study_site.java_project.web.service;

import com.study_site.java_project.web.entity.Board;
import com.study_site.java_project.web.entity.Comment;
import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.User;
import com.study_site.java_project.web.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment save(Member member, User user, Board board, String inputComment) {
        Comment comment = new Comment(member, user, board, inputComment, LocalDateTime.now().toString().substring(0, 10));

        return commentRepository.save(comment);
    }

}
