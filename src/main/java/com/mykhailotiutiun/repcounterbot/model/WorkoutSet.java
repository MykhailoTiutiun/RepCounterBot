package com.mykhailotiutiun.repcounterbot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "workout-set")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSet {

    @Id
    String id;

    Integer number;
    Integer reps;
    Integer weight;

    public WorkoutSet(Integer number, Integer reps, Integer weight) {
        this.number = number;
        this.reps = reps;
        this.weight = weight;
    }

    public String print(String pattern) {
        return String.format(pattern, number, weight, reps);
    }
}
