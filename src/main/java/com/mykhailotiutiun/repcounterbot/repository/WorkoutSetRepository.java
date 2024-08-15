package com.mykhailotiutiun.repcounterbot.repository;

import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;

import java.util.List;

public interface WorkoutSetRepository {

    List<WorkoutSet> findAllByWorkoutExercise(WorkoutExercise workoutExercise);
    List<WorkoutSet> findAllPrevSetsByWorkoutExercise(WorkoutExercise workoutExercise);
    WorkoutSet save(WorkoutSet workoutSet);
}
