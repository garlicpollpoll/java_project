package com.study_site.java_project.web.controller.board;

import com.study_site.java_project.s3.S3Uploader;
import com.study_site.java_project.web.dto.board.BoardWriteDto;
import com.study_site.java_project.web.dto.board.CommentDto;
import com.study_site.java_project.web.entity.*;
import com.study_site.java_project.web.repository.*;
import com.study_site.java_project.web.service.BoardService;
import com.study_site.java_project.web.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final S3Uploader s3Uploader;

    @GetMapping("/board_write/{roomId}")
    public String boardWrite(@PathVariable("roomId") String roomId, Model model) {

        model.addAttribute("roomId", roomId);

        return "board/board_write";
    }

    @GetMapping("/board_detail/{boardId}")
    public String boardDetail(@PathVariable("boardId") Long boardId, Model model) {
        Board findBoard = boardRepository.findById(boardId).orElse(null);

        List<Comment> findComments = commentRepository.findByBoardId(boardId);

        model.addAttribute("board", findBoard);
        model.addAttribute("comment", findComments);

        return "board/board_detail";
    }

    @PostMapping("/board_write/{roomId}")
    public String boardWritePost(@PathVariable("roomId") String roomId, @ModelAttribute("write")BoardWriteDto dto, HttpSession session, RedirectAttributes redirectAttributes) {
        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");

        Member findMember = null;
        User findUser = null;

        if (loginId != null && userEmail == null) {
            findMember = memberRepository.findByUsername(loginId);
        }

        if (loginId == null && userEmail != null) {
            findUser = userRepository.findByEmail(userEmail).orElse(null);
        }

        StudyRoom findStudy = studyRoomRepository.findByRoomId(roomId);

        Board saveBoard = boardService.save(dto.getContent(), findMember, findUser, dto.getTitle(), findStudy);

        redirectAttributes.addAttribute("boardId", saveBoard.getId());

        return "redirect:/board_detail/{boardId}";
    }

    @PostMapping("/comment_write/{boardId}")
    public String commentWritePost(@PathVariable("boardId") Long boardId, @ModelAttribute("comment") CommentDto dto,
                                   HttpSession session, RedirectAttributes redirectAttributes) {
        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");

        Member findMember = null;
        User findUser = null;

        if (loginId != null && userEmail == null) {
            findMember = memberRepository.findByUsername(loginId);
        }

        if (loginId == null && userEmail != null) {
            findUser = userRepository.findByEmail(userEmail).orElse(null);
        }

        Board findBoard = boardRepository.findById(boardId).orElse(null);

        commentService.save(findMember, findUser, findBoard, dto.getComment());

        redirectAttributes.addAttribute("boardId", findBoard.getId());

        return "redirect:/board_detail/{boardId}";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("data")MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static");
    }
}


