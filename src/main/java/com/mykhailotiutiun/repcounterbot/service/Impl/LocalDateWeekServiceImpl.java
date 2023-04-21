package com.mykhailotiutiun.repcounterbot.service.Impl;

import com.mykhailotiutiun.repcounterbot.service.LocalDateWeekService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class LocalDateWeekServiceImpl implements LocalDateWeekService {

    @Override
    public LocalDate getFirstDateOfWeekFromDate(LocalDate localDate) {

        int i = 0;
        while (localDate.getDayOfWeek().minus(i).getValue() != 1) {
            i++;
        }

        return localDate.minusDays(i);
    }

    @Override
    public LocalDate getLastDateOfWeekFromDate(LocalDate localDate) {

        int i = 0;
        while (localDate.getDayOfWeek().plus(i).getValue() != 7) {
            i++;
        }

        return localDate.plusDays(i);
    }

    @Override
    public Boolean isCurrentWeek(LocalDate startWeekLocalDate, LocalDate endWeekLocalDate) {
        return (LocalDate.now().isAfter(startWeekLocalDate) || LocalDate.now().isEqual(startWeekLocalDate)) && (LocalDate.now().isBefore(endWeekLocalDate) || LocalDate.now().isEqual(endWeekLocalDate));
    }
}
