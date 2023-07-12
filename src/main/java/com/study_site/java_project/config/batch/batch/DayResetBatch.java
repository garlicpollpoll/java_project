package com.study_site.java_project.config.batch.batch;

import com.study_site.java_project.config.batch.reader.QuerydslPagingItemReader;
import com.study_site.java_project.config.batch.incrementer.UniqueRunIdIncrementer;
import com.study_site.java_project.web.entity.*;
import com.study_site.java_project.web.repository.ParticipateRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DayResetBatch {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ParticipateRoomRepository participateRoomRepository;
    private final EntityManagerFactory emf;

    @Bean
    public Job dayResetBatchJob() {
        return jobBuilderFactory
                .get("dayResetBatchJob")
                .preventRestart()
                .incrementer(new UniqueRunIdIncrementer())
                .start(dayResetBatchStep())
                .build();
    }

    @Bean
    public Step dayResetBatchStep() {
        return stepBuilderFactory
                .get("dayResetBatchStep")
                .<ParticipateRoom, ParticipateRoom>chunk(100)
                .reader(dayResetBatchReader())
                .processor(dayResetBatchProcessor())
                .writer(dayResetBatchWriter())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public QuerydslPagingItemReader<ParticipateRoom> dayResetBatchReader() {
        return new QuerydslPagingItemReader<>(emf, 100,
                queryFactory -> queryFactory
                        .selectFrom(QParticipateRoom.participateRoom)
        );
    }

    @Bean
    public ItemProcessor<ParticipateRoom, ParticipateRoom> dayResetBatchProcessor() {
        return participateRoom -> {
            log.info("processor 작동");
            return participateRoom.allDayReset();
        };
    }

    @Bean
    public ItemWriter<ParticipateRoom> dayResetBatchWriter() {
        return ((List<? extends ParticipateRoom> participates) -> {
            participateRoomRepository.saveAll(participates);
        });
    }
}

