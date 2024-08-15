package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;

import java.util.List;

public interface WorkoutExerciseService {
    WorkoutExercise getById(Long id);
    Byte getLatestNumberByWorkoutDay(WorkoutDay workoutDay);
    WorkoutExercise getByNumberAndWorkoutDay(Byte number, WorkoutDay workoutDay);
    List<WorkoutExercise> getAllByWorkoutDay(WorkoutDay workoutDay);

    void create(WorkoutExercise workoutExercise);
    void createAllFromOldWorkoutDay(WorkoutDay oldWorkoutDay, WorkoutDay newWorkoutDay);

    void setName(Long workoutExerciseId, String name);
    void moveUp(Long workoutExerciseId);
    void moveDown(Long workoutExerciseId);
    void addSets(Long workoutExerciseId, List<WorkoutSet> workoutSets);
    void deleteById(Long id);

}
