package com.study_site.java_project.config.batch.batch;

import com.study_site.java_project.config.batch.reader.QuerydslNoOffsetPagingItemReader;
import com.study_site.java_project.config.batch.reader.QuerydslPagingItemReader;
import com.study_site.java_project.config.batch.reader.expression.Expression;
import com.study_site.java_project.config.batch.reader.options.QuerydslNoOffsetNumberOptions;
import com.study_site.java_project.web.entity.Member;
import com.study_site.java_project.web.entity.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

import static com.study_site.java_project.web.entity.QMember.*;

@Configuration
@RequiredArgsConstructor
public class NoOffsetTestBatch {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;

    private static final int chunk = 1000;

    @Bean
    public Job noOffsetJob() {
        return jobBuilderFactory.get("noOffsetJob")
                .start(noOffsetStep())
                .build();
    }

    @Bean
    public Step noOffsetStep() {
        return stepBuilderFactory.get("noOffsetStep")
                .<Member, Member>chunk(chunk)
                .reader(noOffsetReader())
                .writer(noOffsetWriter())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public QuerydslNoOffsetPagingItemReader<Member> noOffsetReader() {
        QuerydslNoOffsetNumberOptions<Member, Long> options = new QuerydslNoOffsetNumberOptions<>(member.id, Expression.ASC);

        return new QuerydslNoOffsetPagingItemReader<>(emf, chunk, options, queryFactory -> {
            return queryFactory.selectFrom(member);
        });
    }

//    @Bean
//    public QuerydslPagingItemReader<Member> noOffsetReader() {
//        return new QuerydslPagingItemReader<>(emf, chunk, queryFactory -> {
//            return queryFactory.selectFrom(member);
//        });
//    }

//    @Bean
//    public JpaPagingItemReader<Member> noOffsetReader() {
//        return new JpaPagingItemReaderBuilder<Member>()
//                .entityManagerFactory(emf)
//                .pageSize(chunk)
//                .queryString("select m from Member m")
//                .name("noOffsetReader")
//                .build();
//    }

//    @Bean
//    public JpaCursorItemReader<Member> noOffsetReader() {
//        return new JpaCursorItemReaderBuilder<Member>()
//                .entityManagerFactory(emf)
//                .queryString("select m from Member m")
//                .name("noOffsetReader")
//                .build();
//    }

    @Bean
    public JpaItemWriter<Member> noOffsetWriter() {
        return new JpaItemWriterBuilder<Member>()
                .entityManagerFactory(emf)
                .build();
    }
}
