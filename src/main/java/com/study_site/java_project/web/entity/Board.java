package com.study_site.java_project.web.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private StudyRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String createDate;

    @Lob
    private String content;

    public Board(StudyRoom room, Member member, User user, String title, String createDate, String content) {
        this.room = room;
        this.member = member;
        this.user = user;
        this.title = title;
        this.createDate = createDate;
        this.content = content;
    }
}
