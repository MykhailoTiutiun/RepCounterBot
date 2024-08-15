package com.mykhailotiutiun.repcounterbot.cache.impl;

import com.mykhailotiutiun.repcounterbot.cache.SelectedWorkoutDayCache;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SelectedWorkoutDayCacheImpl implements SelectedWorkoutDayCache {


    private final Map<String, String> selectedWorkoutDays = new HashMap<>();


    @Override
    public void setSelectedWorkoutDay(String chatId, String workoutDayId) {
        selectedWorkoutDays.put(chatId, workoutDayId);
    }

    @Override
    public String getSelectedWorkoutDay(String chatId) {
        return selectedWorkoutDays.get(chatId);
    }
}
