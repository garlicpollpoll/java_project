package com.study_site.java_project.web.dto.study;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class MakeStudyRoomDto {

    @NotEmpty
    private String roomName;
    @NotNull
    private long deadline;
    @NotNull
    private long warning;

    private String content;
}
