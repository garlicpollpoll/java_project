package com.study_site.java_project.web.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Alarm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private StudyRoom room;

    private LocalDateTime createDate;

    private String message;

    public Alarm(Member member, User user, StudyRoom room, LocalDateTime createDate, String message) {
        this.member = member;
        this.user = user;
        this.room = room;
        this.createDate = createDate;
        this.message = message;
    }
}
