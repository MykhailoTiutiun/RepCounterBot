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

import static org.junit.Assert.*;
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
        long id = 1L;
        WorkoutDay workoutDay = WorkoutDay.builder().id(id).build();
        when(workoutDayRepository.findById(id)).thenReturn(Optional.of(workoutDay));
        assertEquals(workoutDay, workoutDayService.getById(id));

        when(workoutDayRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            workoutDayService.getById(id);
        });
    }

    @Test
    public void testGetAllByWorkoutWeek(){
        WorkoutWeek workoutWeek = WorkoutWeek.builder().id(1L).build();

        List<WorkoutDay> workoutDays = new ArrayList<>();
        for (int i = 7; i > 0; i--){
            workoutDays.add(WorkoutDay.builder().id((long) (i)).date(LocalDate.now().plusDays(i)).build());
        }
        when(workoutDayRepository.findAllByWorkoutWeek(workoutWeek)).thenReturn(workoutDays);
        assertEquals(workoutDays.get(6), workoutDayService.getAllByWorkoutWeek(workoutWeek).get(0));
    }

    @Test
    public void testCreateAllFromOldWorkoutWeek(){
        WorkoutWeek workoutWeek = WorkoutWeek.builder().id(1L).build();
        WorkoutWeek newWorkoutWeek = WorkoutWeek.builder().id(2L).weekStartDate(LocalDate.now()).build();
        List<WorkoutDay> oldWorkoutDays = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            oldWorkoutDays.add(WorkoutDay.builder().id((long) (i + 1)).date(LocalDate.now().plusDays(i)).build());
        }
        when(workoutDayRepository.findAllByWorkoutWeek(workoutWeek)).thenReturn(oldWorkoutDays);
        workoutDayService.createAllFromOldWorkoutWeek(workoutWeek, newWorkoutWeek);
        verify(workoutDayRepository, times(7)).save(any());
        verify(workoutExerciseService, times(7)).createAllFromOldWorkoutDay(any(), any());
    }

    @Test
    public void testSave(){
        WorkoutDay workoutDay = WorkoutDay.builder().id(1L).build();
        workoutDayService.save(workoutDay);
        verify(workoutDayRepository).save(workoutDay);
    }

    @Test
    public void setWorkoutDayName(){
        long id = 1L;
        String name = "Name";
        WorkoutDay workoutDay = WorkoutDay.builder().id(id).build();
        when(workoutDayRepository.findById(id)).thenReturn(Optional.of(workoutDay));
        workoutDayService.setName(id, name);
        verify(workoutDayRepository).save(workoutDay);
        assertEquals(workoutDay.getName(), name);
        assertTrue(workoutDay.getIsWorkoutDay());
    }

    @Test
    public void setRestWorkoutDay(){
        long id = 1L;
        WorkoutDay workoutDay = WorkoutDay.builder().id(id).build();
        when(workoutDayRepository.findById(id)).thenReturn(Optional.of(workoutDay));
        workoutDayService.setRestWorkoutDay(id);
        verify(workoutDayRepository).save(workoutDay);
        assertFalse(workoutDay.getIsWorkoutDay());
    }

}
