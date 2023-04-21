package com.mykhailotiutiun.repcounterbot.repository;

import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WorkoutWeekRepository extends MongoRepository<WorkoutWeek, String> {

    Optional<WorkoutWeek> findByUserIdAndCurrent(Long userId, Boolean isCurrent);
}
