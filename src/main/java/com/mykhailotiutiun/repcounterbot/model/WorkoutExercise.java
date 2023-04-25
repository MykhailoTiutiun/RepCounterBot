package com.mykhailotiutiun.repcounterbot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Document(collection = "workout-exercises")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExercise {

    @Id
    String id;

    Byte number;

    String name;

    @DocumentReference(lazy = true)
    WorkoutDay workoutDay;

    @DBRef(lazy = true)
    List<WorkoutSet> workoutSets = new ArrayList<>();

    public WorkoutExercise(String name, WorkoutDay workoutDay) {
        this.name = name;
        this.workoutDay = workoutDay;
    }

    public String print() {
        return String.format("%d. %s", number, name);
    }
}
