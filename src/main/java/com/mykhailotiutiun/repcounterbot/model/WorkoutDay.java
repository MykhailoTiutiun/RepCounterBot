package com.mykhailotiutiun.repcounterbot.model;

import lombok.*;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDay {

    private Long id;
    private String name;
    private LocalDate date;
    private Boolean isWorkoutDay;
    private WorkoutWeek workoutWeek;

    public Boolean isWorkoutDay() {
        return isWorkoutDay;
    }

    public String print(String isRestDayString, String isNotRestDayAndWorkoutString, String localTag) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(StringUtils.capitalize(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag(localTag))));

        stringBuilder.append(", ");

        if (isWorkoutDay != null && !isWorkoutDay) {
            stringBuilder.append(isRestDayString);
        } else {
            stringBuilder.append(Objects.requireNonNullElse(name, isNotRestDayAndWorkoutString));
        }

        stringBuilder.append(" ");

        stringBuilder.append(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        return stringBuilder.toString();
    }


}
