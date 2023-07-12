package com.study_site.java_project.web.dto.study;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SubmitStudyDto {

    private MultipartFile submit;
}
