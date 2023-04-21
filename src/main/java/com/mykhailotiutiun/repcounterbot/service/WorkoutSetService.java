package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;

public interface WorkoutSetService {
    WorkoutSet getWorkoutSetById(String id);

    void save(WorkoutSet workoutSet);

    void deleteById(String id);
}
