package com.mykhailotiutiun.repcounterbot.repository;

import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutExerciseRepository extends MongoRepository<WorkoutExercise, String> {

    Optional<WorkoutExercise> findByNumberAndWorkoutDay(Byte number, WorkoutDay workoutDay);
    List<WorkoutExercise> findAllByWorkoutDayOrderByNumberDesc(WorkoutDay workoutDay);

    List<WorkoutExercise> findAllByWorkoutDay(WorkoutDay workoutDay);

}
