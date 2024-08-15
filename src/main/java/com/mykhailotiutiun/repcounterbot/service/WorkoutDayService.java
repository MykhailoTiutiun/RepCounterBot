package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;

import java.util.List;

public interface WorkoutDayService {
    WorkoutDay getById(Long id);
    List<WorkoutDay> getAllByWorkoutWeek(WorkoutWeek workoutWeek);

    void createAllFromOldWorkoutWeek(WorkoutWeek oldWorkoutWeek, WorkoutWeek newWorkoutWeek);
    void save(WorkoutDay workoutDay);
    void setName(Long workoutDayId, String name);
    void setRestWorkoutDay(Long workoutDayId);

}
