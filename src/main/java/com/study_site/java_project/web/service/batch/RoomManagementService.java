package com.study_site.java_project.web.service.batch;

import com.study_site.java_project.config.batch.batch.ParticipateRoomManagementBatch;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.repository.StudyRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomManagementService {

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final ParticipateRoomManagementBatch config;
    private final StudyRoomRepository studyRoomRepository;

    @Scheduled(fixedDelay = 5000)
    public void roomManagement() {
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
