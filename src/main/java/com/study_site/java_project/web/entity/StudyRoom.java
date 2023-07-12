package com.study_site.java_project.web.entity;

import com.study_site.java_project.web.enums.RoomStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class StudyRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    private String roomId;
    private String roomName;
    private long deadline;
    private long warning;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    public StudyRoom(String roomId, String roomName, long deadline, long warning, RoomStatus status, String content) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.deadline = deadline;
        this.warning = warning;
        this.status = status;
        this.content = content;
    }

    public StudyRoom roomStatusOpenToClose() {
        this.status = RoomStatus.CLOSE;
        return this;
    }
}
