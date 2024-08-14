package com.mykhailotiutiun.repcounterbot.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExercise {

    private String id;
    private Byte number;
    private String name;
    private WorkoutDay workoutDay;

    private List<WorkoutSet> workoutSets = new ArrayList<>();
    private List<WorkoutSet> prevWorkoutSets = new ArrayList<>();


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
