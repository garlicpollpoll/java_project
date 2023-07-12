package com.study_site.java_project.web.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study_site.java_project.web.entity.ParticipateRoom;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.enums.ParticipateStatus;
import com.study_site.java_project.web.enums.RoomStatus;
import com.study_site.java_project.web.repository.StudyRoomRepository;
import com.study_site.java_project.web.repository.custom.ParticipateCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.study_site.java_project.web.entity.QMember.*;
import static com.study_site.java_project.web.entity.QParticipateRoom.*;
import static com.study_site.java_project.web.entity.QStudyRoom.*;
import static com.study_site.java_project.web.entity.QUser.*;

public class ParticipateRoomRepositoryImpl implements ParticipateCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public ParticipateRoomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ParticipateRoom> findByRoomId(String roomId) {
        return queryFactory
                .selectFrom(participateRoom)
                .leftJoin(participateRoom.room, studyRoom).fetchJoin()
                .leftJoin(participateRoom.member, member).fetchJoin()
                .leftJoin(participateRoom.user, user).fetchJoin()
                .where(
                        studyRoom.roomId.eq(roomId)
                )
                .fetch();
    }

    @Override
    public List<ParticipateRoom> findByUsername(String username) {
        return queryFactory
                .selectFrom(participateRoom)
                .leftJoin(participateRoom.room, studyRoom).fetchJoin()
                .leftJoin(participateRoom.member, member).fetchJoin()
                .leftJoin(participateRoom.user, user).fetchJoin()
                .where(
                        member.username.eq(username)
                )
                .fetch();
    }

    @Override
    public List<ParticipateRoom> findByEmail(String email) {
        return queryFactory
                .selectFrom(participateRoom)
                .leftJoin(participateRoom.room, studyRoom).fetchJoin()
                .leftJoin(participateRoom.member, member).fetchJoin()
                .leftJoin(participateRoom.user, user).fetchJoin()
                .where(
                        user.email.eq(email)
                )
                .fetch();
    }

    @Override
    public List<ParticipateRoom> findByRoomIdAndUpdateDate(String roomId, LocalDateTime updateDate) {
        return queryFactory
                .selectFrom(participateRoom)
                .leftJoin(participateRoom.room, studyRoom).fetchJoin()
                .leftJoin(participateRoom.member, member).fetchJoin()
                .leftJoin(participateRoom.user, user).fetchJoin()
                .where(
                        studyRoom.roomId.eq(roomId),
                        participateRoom.updateDate.loe(updateDate)
                )
                .fetch();
    }

    @Override
    public List<ParticipateRoom> findByRoomIdAndDeadline(String roomId, long deadline) {
        return queryFactory
                .selectFrom(participateRoom)
                .leftJoin(participateRoom.room, studyRoom)
                .where(
                        participateRoom.warning.eq(deadline),
                        studyRoom.roomId.eq(roomId)
                )
                .fetch();
    }

    @Override
    public List<ParticipateRoom> findByUsernameAndStatus(String username, RoomStatus status, ParticipateStatus pStatus) {
        return queryFactory
                .selectFrom(participateRoom)
                .leftJoin(participateRoom.room, studyRoom).fetchJoin()
                .leftJoin(participateRoom.member, member).fetchJoin()
                .where(
                        participateRoom.member.username.eq(username),
                        studyRoom.status.eq(status),
                        participateRoom.participateStatus.eq(pStatus)
                )
                .fetch();
    }

    @Override
    public List<ParticipateRoom> findByEmailAndStatus(String email, RoomStatus status, ParticipateStatus pStatus) {
        return queryFactory
                .selectFrom(participateRoom)
                .leftJoin(participateRoom.user, user).fetchJoin()
                .leftJoin(participateRoom.room, studyRoom).fetchJoin()
                .where(
                        participateRoom.user.email.eq(email),
                        participateRoom.participateStatus.eq(pStatus),
                        studyRoom.status.eq(status)
                )
                .fetch();
    }
}
