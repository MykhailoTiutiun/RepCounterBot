package com.mykhailotiutiun.repcounterbot.repository;

import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkoutWeekRepository extends MongoRepository<WorkoutWeek, String> {

    Optional<WorkoutWeek> findByUserIdAndWeekStartDate(Long userId, LocalDate localDate);
}
