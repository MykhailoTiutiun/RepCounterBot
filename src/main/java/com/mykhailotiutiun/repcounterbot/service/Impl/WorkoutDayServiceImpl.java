package com.mykhailotiutiun.repcounterbot.service.Impl;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutDayRepository;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class WorkoutDayServiceImpl implements WorkoutDayService {

    private final WorkoutDayRepository workoutDayRepository;
    private final WorkoutExerciseService workoutExerciseService;

    public WorkoutDayServiceImpl(WorkoutDayRepository workoutDayRepository, WorkoutExerciseService workoutExerciseService) {
        this.workoutDayRepository = workoutDayRepository;
        this.workoutExerciseService = workoutExerciseService;
    }

    @Override
    public WorkoutDay getById(Long id) {
        log.trace("Get WorkoutDay by id: {}", id);
        return workoutDayRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<WorkoutDay> getAllByWorkoutWeek(WorkoutWeek workoutWeek) {
        log.trace("Get WorkoutDays by WorkoutWeek: {}", workoutWeek);
        List<WorkoutDay> workoutDays = workoutDayRepository.findAllByWorkoutWeek(workoutWeek);
        workoutDays.sort(Comparator.comparing(WorkoutDay::getDate));
        return workoutDays;
    }

    @Override
    @Transactional
    public void createAllFromOldWorkoutWeek(WorkoutWeek oldWorkoutWeek, WorkoutWeek newWorkoutWeek) {
        log.trace("Create all WorkoutDays from old WorkoutWeek: {}, {}", oldWorkoutWeek, newWorkoutWeek);
        List<WorkoutDay> workoutDays = getAllByWorkoutWeek(oldWorkoutWeek);

        for (int i = 0; i < workoutDays.size(); i++) {
            WorkoutDay workoutDayOld = workoutDays.get(i);
            WorkoutDay workoutDay = WorkoutDay.builder()
                    .workoutWeek(newWorkoutWeek)
                    .name(workoutDayOld.getName())
                    .date(newWorkoutWeek.getWeekStartDate().plusDays(i))
                    .isWorkoutDay(workoutDayOld.getIsWorkoutDay())
                    .build();
            save(workoutDay);

            workoutExerciseService.createAllFromOldWorkoutDay(workoutDayOld, workoutDay);
        }


    }

    @Override
    public void save(WorkoutDay workoutWeek) {
        log.trace("Save WorkoutDay: {}", workoutWeek);
        workoutDayRepository.save(workoutWeek);
    }

    @Override
    @Transactional
    public void setName(Long workoutDayId, String name) {
        log.trace("Set workout name {} for WorkoutDay by id: {}", name, workoutDayId);

        WorkoutDay workoutDay = getById(workoutDayId);
        workoutDay.setName(name);
        workoutDay.setIsWorkoutDay(true);

        save(workoutDay);
    }

    @Override
    @Transactional
    public void setRestWorkoutDay(Long workoutDayId) {
        log.trace("Set rest for WorkoutDay by id: {}", workoutDayId);

        WorkoutDay workoutDay = getById(workoutDayId);
        workoutDay.setIsWorkoutDay(false);

        save(workoutDay);
    }
}
