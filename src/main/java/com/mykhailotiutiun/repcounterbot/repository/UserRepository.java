package com.mykhailotiutiun.repcounterbot.repository;

import com.mykhailotiutiun.repcounterbot.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long id);
    boolean existsById(Long id);

    User save(User user);
}
