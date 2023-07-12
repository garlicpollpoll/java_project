package com.study_site.java_project.web.service;

import com.study_site.java_project.web.entity.Board;
import com.study_site.java_project.web.entity.Comment;
import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.enums.MemberRole;
import com.study_site.java_project.web.enums.RoomStatus;
import com.study_site.java_project.web.repository.BoardRepository;
import com.study_site.java_project.web.repository.CommentRepository;
import com.study_site.java_project.web.repository.MemberRepository;
import com.study_site.java_project.web.repository.StudyRoomRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    BoardService boardService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StudyRoomRepository studyRoomRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CommentRepository commentRepository;

    @Test
    @DisplayName("게시판이 잘 저장되는지 확인하는 테스트")
    public void boardSaveTest() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member = new Member("username", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        Board board = new Board(room, member, null, "title", "createDate", "content");
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room);
        boardService.save(board.getContent(), member, null, board.getTitle(), room);
        //then
        List<Board> findAll = boardRepository.findAll();
        assertThat(findAll.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글이 잘 작성되는지 확인하는 테스트")
    public void commentSaveTest() throws Exception {
        //given
        String roomId = UUID.randomUUID().toString();
        Member member = new Member("username", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");
        Board board = new Board(room, member, null, "title", "createDate", "content");
        Comment comment = new Comment(member, null, board, "comment", "createDate");
        //when
        memberRepository.save(member);
        studyRoomRepository.save(room);
        boardRepository.save(board);
        commentRepository.save(comment);
        //then
        List<Comment> findAll = commentRepository.findAll();
        assertThat(findAll.size()).isEqualTo(1);
    }
}
