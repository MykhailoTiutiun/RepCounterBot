package com.mykhailotiutiun.repcounterbot.service.Impl;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutDayRepository;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class WorkoutDayServiceImpl implements WorkoutDayService {

    private final WorkoutDayRepository workoutDayRepository;
    private final WorkoutExerciseService workoutExerciseService;
    private final LocaleMessageUtil localeMessageUtil;

    public WorkoutDayServiceImpl(WorkoutDayRepository workoutDayRepository, WorkoutExerciseService workoutExerciseService, LocaleMessageUtil localeMessageUtil) {
        this.workoutDayRepository = workoutDayRepository;
        this.workoutExerciseService = workoutExerciseService;
        this.localeMessageUtil = localeMessageUtil;
    }

    @Override
    public WorkoutDay getById(String id) {
        log.trace("Get WorkoutDay with id: {}", id);
        return workoutDayRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<WorkoutDay> getAllByWorkoutWeek(WorkoutWeek workoutWeek) {
        log.trace("Get WorkoutDays by WorkoutWeek: {}", workoutWeek);
        return workoutDayRepository.findAllByWorkoutWeek(workoutWeek);
    }

    @Override
    @Transactional
    public void createAllFromOldWorkoutWeek(WorkoutWeek oldWorkoutWeek, WorkoutWeek newWorkoutWeek) {
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
    public void setName(String workoutDayId, String name) {
        log.trace("Set workout name {} to WorkoutDay by id: {}", name, workoutDayId);

        WorkoutDay workoutDay = getById(workoutDayId);
        workoutDay.setName(name);
        workoutDay.setIsWorkoutDay(true);

        save(workoutDay);
    }

    @Override
    @Transactional
    public void setRestWorkoutDay(String workoutDayId) {
        log.trace("Set rest for WorkoutDay by id: {}", workoutDayId);

        WorkoutDay workoutDay = getById(workoutDayId);
        workoutDay.setIsWorkoutDay(false);

        save(workoutDay);
    }
}
