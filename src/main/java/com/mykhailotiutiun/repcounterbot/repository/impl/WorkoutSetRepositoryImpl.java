package com.mykhailotiutiun.repcounterbot.repository.impl;

import com.mykhailotiutiun.repcounterbot.mapper.WorkoutSetMapper;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutSetRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WorkoutSetRepositoryImpl implements WorkoutSetRepository {

    private final JdbcTemplate jdbcTemplate;

    public WorkoutSetRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private boolean existsById(Long id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM workout_sets WHERE id = ?)", Boolean.class, id));
    }

    @Override
    public List<WorkoutSet> findAllByWorkoutExercise(WorkoutExercise workoutExercise) {
        return jdbcTemplate.query("SELECT * FROM workout_sets WHERE workout_ex_id = ?", new WorkoutSetMapper(), workoutExercise.getId());
    }

    @Override
    public List<WorkoutSet> findAllPrevSetsByWorkoutExercise(WorkoutExercise workoutExercise) {
        return jdbcTemplate.query("SELECT * FROM workout_sets WHERE prev_workout_ex_id = ?", new WorkoutSetMapper(), workoutExercise.getId());
    }

    @Override
    public WorkoutSet save(WorkoutSet workoutSet) {
        if(existsById(workoutSet.getId())) {
            jdbcTemplate.update("UPDATE workout_sets SET number = ?, weight = ?, reps = ? WHERE id = ?",
                    workoutSet.getNumber(),
                    workoutSet.getWeight(),
                    workoutSet.getReps(),
                    workoutSet.getId());
        } else {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("workout_sets").usingGeneratedKeyColumns("id");
            Map<String, Object> parameters = new HashMap<>(3);
            parameters.put("number", workoutSet.getNumber());
            parameters.put("reps", workoutSet.getReps());
            parameters.put("weight", workoutSet.getWeight());
            workoutSet.setId((Long) simpleJdbcInsert.executeAndReturnKey(parameters));
        }
        return workoutSet;
    }
}
