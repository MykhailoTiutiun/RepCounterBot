package com.mykhailotiutiun.repcounterbot.service;


import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutWeekRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class WorkoutWeekService {

    private final WorkoutWeekRepository workoutWeekRepository;
    private final WorkoutDayService workoutDayService;
    private final LocalDateWeekService localDateWeekService;


    public WorkoutWeekService(WorkoutWeekRepository workoutWeekRepository, WorkoutDayService workoutDayService, LocalDateWeekService localDateWeekService) {
        this.workoutWeekRepository = workoutWeekRepository;
        this.workoutDayService = workoutDayService;
        this.localDateWeekService = localDateWeekService;
    }

    public WorkoutWeek getWorkoutWeekById(String id){
        log.trace("Get WorkoutWeek with id: {}", id);
        return workoutWeekRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public WorkoutWeek getWorkoutWeekByUserIdAndLocalDate(Long userId, LocalDate localDate) {
        log.trace("Get WorkoutWeek with user-id {} and local-date {}", userId, localDate.toString());
        return workoutWeekRepository.findByUserIdAndWeekStartDate(userId, localDateWeekService.getFirstDateOfWeekFromDate(localDate)).orElseThrow(EntityNotFoundException::new);
    }

    public List<WorkoutWeek> getAllWorkoutWeeks(){
        log.trace("Get all WorkoutWeeks");
        return workoutWeekRepository.findAll();
    }

    public void create(WorkoutWeek workoutWeek) throws EntityAlreadyExistsException {
        log.trace("Create WorkoutWeek: {}", workoutWeek);
        save(workoutWeek);

        for(int i = 0; i < 7; i++){
            workoutDayService.save(new WorkoutDay(workoutWeek, null, workoutWeek.getWeekStartDate().plusDays(i), null));
        }
    }

    public void save(WorkoutWeek workoutWeek){
        log.trace("Save WorkoutWeek: {}", workoutWeek);
        workoutWeekRepository.save(workoutWeek);
    }

    public void deleteById(String id){
        log.trace("Delete WorkoutWeek with id: {}", id);
        workoutWeekRepository.deleteById(id);
    }
}
