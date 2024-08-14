package com.mykhailotiutiun.repcounterbot.services;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutDayRepository;
import com.mykhailotiutiun.repcounterbot.service.Impl.WorkoutDayServiceImpl;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkoutDayServiceImplTest {

    @Mock
    private WorkoutDayRepository workoutDayRepository;
    @Mock
    private WorkoutExerciseService workoutExerciseService;
    @InjectMocks
    private WorkoutDayServiceImpl workoutDayService;

    @Test
    public void testGetWorkoutDayById(){
        WorkoutDay workoutDay = WorkoutDay.builder().id("1").build();
        when(workoutDayRepository.findById("1")).thenReturn(Optional.of(workoutDay));
        assertEquals(workoutDay, workoutDayService.getById("1"));

        when(workoutDayRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            workoutDayService.getById("1");
        });
    }

    @Test
    public void testGetAllByWorkoutWeek(){
        WorkoutWeek workoutWeek = WorkoutWeek.builder().id("1").build();

        when(workoutDayRepository.findAllByWorkoutWeek(workoutWeek)).thenReturn(List.of(new WorkoutDay()));
        assertNotNull(workoutDayService.getAllByWorkoutWeek(workoutWeek).get(0));
    }

    @Test
    public void testCreateAllFromOldWorkoutWeek(){
        WorkoutWeek workoutWeek = WorkoutWeek.builder().id("1").build();
        WorkoutWeek newWorkoutWeek = WorkoutWeek.builder().id("2").weekStartDate(LocalDate.now()).build();
        List<WorkoutDay> oldWorkoutDays = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            oldWorkoutDays.add(WorkoutDay.builder().id(String.valueOf(i + 1)).build());
        }
        when(workoutDayRepository.findAllByWorkoutWeek(workoutWeek)).thenReturn(oldWorkoutDays);
        workoutDayService.createAllFromOldWorkoutWeek(workoutWeek, newWorkoutWeek);
        verify(workoutDayRepository, times(7)).save(any());
        verify(workoutExerciseService, times(7)).createAllFromOldWorkoutDay(any(), any());
    }

    @Test
    public void testSave(){
        WorkoutDay workoutDay = WorkoutDay.builder().id("1").build();
        workoutDayService.save(workoutDay);
        verify(workoutDayRepository).save(workoutDay);
    }

    @Test
    public void setWorkoutDayName(){
        WorkoutDay workoutDay = WorkoutDay.builder().id("1").build();
        when(workoutDayRepository.findById("1")).thenReturn(Optional.of(workoutDay));
        workoutDayService.setName("1", "Name");
        verify(workoutDayRepository).save(workoutDay);
        assertEquals(workoutDay.getName(), "Name");
        assertTrue(workoutDay.getIsWorkoutDay());
    }

    @Test
    public void setRestWorkoutDay(){
        WorkoutDay workoutDay = WorkoutDay.builder().id("1").build();
        when(workoutDayRepository.findById("1")).thenReturn(Optional.of(workoutDay));
        workoutDayService.setRestWorkoutDay("1");
        verify(workoutDayRepository).save(workoutDay);
        assertFalse(workoutDay.getIsWorkoutDay());
    }

}
