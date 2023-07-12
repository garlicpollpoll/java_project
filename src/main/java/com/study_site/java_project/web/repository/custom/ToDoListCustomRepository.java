package com.study_site.java_project.web.repository.custom;

import com.study_site.java_project.web.entity.CheckToDoList;

import java.util.List;

public interface ToDoListCustomRepository {

    List<CheckToDoList> findByRoomRoomId(String roomId);

    List<CheckToDoList> findByUsername(String username);

    List<CheckToDoList> findByEmail(String email);
}
