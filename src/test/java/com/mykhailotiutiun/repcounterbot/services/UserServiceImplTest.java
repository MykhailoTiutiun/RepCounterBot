package com.mykhailotiutiun.repcounterbot.services;

import com.mykhailotiutiun.repcounterbot.cache.SelectedLanguageCache;
import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.UserRepository;
import com.mykhailotiutiun.repcounterbot.service.Impl.UserServiceImpl;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import com.mykhailotiutiun.repcounterbot.util.LocalDateWeekUtil;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private WorkoutWeekService workoutWeekService;
    @Mock
    private LocalDateWeekUtil localDateWeekUtil;
    @Mock
    private SelectedLanguageCache selectedLanguageCache;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User expectedUser = User.builder().id(userId).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.getById(userId);
        assertEquals(expectedUser, actualUser);

        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getById(2L);
        });
    }

    @Test
    public void testCreateUser() {
        User newUser = User.builder().id(1L).build();
        when(userRepository.existsById(newUser.getId())).thenReturn(false);
        userService.create(newUser);

        verify(userRepository).save(newUser);
        verify(workoutWeekService).create(any(WorkoutWeek.class));

        when(userRepository.existsById(newUser.getId())).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            userService.create(newUser);
        });
    }

    @Test
    public void testSetUserLang() {
        Long userId = 3L;
        String localTag = "en_US";

        User user = User.builder().id(1L).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.setUserLang(userId, localTag);

        verify(userRepository).save(user);
        verify(selectedLanguageCache).setSelectedLanguage(String.valueOf(userId), localTag);
    }
}