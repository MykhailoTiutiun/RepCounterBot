package com.mykhailotiutiun.repcounterbot.service.Impl;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutSetRepository;
import com.mykhailotiutiun.repcounterbot.service.WorkoutSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkoutSetServiceImpl implements WorkoutSetService {

    private final WorkoutSetRepository workoutSetRepository;

    public WorkoutSetServiceImpl(WorkoutSetRepository workoutSetRepository) {
        this.workoutSetRepository = workoutSetRepository;
    }

    @Override
    public WorkoutSet getWorkoutSetById(String id) {
        return workoutSetRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void save(WorkoutSet workoutSet) {
        workoutSetRepository.save(workoutSet);
    }

    @Override
    public void deleteById(String id) {
        workoutSetRepository.deleteById(id);
    }
}
