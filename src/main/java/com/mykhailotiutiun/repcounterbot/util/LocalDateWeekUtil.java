package com.mykhailotiutiun.repcounterbot.util;

import java.time.LocalDate;

public interface LocalDateWeekUtil {
    LocalDate getFirstDateOfWeekFromDate(LocalDate localDate);

    LocalDate getLastDateOfWeekFromDate(LocalDate localDate);

    Boolean isCurrentWeek(LocalDate startWeekLocalDate, LocalDate endWeekLocalDate);
}
