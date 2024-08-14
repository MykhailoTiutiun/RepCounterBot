package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

public interface WorkoutExerciseService {
    WorkoutExercise getById(String id);
    Byte getLatestNumberByWorkoutDay(WorkoutDay workoutDay);
    WorkoutExercise getByNumberAndWorkoutDay(Byte number, WorkoutDay workoutDay);
    List<WorkoutExercise> getAllByWorkoutDay(WorkoutDay workoutDay);

    void create(WorkoutExercise workoutExercise);
    void createAllFromOldWorkoutDay(WorkoutDay oldWorkoutDay, WorkoutDay newWorkoutDay);

    void setName(String workoutExerciseId, String name);
    void moveUp(String workoutExerciseId);
    void moveDown(String workoutExerciseId);
    void addSets(String workoutExerciseId, List<WorkoutSet> workoutSets);
    void deleteById(String id);

}
