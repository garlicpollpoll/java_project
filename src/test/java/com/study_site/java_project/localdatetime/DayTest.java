package com.study_site.java_project.localdatetime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class DayTest {

    @Test
    @DisplayName("요일 테스트")
    public void dayTest() throws Exception {
        //given
        System.out.println("LocalDateTime.now().getDayOfMonth() = " + LocalDateTime.now().getDayOfMonth());
        System.out.println("LocalDateTime.now().getDayOfWeek() = " + LocalDateTime.now().getDayOfWeek());
        System.out.println("LocalDateTime.now().getDayOfYear() = " + LocalDateTime.now().getDayOfYear());
        //when

        //then
    }
}
