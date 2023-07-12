package com.study_site.java_project.web.repository;

import com.study_site.java_project.web.entity.CheckToDoList;
import com.study_site.java_project.web.repository.custom.ToDoListCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckToDoListRepository extends JpaRepository<CheckToDoList, Long>, ToDoListCustomRepository {
}
