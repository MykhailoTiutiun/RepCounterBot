package com.mykhailotiutiun.repcounterbot.cache;

public interface SelectedWorkoutDayCache {

    void setSelectedWorkoutDay(String chatId, String workoutDayId);

    String getSelectedWorkoutDay(String chatId);
}
