package com.mykhailotiutiun.repcounterbot.services;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutExerciseRepository;
import com.mykhailotiutiun.repcounterbot.service.Impl.WorkoutExerciseServiceImpl;
import com.mykhailotiutiun.repcounterbot.service.WorkoutSetService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkoutExerciseServiceImplTest {

    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;
    @Mock
    private WorkoutSetService workoutSetService;
    @InjectMocks
    private WorkoutExerciseServiceImpl workoutExerciseService;

    @Test
    public void getById(){
        Long id = 1L;
        WorkoutExercise workoutExercise = WorkoutExercise.builder().id(id).build();
        workoutExercise.setWorkoutSets(new ArrayList<>(List.of(WorkoutSet.builder().number(2).build(), WorkoutSet.builder().number(1).build())));
        when(workoutExerciseRepository.findById(id)).thenReturn(Optional.of(workoutExercise));
        assertEquals(workoutExercise, workoutExerciseService.getById(id));
        assertEquals(1, (int) workoutExercise.getWorkoutSets().get(0).getNumber());
        assertEquals(2, (int) workoutExercise.getWorkoutSets().get(1).getNumber());


        when(workoutExerciseRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            workoutExerciseService.getById(id);
        });
    }

    @Test
    public void getLatestNumberByWorkoutDay(){
        WorkoutExercise workoutExercise = WorkoutExercise.builder().id(1L).number((byte) 1).build();
        WorkoutDay workoutDay = WorkoutDay.builder().id(2L).build();
        when(workoutExerciseRepository.findAllByWorkoutDayOrderByNumberDesc(workoutDay)).thenReturn(List.of(workoutExercise));
        assertEquals(workoutExercise.getNumber(), workoutExerciseService.getLatestNumberByWorkoutDay(workoutDay));

        when(workoutExerciseRepository.findAllByWorkoutDayOrderByNumberDesc(workoutDay)).thenReturn(new ArrayList<>());
        assertEquals(Byte.valueOf((byte) 0), workoutExerciseService.getLatestNumberByWorkoutDay(workoutDay));
    }

    @Test
    public void getByNumberAndWorkoutDay(){
        WorkoutExercise workoutExercise = WorkoutExercise.builder().id(1L).number((byte) 1).build();
        WorkoutDay workoutDay = WorkoutDay.builder().id(2L).build();
        when(workoutExerciseRepository.findByNumberAndWorkoutDay(workoutExercise.getNumber(), workoutDay)).thenReturn(Optional.of(workoutExercise));
        assertEquals(workoutExercise, workoutExerciseService.getByNumberAndWorkoutDay((byte) 1, workoutDay));

        when(workoutExerciseRepository.findByNumberAndWorkoutDay(workoutExercise.getNumber(), workoutDay)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            workoutExerciseService.getByNumberAndWorkoutDay((byte) 1, workoutDay);
        });
    }

    @Test
    public void getAllByWorkoutDay(){
        WorkoutExercise workoutExercise = WorkoutExercise.builder().id(1L).number((byte) 1).build();
        WorkoutDay workoutDay = WorkoutDay.builder().id(2L).build();
        when(workoutExerciseRepository.findAllByWorkoutDayOrderByNumberDesc(workoutDay)).thenReturn(List.of(workoutExercise));
        assertEquals(workoutExercise, workoutExerciseService.getAllByWorkoutDay(workoutDay).get(0));
    }

    @Test
    public void create(){
        WorkoutExercise workoutExercise = WorkoutExercise.builder().id(1L).build();
        WorkoutDay workoutDay = WorkoutDay.builder().id(2L).build();
        workoutExercise.setWorkoutDay(workoutDay);
        when(workoutExerciseRepository.findAllByWorkoutDayOrderByNumberDesc(workoutDay)).thenReturn(List.of(WorkoutExercise.builder().number((byte) 1).build()));
        workoutExerciseService.create(workoutExercise);
        assertEquals(Byte.valueOf((byte) 2),  workoutExercise.getNumber());
        verify(workoutExerciseRepository).save(workoutExercise);
    }

    @Test
    public void createAllFromOldWorkoutDay(){
        WorkoutDay workoutDay = WorkoutDay.builder().id(1L).build();
        WorkoutDay newWorkoutDay = WorkoutDay.builder().id(2L).build();
        List<WorkoutExercise> oldWorkoutExercises = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            oldWorkoutExercises.add(WorkoutExercise.builder().id((long) (i + 1)).build());
        }
        when(workoutExerciseRepository.findAllByWorkoutDayOrderByNumberDesc(workoutDay)).thenReturn(oldWorkoutExercises);
        workoutExerciseService.createAllFromOldWorkoutDay(workoutDay, newWorkoutDay);
        verify(workoutExerciseRepository, times(7)).save(any());
    }

    @Test
    public void setName(){
        Long id = 1L;
        String name = "Name";
        WorkoutExercise workoutExercise = WorkoutExercise.builder().id(id).workoutSets(new ArrayList<>()).build();
        when(workoutExerciseRepository.findById(id)).thenReturn(Optional.of(workoutExercise));
        workoutExerciseService.setName(id, name);
        verify(workoutExerciseRepository).save(workoutExercise);
        Assertions.assertEquals(workoutExercise.getName(), name);
    }

    @Test
    public void moveUp(){
        Long id = 1L;
        WorkoutExercise workoutExercise = WorkoutExercise.builder().id(id).number((byte) 2).workoutSets(new ArrayList<>()).build();
        WorkoutExercise upperWorkoutExercise = WorkoutExercise.builder().id(id).number((byte) 1).workoutSets(new ArrayList<>()).build();
        when(workoutExerciseRepository.findById(workoutExercise.getId())).thenReturn(Optional.of(workoutExercise));
        when(workoutExerciseRepository.findByNumberAndWorkoutDay(eq((byte) 1), any())).thenReturn(Optional.of(upperWorkoutExercise));
        workoutExerciseService.moveUp(id);
        verify(workoutExerciseRepository).save(workoutExercise);
        verify(workoutExerciseRepository).save(upperWorkoutExercise);
        assertEquals(Byte.valueOf((byte) 1), workoutExercise.getNumber());
        assertEquals(Byte.valueOf((byte) 2), upperWorkoutExercise.getNumber());

        when(workoutExerciseRepository.findById(workoutExercise.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            workoutExerciseService.moveUp(id);
        });

        when(workoutExerciseRepository.findById(workoutExercise.getId())).thenReturn(Optional.of(workoutExercise));
        assertThrows(EntityNotFoundException.class, () -> {
            workoutExerciseService.moveUp(id);
        });
    }

    @Test
    public void moveDown(){
        Long s = 1L;
        WorkoutExercise workoutExercise = WorkoutExercise.builder().id(s).number((byte) 1).workoutSets(new ArrayList<>()).build();
        WorkoutExercise lowerWorkoutExercise = WorkoutExercise.builder().id(s).number((byte) 2).workoutSets(new ArrayList<>()).build();
        when(workoutExerciseRepository.findById(workoutExercise.getId())).thenReturn(Optional.of(workoutExercise));
        when(workoutExerciseRepository.findByNumberAndWorkoutDay(eq((byte) 2), any())).thenReturn(Optional.of(lowerWorkoutExercise));
        workoutExerciseService.moveDown(s);
        verify(workoutExerciseRepository).save(workoutExercise);
        verify(workoutExerciseRepository).save(lowerWorkoutExercise);
        assertEquals(Byte.valueOf((byte) 2), workoutExercise.getNumber());
        assertEquals(Byte.valueOf((byte) 1), lowerWorkoutExercise.getNumber());

        when(workoutExerciseRepository.findById(workoutExercise.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            workoutExerciseService.moveDown(s);
        });

        when(workoutExerciseRepository.findById(workoutExercise.getId())).thenReturn(Optional.of(workoutExercise));
        assertThrows(EntityNotFoundException.class, () -> {
            workoutExerciseService.moveDown(s);
        });
    }

    @Test
    public void addSets(){
        Long id = 1L;
        WorkoutExercise workoutExercise = WorkoutExercise.builder().id(id).workoutSets(new ArrayList<>()).build();
        List<WorkoutSet> workoutSets = List.of(WorkoutSet.builder().id(id).build(), WorkoutSet.builder().id(2L).build());
        when(workoutExerciseRepository.findById(id)).thenReturn(Optional.of(workoutExercise));
        workoutExerciseService.addSets(id, workoutSets);
        verify(workoutSetService, times(2)).save(any());
        verify(workoutExerciseRepository).save(workoutExercise);
        assertEquals(workoutSets, workoutExercise.getWorkoutSets());
    }

    @Test
    public void deleteById(){
        long id = 1L;
        workoutExerciseService.deleteById(id);
        verify(workoutExerciseRepository).deleteById(id);
    }
}
