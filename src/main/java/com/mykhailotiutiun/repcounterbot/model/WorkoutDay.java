package com.mykhailotiutiun.repcounterbot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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

    @DocumentReference
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

    public String print(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("uk-UA")));

        stringBuilder.append(" ");

        if(isWorkoutDay != null && !isWorkoutDay) {
            stringBuilder.append("день відпочинку");
        } else {
            stringBuilder.append(Objects.requireNonNullElse(name, "не вказано"));
        }

        stringBuilder.append(" ");

        stringBuilder.append(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        return stringBuilder.toString();
    }
}
