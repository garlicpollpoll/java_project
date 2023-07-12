package com.study_site.java_project.web.entity;

import com.study_site.java_project.web.enums.MemberStatus;
import com.study_site.java_project.web.enums.ParticipateStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 어디에 누가 참여하고 있는지에 대한 정보 (주로 사람에 대한 정보)
 * 어떤 방에 (room), 누가 (member, user), 경고는 몇회인지 (warning), 이사람이 이 방의 주인인지 (MemberStatus)
 * 이사람의 상태 (참여하고 있는지 강퇴당했는지) (RoomStatus)
 */
@Entity
@Getter
@NoArgsConstructor
public class ParticipateRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private StudyRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    private long warning;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    private ParticipateStatus participateStatus;

    private LocalDateTime updateDate;

    @Setter
    private String monday;
    @Setter
    private String tuesday;
    @Setter
    private String wednesday;
    @Setter
    private String thursday;
    @Setter
    private String friday;
    @Setter
    private String saturday;
    @Setter
    private String sunday;


    public ParticipateRoom(StudyRoom room, Member member, User user, long warning,
                           MemberStatus status, ParticipateStatus participateStatus,
                           LocalDateTime updateDate, String monday, String tuesday, String wednesday,
                           String thursday, String friday, String saturday, String sunday) {
        this.room = room;
        this.member = member;
        this.user = user;
        this.warning = warning;
        this.status = status;
        this.participateStatus = participateStatus;
        this.updateDate = updateDate;

        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public ParticipateRoom allDayReset() {
        this.monday = null;
        this.tuesday = null;
        this.wednesday = null;
        this.thursday = null;
        this.friday = null;
        this.saturday = null;
        this.sunday = null;
        return this;
    }

    public void setUpdateDate(LocalDateTime now) {
        this.updateDate = now;
    }

    public ParticipateRoom participateStatusEnterToExpulsion() {
        this.participateStatus = ParticipateStatus.EXPULSION;
        return this;
    }

    public ParticipateRoom addWarning() {
        long nowWarning = this.warning;
        long warningPlus = ++nowWarning;
        this.warning = warningPlus;
        return this;
    }
}
