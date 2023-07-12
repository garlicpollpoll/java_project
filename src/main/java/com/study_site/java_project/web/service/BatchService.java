package com.study_site.java_project.web.service;

import com.study_site.java_project.config.batch.batch.ParticipateRoomManagementBatch;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final ParticipateRoomManagementBatch config;
    private final JobExplorer jobExplorer;
    private final JobLauncher jobLauncher;

//    @Scheduled(fixedDelay = 10000)
    public void batchJob() {
        try {
            jobLauncher.run(config.roomManagementBatchJob(),
                    new JobParametersBuilder(jobExplorer)
                            .addString("requestDate", LocalDateTime.now().toString())
                            .toJobParameters()
            );
        } catch (JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e);
        } catch (JobRestartException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}
