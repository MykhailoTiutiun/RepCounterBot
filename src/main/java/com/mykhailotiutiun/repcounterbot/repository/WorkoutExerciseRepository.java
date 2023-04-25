package com.mykhailotiutiun.repcounterbot.repository;

import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkoutExerciseRepository extends MongoRepository<WorkoutExercise, String> {

    List<WorkoutExercise> findAllByWorkoutDayOrderByNumberDesc(WorkoutDay workoutDay);

    List<WorkoutExercise> findAllByWorkoutDay(WorkoutDay workoutDay);

}
