package com.mykhailotiutiun.repcounterbot.repository.impl;

import com.mykhailotiutiun.repcounterbot.mapper.UserMapper;
import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM bot_users WHERE id = ?", new UserMapper(), id));
    }

    @Override
    public boolean existsById(Long id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT id FROM bot_users WHERE id = ?)", Boolean.class, id));
    }

    @Override
    public User save(User user) {
        if (existsById(user.getId())) {
            jdbcTemplate.update("UPDATE bot_users SET username = ?, local_tag = ? WHERE id = ?", user.getUsername(), user.getLocalTag(), user.getId());
        } else {
            jdbcTemplate.update("INSERT INTO bot_users (id, username, local_tag) VALUES (?, ?, ?)", user.getId(), user.getUsername(), user.getLocalTag());
        }
        return user;
    }
}