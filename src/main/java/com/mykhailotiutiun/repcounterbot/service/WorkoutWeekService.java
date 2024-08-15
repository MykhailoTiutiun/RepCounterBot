package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;

public interface WorkoutWeekService {

    WorkoutWeek getCurrentWorkoutWeekByUserId(Long userId);

    void create(WorkoutWeek workoutWeek) throws EntityAlreadyExistsException;
}
