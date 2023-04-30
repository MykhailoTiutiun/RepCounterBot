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

    @DBRef(lazy = true)
    List<WorkoutSet> prevWorkoutSets = new ArrayList<>();

    public WorkoutExercise(String name, WorkoutDay workoutDay) {
        this.name = name;
        this.workoutDay = workoutDay;
    }

    public WorkoutExercise(Byte number, String name, WorkoutDay workoutDay, List<WorkoutSet> prevWorkoutSets) {
        this.number = number;
        this.name = name;
        this.workoutDay = workoutDay;
        this.prevWorkoutSets = prevWorkoutSets;
    }


    public String printForWorkoutExercise(String onPrevWorkoutMessage, String workoutSetsPrintPattern){
        StringBuilder stringBuilder = new StringBuilder(String.format("%d. %s", number, name));
        if(!prevWorkoutSets.isEmpty()){
            stringBuilder.append("\n").append(onPrevWorkoutMessage).append(printWorkoutSets(this.prevWorkoutSets, workoutSetsPrintPattern));
        }
        return stringBuilder.toString();
    }

    public String printForWorkoutDayKeyboard() {
        return String.format("%d. %s", number, name);
    }

    private String printWorkoutSets(List<WorkoutSet> workoutSets, String workoutSetsPrintPattern) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; workoutSets.size() > i; i++){
            if(i%2==0){
                stringBuilder.append("\n");
            }
            stringBuilder.append(workoutSets.get(i).print(workoutSetsPrintPattern));
            stringBuilder.append(", ");
        }

        return stringBuilder.toString();
    }
}
