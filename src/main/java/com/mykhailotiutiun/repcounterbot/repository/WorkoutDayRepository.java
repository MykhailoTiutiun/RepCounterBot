package com.mykhailotiutiun.repcounterbot.repository;

import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkoutDayRepository extends MongoRepository<WorkoutDay, String> {

    List<WorkoutDay> findAllByWorkoutWeek(WorkoutWeek WorkoutWeek);
}
