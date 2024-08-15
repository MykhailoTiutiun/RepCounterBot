package com.mykhailotiutiun.repcounterbot.mapper;

import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkoutSetMapper implements RowMapper<WorkoutSet> {
    @Override
    public WorkoutSet mapRow(ResultSet rs, int rowNum) throws SQLException {
        return WorkoutSet.builder()
                .id(rs.getLong("id"))
                .number(rs.getInt("number"))
                .weight(rs.getInt("weight"))
                .reps(rs.getInt("reps"))
                .build();
    }
}
