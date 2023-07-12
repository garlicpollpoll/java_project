package com.study_site.java_project.batch;

import com.study_site.java_project.config.batch.batch.NoOffsetTestBatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class NoOffsetBatchTest {

    @Autowired
    NoOffsetTestBatch batch;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobExplorer jobExplorer;

    @Test
//    @DisplayName("JpaCursorItemReader 속도 테스트")
//    @DisplayName("JpaPagingItemReader 속도 테스트")
//    @DisplayName("QuerydslPagingItemReader 속도 테스트")
    @DisplayName("QuerydslNoOffsetPagingItemReader 속도 테스트")
    public void test() throws Exception {
        jobLauncher.run(
                batch.noOffsetJob(), new JobParametersBuilder(jobExplorer)
                        .addString("requestDate", LocalDateTime.now().toString())
                        .toJobParameters()
        );
    }
}
