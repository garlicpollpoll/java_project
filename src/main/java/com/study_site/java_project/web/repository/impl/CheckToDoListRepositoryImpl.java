package com.study_site.java_project.web.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study_site.java_project.web.entity.*;
import com.study_site.java_project.web.repository.custom.ToDoListCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

import static com.study_site.java_project.web.entity.QCheckToDoList.*;
import static com.study_site.java_project.web.entity.QMember.*;
import static com.study_site.java_project.web.entity.QStudyRoom.*;
import static com.study_site.java_project.web.entity.QUser.*;

public class CheckToDoListRepositoryImpl implements ToDoListCustomRepository {

    private  final JPAQueryFactory queryFactory;

    @Autowired
    public CheckToDoListRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * @deprecated
     * @param roomId
     * @return
     */
    @Override
    public List<CheckToDoList> findByRoomRoomId(String roomId) {
        return queryFactory
                .selectFrom(checkToDoList)
                .leftJoin(checkToDoList.room, studyRoom).fetchJoin()
                .leftJoin(checkToDoList.member, member).fetchJoin()
                .leftJoin(checkToDoList.user, user).fetchJoin()
                .where(
                        studyRoom.roomId.eq(roomId)
                )
                .fetch();
    }

    /**
     * @deprecated
     * @param username
     * @return
     */
    @Override
    public List<CheckToDoList> findByUsername(String username) {
        return queryFactory
                .selectFrom(checkToDoList)
                .leftJoin(checkToDoList.room, studyRoom).fetchJoin()
                .leftJoin(checkToDoList.member, member).fetchJoin()
                .leftJoin(checkToDoList.user, user).fetchJoin()
                .where(
                        member.username.eq(username)
                )
                .fetch();
    }

    /**
     * @deprecated
     * @param email
     * @return
     */
    @Override
    public List<CheckToDoList> findByEmail(String email) {
        return queryFactory
                .selectFrom(checkToDoList)
                .leftJoin(checkToDoList.room, studyRoom).fetchJoin()
                .leftJoin(checkToDoList.member, member).fetchJoin()
                .leftJoin(checkToDoList.user, user).fetchJoin()
                .where(
                        user.email.eq(email)
                )
                .fetch();
    }
}
