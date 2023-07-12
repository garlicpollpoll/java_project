package com.study_site.java_project.web.service;

import com.study_site.java_project.web.entity.CheckToDoList;
import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.entity.User;
import com.study_site.java_project.web.repository.CheckToDoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckToDoListService {

    private final CheckToDoListRepository checkToDoListRepository;

    @Transactional
    public void save(StudyRoom room, Member member, User user) {
        CheckToDoList check = new CheckToDoList(room, member, user, null, null, null, null, null, null, null);

        checkToDoListRepository.save(check);
    }

    @Transactional
    public List<CheckToDoList> findCheckListByRoomId(String roomId) {
        return checkToDoListRepository.findByRoomRoomId(roomId);
    }
}
