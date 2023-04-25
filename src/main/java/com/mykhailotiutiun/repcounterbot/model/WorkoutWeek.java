package com.mykhailotiutiun.repcounterbot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@Document(collection = "workout-weeks")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutWeek {

    @Id
    String id;

    @DocumentReference(lazy = true)
    User user;

    Boolean current = true;

    LocalDate weekStartDate;
    LocalDate weekEndDate;

    public WorkoutWeek(User user, LocalDate weekStartDate, LocalDate weekEndDate) {
        this.user = user;
        this.weekStartDate = weekStartDate;
        this.weekEndDate = weekEndDate;
    }

    public Boolean isCurrent() {
        return current;
    }

    public String print(String pattern) {
        return String.format(pattern, weekStartDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), weekEndDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }
}
