package com.mykhailotiutiun.repcounterbot.mapper;

import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkoutDayMapper implements RowMapper<WorkoutDay> {
    @Override
    public WorkoutDay mapRow(ResultSet rs, int rowNum) throws SQLException {
        return WorkoutDay.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .isWorkoutDay(rs.getBoolean("is_workout_day"))
                .date(rs.getDate("date").toLocalDate())
                .workoutWeek(WorkoutWeek.builder().id(rs.getLong("workout_week_id")).build())
                .build();
    }
}
