package com.mykhailotiutiun.repcounterbot.model;

import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutWeek {

    private Long id;
    private User user;
    private Boolean current = true;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;

    public Boolean isCurrent() {
        return current;
    }

    public String print(String pattern) {
        return String.format(pattern, weekStartDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), weekEndDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }
}
