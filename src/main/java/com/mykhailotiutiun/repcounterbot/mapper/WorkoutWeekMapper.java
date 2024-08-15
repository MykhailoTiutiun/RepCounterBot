package com.mykhailotiutiun.repcounterbot.mapper;

import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkoutWeekMapper implements RowMapper<WorkoutWeek> {
    @Override
    public WorkoutWeek mapRow(ResultSet rs, int rowNum) throws SQLException {
        return WorkoutWeek.builder()
                .id(rs.getLong("id"))
                .current(rs.getBoolean("current"))
                .weekStartDate(rs.getDate("week_start_date").toLocalDate())
                .weekEndDate(rs.getDate("week_end_date").toLocalDate())
                .user(User.builder().id(rs.getLong("user_id")).build())
                .build();
    }
}
