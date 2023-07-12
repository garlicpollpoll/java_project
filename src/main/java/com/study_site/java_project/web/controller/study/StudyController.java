package com.study_site.java_project.web.controller.study;

import com.study_site.java_project.s3.S3Uploader;
import com.study_site.java_project.web.dto.enter.RoomIdDto;
import com.study_site.java_project.web.dto.paging.PagingDto;
import com.study_site.java_project.web.dto.study.MakeStudyRoomDto;
import com.study_site.java_project.web.dto.study.SubmitStudyDto;
import com.study_site.java_project.web.entity.*;
import com.study_site.java_project.web.enums.ParticipateStatus;
import com.study_site.java_project.web.enums.RoomStatus;
import com.study_site.java_project.web.repository.BoardRepository;
import com.study_site.java_project.web.repository.CheckToDoListRepository;
import com.study_site.java_project.web.repository.ParticipateRoomRepository;
import com.study_site.java_project.web.repository.StudyRoomRepository;
import com.study_site.java_project.web.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StudyController {

    private final StudyRoomService studyRoomService;
    private final StudyRoomRepository studyRoomRepository;

    private final ParticipateRoomService participateRoomService;
    private final ParticipateRoomRepository participateRoomRepository;
    private final MemberService memberService;
    private final UserService userService;
    private final S3Uploader s3Uploader;
    private final CheckToDoListService checkToDoListService;
    private final AlertService alertService;
    private final BoardRepository boardRepository;
    private final PagingService pagingService;

    /**
     * roomId에 해당하는 스터디 방에 참여하고 싶을 때 연결되는 컨트롤러
     */
    @PostMapping("/enter")
    @ResponseBody
    public Map<String, String> enterStudy(@RequestBody RoomIdDto dto, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");

        Map<String, String> map = new HashMap<>();
        String message = null;

        Member findMember = null;
        User findUser = null;

        StudyRoom findStudyRoom = studyRoomRepository.findByRoomId(dto.getRoomId());

        if (loginId == null && userEmail == null) {
            message = "로그인 후 이용할 수 있습니다.";
            map.put("message", message);
            return map;
        }

        if (loginId != null && userEmail == null) {
            if (findStudyRoom.getStatus() == RoomStatus.CLOSE) {
                message = "이 스터디는 모든 회원이 강퇴당해 들어갈 수 없는 스터디입니다.";
                map.put("message", message);
                return map;
            }

            findMember = memberService.findByUsername(loginId);
            String checkEnteredMember = participateRoomService.isCheckEnteredMember(dto.getRoomId(), findMember);
            if (checkEnteredMember.equals("환영합니다.")) {
                participateRoomService.enter(findStudyRoom, findMember, null);
                message = "참가되었습니다. 환영합니다!";
                map.put("message", message);
                return map;
            }
            else {
                message = checkEnteredMember;
                map.put("message", message);
                return map;
            }
        }

        if (userEmail != null && loginId == null) {
            if (findStudyRoom.getStatus() == RoomStatus.CLOSE) {
                message = "이 스터디는 모든 회원이 강퇴당해 들어갈 수 없는 스터디입니다.";
                map.put("message", message);
                return map;
            }

            findUser = userService.findByEmail(userEmail);
            String checkEnterUser = participateRoomService.isCheckEnterUser(dto.getRoomId(), findUser);
            if (checkEnterUser.equals("환영합니다.")) {
                participateRoomService.enter(findStudyRoom, null, findUser);
                message = "참가되었습니다. 환영합니다!";
                map.put("message", message);
                return map;
            }
            else {
                message = checkEnterUser;
                map.put("message", message);
                return map;
            }
        }

        return null;
    }

    /**
     * 전체 스터디 리스트를 보여주는 컨트롤러
     */
    @GetMapping("/study_list")
    public String studyList(Model model) {
        List<StudyRoom> findAllStudy = studyRoomRepository.findAll();

        model.addAttribute("studyList", findAllStudy);

        return "room/study_room_list";
    }

    /**
     * roomId에 해당하는 스터디에 대한 자세한 페이지를 볼 수 있는 컨트롤러
     */
    @GetMapping("/study_room_detail/{roomId}")
    public String detailStudyRoom(@PathVariable("roomId") String roomId, Model model) {
        StudyRoom findStudyRoom = studyRoomRepository.findByRoomId(roomId);

        List<ParticipateRoom> findByRoomId = participateRoomRepository.findByRoomId(roomId);

        model.addAttribute("studyRoom", findStudyRoom);
        model.addAttribute("count", findByRoomId.size());

        return "room/study_room_detail";
    }

    /**
     * 스터디 생성 폼으로 연결되는 컨트롤러
     */
    @GetMapping("/make_study_room")
    public String makeStudyRoom(Model model) {
        MakeStudyRoomDto dto = new MakeStudyRoomDto();
        model.addAttribute("study", dto);
        return "room/make_room";
    }

    /**
     * 실제 스터디를 생성했을 때 이어지는 POST 컨트롤러
     */
    @PostMapping("/make_study_room")
    public String makeStudyRoomPost(@Validated @ModelAttribute("study") MakeStudyRoomDto dto, BindingResult bindingResult,
                                    HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "room/make_room";
        }

        Member findMember = null;
        User findUser = null;

        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");

        if (loginId != null) {
            findMember = memberService.findByUsername(loginId);
        }

        if (userEmail != null) {
            findUser = userService.findByEmail(userEmail);
        }

        StudyRoom room = studyRoomService.save(dto);

        participateRoomService.save(room, findMember, findUser);
        checkToDoListService.save(room, findMember, findUser);

        return "redirect:/study_list";
    }

    /**
     * 내가 현재 속해있는 스터디를 보여주는 컨트롤러
     */
    @GetMapping("/my_study_list")
    public String myStudyList(Model model, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");

        Member findMember = null;
        User findUser = null;

        if (loginId != null && userEmail == null) {
            findMember = memberService.findByUsername(loginId);
            List<ParticipateRoom> findByUsername = participateRoomRepository.findByUsernameAndStatus(findMember.getUsername(), RoomStatus.OPEN, ParticipateStatus.ENTER);
            model.addAttribute("myStudy", findByUsername);
        }

        if (userEmail != null && loginId == null) {
            findUser = userService.findByEmail(userEmail);
            List<ParticipateRoom> findByEmail = participateRoomRepository.findByEmailAndStatus(findUser.getEmail(), RoomStatus.OPEN, ParticipateStatus.ENTER);
            model.addAttribute("myStudy", findByEmail);
        }

        return "room/my_study_list";
    }

    /**
     * 해당 roomId 에 과제를 제출을 위한 페이지로 이동되는 컨트롤러
     * 혹시 절대경로로 뚫고 들어오는 경우를 대비해서 스터디가 닫혀있거나 강퇴당한 사용자인 경우 밖으로 튕겨져나가게 하는 로직 추가
     */
    @GetMapping("/submit_study/{roomId}")
    public String submitStudy(@PathVariable("roomId") String roomId, Model model,
                              HttpServletResponse response, HttpSession session,
                              @RequestParam(value = "page", defaultValue = "0") Integer pageNow) throws IOException {
        SubmitStudyDto dto = new SubmitStudyDto();

        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");

        StudyRoom findStudy = studyRoomRepository.findByRoomId(roomId);

        if (findStudy.getStatus() == RoomStatus.CLOSE) {
            alertService.alertAndRedirect(response, "닫힌 스터디입니다.", "/my_study_list");
        }

        if (loginId != null && userEmail == null) {
            Member findMember = memberService.findByUsername(loginId);
            ParticipateRoom findParticipate = participateRoomRepository.findByRoomRoomIdAndMemberUsername(roomId, findMember.getUsername());

            model.addAttribute("participate", findParticipate);

            if (findParticipate.getParticipateStatus() == ParticipateStatus.EXPULSION) {
                alertService.alertAndRedirect(response, "강퇴당한 방은 다시 들어갈 수 없습니다.", "/my_study_list");
            }
        }

        if (loginId == null && userEmail != null) {
            User findUser = userService.findByEmail(userEmail);
            ParticipateRoom findParticipate = participateRoomRepository.findByRoomRoomIdAndUserEmail(roomId, findUser.getEmail());

            model.addAttribute("participate", findParticipate);

            if (findParticipate.getParticipateStatus() == ParticipateStatus.EXPULSION) {
                alertService.alertAndRedirect(response, "강퇴당한 방은 다시 들어갈 수 없습니다.", "/my_study_list");
            }
        }

        List<ParticipateRoom> findParticipate = participateRoomRepository.findByRoomRoomIdAndParticipateStatus(roomId, ParticipateStatus.ENTER);

        // 페이징 처리 (게시판)

        PageRequest page = PageRequest.of(pageNow, 10);
        List<Board> findBoards = boardRepository.findByRoomRoomId(roomId, page);
        List<Board> findAllBoard = boardRepository.findByRoomRoomId(roomId);

        PagingDto pagingDto = pagingService.paging(10, pageNow, findAllBoard.size());

        model.addAttribute("board", findBoards);
        model.addAttribute("pageCount", pagingDto.getMap());
        model.addAttribute("lastPage", pagingDto.getTotalPage());

        model.addAttribute("study", findStudy);
        model.addAttribute("checkList", findParticipate);
        model.addAttribute("submit", dto);


        return "room/submit_study";
    }

    /**
     * 과제를 제출했을 때 이동되는 컨트롤러
     */
    @PostMapping("/submit_study/{roomId}")
    @Transactional
    public String submitStudyPost(@ModelAttribute("submit")SubmitStudyDto dto, BindingResult bindingResult,
                                  HttpSession session, @PathVariable("roomId") String roomId,
                                  RedirectAttributes redirectAttributes, Model model) throws IOException {
        if (dto.getSubmit().isEmpty()) {
            bindingResult.reject("NotEmptyFile");
            StudyRoom findStudy = studyRoomRepository.findByRoomId(roomId);
            List<ParticipateRoom> findParticipate = participateRoomService.findByRoomId(roomId);

            model.addAttribute("study", findStudy);
            model.addAttribute("checkList", findParticipate);

            return "room/submit_study";
        }

        String today = LocalDateTime.now().getDayOfWeek().toString();

        Member findMember = null;
        User findUser = null;

        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");

        if (loginId != null && userEmail == null) {
            findMember = memberService.findByUsername(loginId);
            List<ParticipateRoom> findParticipate = participateRoomService.findByUsername(findMember.getUsername());
            for (ParticipateRoom participateRoom : findParticipate) {
                if (participateRoom.getRoom().getRoomId().equals(roomId)) {
                    participateRoom.setUpdateDate(LocalDateTime.now());
                    participateRoomService.checkToday(today, participateRoom);
                }
            }
        }

        if (loginId == null && userEmail != null) {
            findUser = userService.findByEmail(userEmail);
            List<ParticipateRoom> findParticipate = participateRoomService.findByEmail(findUser.getEmail());
            for (ParticipateRoom participateRoom : findParticipate) {
                if (participateRoom.getRoom().getRoomId().equals(roomId)) {
                    participateRoom.setUpdateDate(LocalDateTime.now());
                    participateRoomService.checkToday(today, participateRoom);
                }
            }
        }

        s3Uploader.upload(dto.getSubmit(), "static");

        redirectAttributes.addAttribute("roomId", roomId);

        return "redirect:/submit_study/{roomId}";
    }
}
