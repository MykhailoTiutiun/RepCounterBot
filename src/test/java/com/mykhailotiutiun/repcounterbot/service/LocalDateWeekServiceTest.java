package com.mykhailotiutiun.repcounterbot.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LocalDateWeekServiceTest {

    @Autowired
    private LocalDateWeekService localDateWeekService;

    @Test
    public void getFirstDateOfWeekFromDate() {
        LocalDate localDate = LocalDate.of(2023, 4, 6);

        assertTrue(LocalDate.of(2023, 4, 3).isEqual(localDateWeekService.getFirstDateOfWeekFromDate(localDate)));
    }

    @Test
    public void getLastDateOfWeekFromDate() {
        LocalDate localDate = LocalDate.of(2023, 4, 6);

        assertTrue(LocalDate.of(2023, 4, 9).isEqual(localDateWeekService.getLastDateOfWeekFromDate(localDate)));
    }
}