package com.mykhailotiutiun.repcounterbot.cache.impl;

import com.mykhailotiutiun.repcounterbot.cache.SelectedWorkoutExerciseCache;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SelectedWorkoutExerciseCacheImpl implements SelectedWorkoutExerciseCache {

    private final Map<String, String> selectedWorkoutExercises = new HashMap<>();


    @Override
    public void setSelectedWorkoutExercise(String chatId, String workoutExerciseId) {
        selectedWorkoutExercises.put(chatId, workoutExerciseId);
    }

    @Override
    public String getSelectedWorkoutExercise(String chatId) {
        return selectedWorkoutExercises.get(chatId);
    }

}
