package com.mykhailotiutiun.repcounterbot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

@Getter
@Setter
@ToString
@Document(collection = "workout-days")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDay {

    @Id
    String id;

    @DocumentReference(lazy = true)
    WorkoutWeek workoutWeek;

    String name;

    LocalDate date;

    Boolean isWorkoutDay;

    public WorkoutDay(WorkoutWeek workoutWeek, String name, LocalDate date, Boolean isWorkoutDay) {
        this.workoutWeek = workoutWeek;
        this.name = name;
        this.date = date;
        this.isWorkoutDay = isWorkoutDay;
    }

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
