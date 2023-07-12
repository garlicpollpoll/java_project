package com.study_site.java_project.web.dto.join;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class JoinDto {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String loginPw;
}
