package com.study_site.java_project.config.batch.batch;

import com.study_site.java_project.config.batch.writer.JpaItemListWriter;
import com.study_site.java_project.config.batch.reader.QuerydslPagingItemReader;
import com.study_site.java_project.config.batch.incrementer.UniqueRunIdIncrementer;
import com.study_site.java_project.web.entity.ParticipateRoom;
import com.study_site.java_project.web.entity.StudyRoom;
import com.study_site.java_project.web.enums.ParticipateStatus;
import com.study_site.java_project.web.enums.RoomStatus;
import com.study_site.java_project.web.repository.ParticipateRoomRepository;
import com.study_site.java_project.web.repository.StudyRoomRepository;
import com.study_site.java_project.web.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.study_site.java_project.web.entity.QParticipateRoom.*;
import static com.study_site.java_project.web.entity.QStudyRoom.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ParticipateRoomManagementBatch {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final ParticipateRoomRepository participateRoomRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final AlarmService alarmService;

    private static final int PAGE_SIZE = 1000;

    @Bean
    public Job roomManagementBatchJob() {
        return jobBuilderFactory
                .get("roomManagementBatchJob")
                .preventRestart()
                .incrementer(new UniqueRunIdIncrementer())
                .start(roomManagementWithWarningStep())
                .next(roomManagementWithExpulsionStep())
                .next(roomManagementWithRoomCloseStep())
                .build();
    }

    /**
     * 제한기간 내에 제출하지 못한 경우에 경고를 1회 추가하는 Step1
     */
    @Bean
    public Step roomManagementWithWarningStep() {
        return stepBuilderFactory
                .get("roomManagementWithWarningStep")
                .<StudyRoom, List<ParticipateRoom>>chunk(PAGE_SIZE)
                .reader(warningReader())
                .processor(warningProcessor())
                .writer(warningWriter())
                .allowStartIfComplete(true)
                .build();
    }

    public QuerydslPagingItemReader<StudyRoom> warningReader() {
        return new QuerydslPagingItemReader<>(emf, PAGE_SIZE,
                    queryFactory -> queryFactory
                            .selectFrom(studyRoom)
                );
    }

    public ItemProcessor<StudyRoom, List<ParticipateRoom>> warningProcessor() {
        return studyRoom -> {
            List<ParticipateRoom> findParticipate = participateRoomRepository.findByRoomIdAndUpdateDate(studyRoom.getRoomId(), LocalDateTime.now().minusDays(studyRoom.getDeadline()));
            List<ParticipateRoom> participateRooms = new ArrayList<>();
            for (ParticipateRoom room : findParticipate) {
                if (room.getParticipateStatus() == ParticipateStatus.ENTER) {
                    alarmService.makeAlarm(room.getMember(), room.getUser(), studyRoom, studyRoom.getRoomName() + "에서 경고가 1회 추가되었습니다.");
                    participateRooms = Arrays.asList(room.addWarning());
                }
            }
            return participateRooms;
        };
    }

    public JpaItemListWriter<ParticipateRoom> warningWriter() {
        JpaItemWriter<ParticipateRoom> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return new JpaItemListWriter<>(writer);
    }

//    public JpaItemWriter<List<ParticipateRoom>> warningWriter() {
//        JpaItemWriter<List<ParticipateRoom>> writer = new JpaItemWriter<>();
//        writer.setEntityManagerFactory(emf);
//        return writer;
//    }

    /**
     * 방에서 설정한 경고 횟수에 모두 도달하면 방에 속해있는 회원을 강퇴하는 Step2
     */
    public Step roomManagementWithExpulsionStep() {
        return stepBuilderFactory
                .get("roomManagementWithExpulsionStep")
                .<StudyRoom, List<ParticipateRoom>>chunk(PAGE_SIZE)
                .reader(expulsionReader())
                .processor(expulsionProcessor())
                .writer(expulsionWriter())
                .allowStartIfComplete(true)
                .build();
    }

    public QuerydslPagingItemReader<StudyRoom> expulsionReader() {
        return new QuerydslPagingItemReader<>(emf, PAGE_SIZE,
                    queryFactory -> queryFactory
                            .selectFrom(studyRoom)
                );
    }

    public ItemProcessor<StudyRoom, List<ParticipateRoom>> expulsionProcessor() {
        return studyRoom -> {
            StudyRoom findStudyRoom = studyRoomRepository.findByRoomId(studyRoom.getRoomId());
            List<ParticipateRoom> findParticipate = participateRoomRepository.findByRoomIdAndDeadline(findStudyRoom.getRoomId(), findStudyRoom.getWarning());
            List<ParticipateRoom> participateList = new ArrayList<>();
            for (ParticipateRoom room : findParticipate) {
                if (findStudyRoom.getWarning() == room.getWarning() && room.getParticipateStatus() == ParticipateStatus.ENTER) {
                    alarmService.makeAlarm(room.getMember(), room.getUser(), studyRoom, studyRoom.getRoomName() + "에서 경고 누적으로 추방되었습니다.");
                    participateList = Arrays.asList(room.participateStatusEnterToExpulsion());
                }
            }
            return participateList;
        };
    }

    public JpaItemListWriter<ParticipateRoom> expulsionWriter() {
        JpaItemWriter<ParticipateRoom> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return new JpaItemListWriter<>(writer);
    }

//    public JpaItemWriter<List<ParticipateRoom>> expulsionWriter() {
//        JpaItemWriter<List<ParticipateRoom>> writer = new JpaItemWriter<>();
//        writer.setEntityManagerFactory(emf);
//        return writer;
//    }

    /**
     * 방에 속한 모든 회원이 강퇴당한 경우 방을 CLOSE 해버리는 Step3
     */
    public Step roomManagementWithRoomCloseStep() {
        return stepBuilderFactory
                .get("roomManagementWithRoomCloseStep")
                .<ParticipateRoom, StudyRoom>chunk(PAGE_SIZE)
                .reader(closeReader())
                .processor(closeProcessor())
                .writer(closeWriter())
                .allowStartIfComplete(true)
                .build();
    }

    public QuerydslPagingItemReader<ParticipateRoom> closeReader() {
        return new QuerydslPagingItemReader<>(emf, PAGE_SIZE,
                queryFactory -> queryFactory
                        .selectFrom(participateRoom)
                        .leftJoin(participateRoom.room, studyRoom).fetchJoin()
                        .where(
                                participateRoom.participateStatus.eq(ParticipateStatus.EXPULSION),
                                participateRoom.room.roomId.eq(studyRoom.roomId)
                        )
        );
    }

    public ItemProcessor<ParticipateRoom, StudyRoom> closeProcessor() {
        return participate -> {
            int allSize = participateRoomRepository.findByRoomId(participate.getRoom().getRoomId()).size();
            int allExpulsionSize = participateRoomRepository.findByRoomRoomIdAndParticipateStatus(participate.getRoom().getRoomId(), ParticipateStatus.EXPULSION).size();
            if (allSize == allExpulsionSize && participate.getRoom().getStatus() == RoomStatus.OPEN) {
                alarmService.makeAlarm(participate.getMember(), participate.getUser(), participate.getRoom(), participate.getRoom().getRoomName() + "의 회원이 모두 추방되어 스터디가 닫혔습니다.");
                return participate.getRoom().roomStatusOpenToClose();
            }
            else {
                return participate.getRoom();
            }
        };
    }

    public ItemWriter<StudyRoom> closeWriter() {
        return ((List<? extends StudyRoom> studyRoom) -> {
            studyRoomRepository.saveAll(studyRoom);
        });
    }
}
