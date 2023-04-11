package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutDayRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class WorkoutDayService {

    private final WorkoutDayRepository workoutDayRepository;

    public WorkoutDayService(WorkoutDayRepository workoutDayRepository) {
        this.workoutDayRepository = workoutDayRepository;
    }

    public WorkoutDay getWorkoutDayById(String id){
        log.trace("Get WorkoutDay with id: {}", id);
        return workoutDayRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<WorkoutDay> getWorkoutDaysByWorkoutWeekId(WorkoutWeek workoutWeek) {
        log.trace("Get WorkoutDays by WorkoutWeek: {}", workoutWeek);
        return workoutDayRepository.findAllByWorkoutWeek(workoutWeek);
    }

    public List<WorkoutDay> getAllWorkoutDays(){
        log.trace("Get all WorkoutDays");
        return workoutDayRepository.findAll();
    }


    public void save(WorkoutDay workoutWeek){
        log.trace("Save WorkoutDay: {}", workoutWeek);
        workoutDayRepository.save(workoutWeek);
    }

    public void deleteById(String id){
        log.trace("Delete WorkoutDay with id: {}", id);
        workoutDayRepository.deleteById(id);
    }
}
