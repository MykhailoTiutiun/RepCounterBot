package com.mykhailotiutiun.repcounterbot.repository;

import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkoutSetRepository extends MongoRepository<WorkoutSet, String> {
}
