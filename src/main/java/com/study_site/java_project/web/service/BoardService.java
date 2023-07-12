package com.study_site.java_project.web.service;

import com.study_site.java_project.web.entity.Board;
import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.entity.User;
import com.study_site.java_project.web.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public Board save(String content, Member member, User user, String title, StudyRoom room) {
        Board board = new Board(room, member, user, title, LocalDateTime.now().toString().substring(0, 10), content);

        return boardRepository.save(board);
    }
}
