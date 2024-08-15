package com.mykhailotiutiun.repcounterbot.repository;

import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;

import java.util.Optional;

public interface WorkoutWeekRepository {

    Optional<WorkoutWeek> findById(Long id);
    Optional<WorkoutWeek> findByUserIdAndCurrent(Long userId, Boolean isCurrent);

    WorkoutWeek save(WorkoutWeek workoutWeek);
}
