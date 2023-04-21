package com.mykhailotiutiun.repcounterbot.service;

import java.time.LocalDate;

public interface LocalDateWeekService {
    LocalDate getFirstDateOfWeekFromDate(LocalDate localDate);

    LocalDate getLastDateOfWeekFromDate(LocalDate localDate);

    Boolean isCurrentWeek(LocalDate startWeekLocalDate, LocalDate endWeekLocalDate);
}
