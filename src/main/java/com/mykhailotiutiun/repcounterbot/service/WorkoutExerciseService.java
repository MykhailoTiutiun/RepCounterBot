package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

public interface WorkoutExerciseService {
    WorkoutExercise getWorkoutExerciseById(String id);

    Byte getLatestWorkoutExerciseNumberByWorkoutDay(WorkoutDay workoutDay);

    List<WorkoutExercise> getWorkoutExerciseByWorkoutDay(WorkoutDay workoutDay);

    List<WorkoutExercise> getAllWorkoutExercises();

    void create(WorkoutExercise workoutExercise);

    void createAllFromOldWorkoutDay(WorkoutDay oldWorkoutDay, WorkoutDay newWorkoutDay);

    void save(WorkoutExercise workoutExercise);

    void addSetsToWorkoutExercise(String workoutExerciseId, List<WorkoutSet> workoutSets);

    void deleteById(String id);

    SendMessage getWorkoutExerciseSendMessage(String chatId, String workoutExerciseId);
    EditMessageText getWorkoutExerciseEditMessage(String chatId, Integer messageId, String workoutExerciseId);
}
