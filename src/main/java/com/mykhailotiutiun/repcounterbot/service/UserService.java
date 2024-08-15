package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.model.User;


public interface UserService {
    User getById(Long id);

    void create(User user) throws EntityAlreadyExistsException;
    void setUserLang(Long userId, String localTag);
}
