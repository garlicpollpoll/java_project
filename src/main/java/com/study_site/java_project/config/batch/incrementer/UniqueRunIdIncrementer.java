package com.study_site.java_project.config.batch.incrementer;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

public class UniqueRunIdIncrementer extends RunIdIncrementer {

    private static final String RUN_ID = "run.id";
    private static final Long DEFAULT_VALUE = 0L;
    private static final Long INCREMENT_VALUE = 1L;

    @Override
    public JobParameters getNext(JobParameters parameters) {
        JobParameters params = (parameters == null) ? new JobParameters() : parameters;
        return new JobParametersBuilder()
                .addLong(RUN_ID, params.getLong(RUN_ID, DEFAULT_VALUE) + INCREMENT_VALUE)
                .toJobParameters();
    }
}
