package com.study_site.java_project.web.dto.paging;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class PagingDto {

    private long totalPage;
    private Map<Long, Long> map = new LinkedHashMap<>();
}
