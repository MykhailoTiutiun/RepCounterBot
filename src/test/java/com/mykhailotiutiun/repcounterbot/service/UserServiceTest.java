package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.service.UserService;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;


@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    private final Random random = new Random();
    private User user;


    @BeforeEach
    public void createUser(){
        user = new User(random.nextLong(), "TestUser");
        userService.save(user);
    }

    @Test
    void saveGetAndDeleteUserTest() {
        assertEquals(user.getUsername(), userService.getUserById(user.getId()).getUsername());
    }

    @AfterEach
    public void deleteUser(){
        userService.deleteById(user.getId());
        user = null;
    }
}
