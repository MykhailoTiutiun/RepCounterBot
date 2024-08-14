package com.mykhailotiutiun.repcounterbot.services;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutWeekRepository;
import com.mykhailotiutiun.repcounterbot.service.Impl.WorkoutWeekServiceImpl;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.util.LocalDateWeekUtil;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkoutWeekServiceImplTest {

    @Mock
    private WorkoutWeekRepository workoutWeekRepository;

    @Mock
    private WorkoutDayService workoutDayService;

    @Mock
    private LocalDateWeekUtil localDateWeekUtil;

    @Mock
    private LocaleMessageUtil localeMessageUtil;

    @InjectMocks
    private WorkoutWeekServiceImpl workoutWeekService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetCurrentWorkoutWeekByUserId() {
        Long userId = 1L;

        WorkoutWeek expectedWorkoutWeek = WorkoutWeek.builder().id("2").user(User.builder().id(userId).build()).weekEndDate(LocalDate.now()).build();
        when(workoutWeekRepository.findByUserIdAndCurrent(userId, true)).thenReturn(Optional.of(expectedWorkoutWeek));
        when(localDateWeekUtil.isCurrentWeek(any(), any())).thenReturn(true);
        WorkoutWeek actualWorkoutWeek = workoutWeekService.getCurrentWorkoutWeekByUserId(userId);
        assertEquals(expectedWorkoutWeek, actualWorkoutWeek);

        when(workoutWeekRepository.findByUserIdAndCurrent(userId, true)).thenReturn(Optional.of(expectedWorkoutWeek));
        when(localDateWeekUtil.isCurrentWeek(any(), any())).thenReturn(false);
        workoutWeekService.getCurrentWorkoutWeekByUserId(userId);
        verify(workoutWeekRepository, times(2)).save(any());
        verify(workoutDayService).createAllFromOldWorkoutWeek(eq(expectedWorkoutWeek), any());

        when(workoutWeekRepository.findByUserIdAndCurrent(userId, true)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            workoutWeekService.getCurrentWorkoutWeekByUserId(userId);
        });
    }

    @Test
    public void testCreateWorkoutWeek() {
        WorkoutWeek newWorkoutWeek = WorkoutWeek.builder().id("1").weekStartDate(LocalDate.now()).build();

        workoutWeekService.create(newWorkoutWeek);

        verify(workoutWeekRepository).save(newWorkoutWeek);
        verify(workoutDayService, times(7)).save(any(WorkoutDay.class));
    }

}