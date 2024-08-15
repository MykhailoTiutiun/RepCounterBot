package com.mykhailotiutiun.repcounterbot.repository;

import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;

import java.util.List;
import java.util.Optional;

public interface WorkoutExerciseRepository {

    Optional<WorkoutExercise> findById(Long id);
    Optional<WorkoutExercise> findByNumberAndWorkoutDay(Byte number, WorkoutDay workoutDay);
    List<WorkoutExercise> findAllByWorkoutDayOrderByNumberDesc(WorkoutDay workoutDay);

    WorkoutExercise save(WorkoutExercise workoutExercise);

    void deleteById(Long id);

}
