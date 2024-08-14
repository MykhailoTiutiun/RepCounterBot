package com.mykhailotiutiun.repcounterbot.util.impl;

import com.mykhailotiutiun.repcounterbot.util.LocalDateWeekUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LocalDateWeekUtilImpl implements LocalDateWeekUtil {

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
