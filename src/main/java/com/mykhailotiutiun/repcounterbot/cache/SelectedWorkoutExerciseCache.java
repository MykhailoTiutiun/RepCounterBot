package com.mykhailotiutiun.repcounterbot.cache;


public interface SelectedWorkoutExerciseCache {

    void setSelectedWorkoutExercise(String chatId, String workoutExerciseId);

    String getSelectedWorkoutExercise(String chatId);
}
