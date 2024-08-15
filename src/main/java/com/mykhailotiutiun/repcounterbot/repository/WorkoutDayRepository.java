package com.mykhailotiutiun.repcounterbot.repository;

import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;

import java.util.List;
import java.util.Optional;

public interface WorkoutDayRepository {

    Optional<WorkoutDay> findById(Long id);
    List<WorkoutDay> findAllByWorkoutWeek(WorkoutWeek workoutWeek);

    WorkoutDay save(WorkoutDay workoutDay);
}
