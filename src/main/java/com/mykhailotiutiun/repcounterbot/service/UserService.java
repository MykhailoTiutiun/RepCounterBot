package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.model.User;

import java.util.List;

public interface UserService {
    User getUserById(Long id);

    List<User> getAllUsers();

    void create(User user) throws EntityAlreadyExistsException;

    void save(User user);

    void setUserLang(String userId, String localTag);

    void deleteById(Long id);
}
