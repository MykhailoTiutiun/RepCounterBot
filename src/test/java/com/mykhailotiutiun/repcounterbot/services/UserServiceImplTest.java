package com.mykhailotiutiun.repcounterbot.services;

import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.UserRepository;
import com.mykhailotiutiun.repcounterbot.service.Impl.UserServiceImpl;
import com.mykhailotiutiun.repcounterbot.service.LocalDateWeekService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkoutWeekService workoutWeekService;

    @Mock
    private LocalDateWeekService localDateWeekService;

    @Mock
    private ChatDataCache chatDataCache;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User expectedUser = new User(userId, "John Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUserById(userId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testGetAllUsers() {
        List<User> expectedUsers = Collections.singletonList(new User(1L, "Alice"));

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void testCreateUser() {
        User newUser = new User(2L, "Bob");

        when(userRepository.existsById(newUser.getId())).thenReturn(false);

        userService.create(newUser);

        verify(userRepository).save(newUser);
        verify(workoutWeekService).create(any(WorkoutWeek.class));
    }

    @Test
    public void testSetUserLang() {
        String userId = "3";
        String localTag = "en_US";

        User user = new User(Long.valueOf(userId), "Eva");

        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.of(user));

        userService.setUserLang(userId, localTag);

        verify(userRepository).save(user);
        verify(chatDataCache).setUserSelectedLanguage(userId, localTag);
    }

    @Test
    public void testDeleteUserById() {
        Long userId = 4L;

        userService.deleteById(userId);

        verify(userRepository).deleteById(userId);
    }
}