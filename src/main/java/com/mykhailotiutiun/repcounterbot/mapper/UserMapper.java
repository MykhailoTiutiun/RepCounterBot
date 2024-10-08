package com.mykhailotiutiun.repcounterbot.mapper;

import com.mykhailotiutiun.repcounterbot.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .localTag(rs.getString("local_tag"))
                .build();
    }
}
