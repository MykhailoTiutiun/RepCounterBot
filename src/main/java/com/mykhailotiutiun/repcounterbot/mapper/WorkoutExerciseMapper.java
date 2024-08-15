package com.mykhailotiutiun.repcounterbot.mapper;

import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WorkoutExerciseMapper implements RowMapper<WorkoutExercise> {
    @Override
    public WorkoutExercise mapRow(ResultSet rs, int rowNum) throws SQLException {
        return WorkoutExercise.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .number(rs.getByte("number"))
                .workoutDay(WorkoutDay.builder().id(rs.getLong("workout_day_id")).build())
                .workoutSets(new ArrayList<>())
                .prevWorkoutSets(new ArrayList<>())
                .build();
    }
}
