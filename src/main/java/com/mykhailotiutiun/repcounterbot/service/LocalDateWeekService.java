package com.mykhailotiutiun.repcounterbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Slf4j
@Service
public class LocalDateWeekService {

    public LocalDate getFirstDateOfWeekFromDate(LocalDate localDate){

        int i = 0;
        while(localDate.getDayOfWeek().minus(i).getValue() != 1){
            i++;
        }

        return localDate.minusDays(i);
    }

    public LocalDate getLastDateOfWeekFromDate(LocalDate localDate){

        int i = 0;
        while(localDate.getDayOfWeek().plus(i).getValue() != 7){
            i++;
        }

        return localDate.plusDays(i);
    }
}
