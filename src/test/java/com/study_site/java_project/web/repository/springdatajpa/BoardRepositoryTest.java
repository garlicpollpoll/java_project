package com.study_site.java_project.web.repository.springdatajpa;

import com.study_site.java_project.web.entity.Board;
import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.enums.MemberRole;
import com.study_site.java_project.web.enums.RoomStatus;
import com.study_site.java_project.web.repository.BoardRepository;
import com.study_site.java_project.web.repository.MemberRepository;
import com.study_site.java_project.web.repository.StudyRoomRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StudyRoomRepository studyRoomRepository;

    String roomId = UUID.randomUUID().toString();

    @BeforeEach
    public void setting() {
        Member member = new Member("username", "password", MemberRole.ROLE_USER);
        StudyRoom room = new StudyRoom(roomId, "roomName", 3, 3, RoomStatus.OPEN, "content");

        memberRepository.save(member);
        studyRoomRepository.save(room);

        for (int i = 0; i < 20; i++) {
            Board board = new Board(room, member, null, "title" + i, "2022-09-27", "content" + i);
            boardRepository.save(board);
        }
    }

    @Test
    @DisplayName("BoardRepository Pageable Test")
    public void findByRoomIdPageableTest() throws Exception {
        PageRequest page = PageRequest.of(0, 10);
        List<Board> findBoards = boardRepository.findByRoomRoomId(roomId, page);

        assertThat(findBoards.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("BoardRepository Size Test")
    public void findByRoomIdSizeTest() throws Exception {
        List<Board> findBoards = boardRepository.findByRoomRoomId(roomId);
        assertThat(findBoards.size()).isEqualTo(20);
    }
}
