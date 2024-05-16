package com.mykhailotiutiun.repcounterbot.services;

import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutWeekRepository;
import com.mykhailotiutiun.repcounterbot.service.Impl.WorkoutWeekServiceImpl;
import com.mykhailotiutiun.repcounterbot.service.LocalDateWeekService;
import com.mykhailotiutiun.repcounterbot.service.LocaleMessageService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkoutWeekServiceImplTest {

    @Mock
    private WorkoutWeekRepository workoutWeekRepository;

    @Mock
    private WorkoutDayService workoutDayService;

    @Mock
    private LocalDateWeekService localDateWeekService;

    @Mock
    private LocaleMessageService localeMessageService;

    @InjectMocks
    private WorkoutWeekServiceImpl workoutWeekService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWorkoutWeekById() {
        String workoutWeekId = "1";
        WorkoutWeek expectedWorkoutWeek = new WorkoutWeek(workoutWeekId, new User(1L, "John"), true, LocalDate.of(20024, 4, 14), LocalDate.of(20024, 4, 20));

        when(workoutWeekRepository.findById(workoutWeekId)).thenReturn(Optional.of(expectedWorkoutWeek));

        WorkoutWeek actualWorkoutWeek = workoutWeekService.getWorkoutWeekById(workoutWeekId);

        assertEquals(expectedWorkoutWeek, actualWorkoutWeek);
    }

    @Test
    public void testGetCurrentWorkoutWeekByUserId() {
        Long userId = 123L;
        WorkoutWeek expectedWorkoutWeek = new WorkoutWeek("2", new User(userId, "John"), true, LocalDate.of(20024, 4, 14), LocalDate.of(20024, 4, 20));

        when(workoutWeekRepository.findByUserIdAndCurrent(userId, true)).thenReturn(Optional.of(expectedWorkoutWeek));
        when(localDateWeekService.isCurrentWeek(any(), any())).thenReturn(true);

        WorkoutWeek actualWorkoutWeek = workoutWeekService.getCurrentWorkoutWeekByUserId(userId);

        assertEquals(expectedWorkoutWeek, actualWorkoutWeek);
    }

    @Test
    public void testCreateWorkoutWeek() {
        WorkoutWeek newWorkoutWeek = new WorkoutWeek("3", new User(1L, "John"), true, LocalDate.of(20024, 4, 14), LocalDate.of(20024, 4, 20));

        when(workoutWeekRepository.existsById(newWorkoutWeek.getId())).thenReturn(false);

        workoutWeekService.create(newWorkoutWeek);

        verify(workoutWeekRepository).save(newWorkoutWeek);
        verify(workoutDayService, times(7)).save(any(WorkoutDay.class));
    }

    @Test
    public void testCreateFromOldWorkoutWeek() {
        WorkoutWeek oldWorkoutWeek = new WorkoutWeek("4", new User(142L, "John"), true, LocalDate.of(20024, 4, 14), LocalDate.of(20024, 4, 20));

        when(localDateWeekService.getFirstDateOfWeekFromDate(any())).thenReturn(LocalDate.now());
        when(localDateWeekService.getLastDateOfWeekFromDate(any())).thenReturn(LocalDate.now());

        workoutWeekService.create(oldWorkoutWeek);
        workoutWeekService.createFromOldWorkoutWeek(oldWorkoutWeek);

        verify(workoutWeekRepository, times(3)).save(any(WorkoutWeek.class));
    }

    @Test
    public void testDeleteWorkoutWeekById() {
        String workoutWeekId = "6";

        workoutWeekService.deleteById(workoutWeekId);

        verify(workoutWeekRepository).deleteById(workoutWeekId);
    }

}