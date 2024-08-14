package com.mykhailotiutiun.repcounterbot.services;

import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutSetRepository;
import com.mykhailotiutiun.repcounterbot.service.Impl.WorkoutSetServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WorkoutSetsServiceImplTest {

    @Mock
    private WorkoutSetRepository workoutSetRepository;
    @InjectMocks
    private WorkoutSetServiceImpl workoutSetService;

    @Test
    public void save(){
        WorkoutSet workoutSet = WorkoutSet.builder().id("1").build();
        workoutSetService.save(workoutSet);
        verify(workoutSetRepository).save(workoutSet);
    }
}
