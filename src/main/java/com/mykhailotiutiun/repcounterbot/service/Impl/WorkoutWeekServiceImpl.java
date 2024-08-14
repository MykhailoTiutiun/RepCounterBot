package com.mykhailotiutiun.repcounterbot.service.Impl;


import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutWeekRepository;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import com.mykhailotiutiun.repcounterbot.util.LocalDateWeekUtil;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WorkoutWeekServiceImpl implements WorkoutWeekService {

    private final WorkoutWeekRepository workoutWeekRepository;
    private final WorkoutDayService workoutDayService;
    private final LocalDateWeekUtil localDateWeekUtil;
    private final LocaleMessageUtil localeMessageUtil;


    public WorkoutWeekServiceImpl(WorkoutWeekRepository workoutWeekRepository, WorkoutDayService workoutDayService, LocalDateWeekUtil localDateWeekUtil, LocaleMessageUtil localeMessageUtil) {
        this.workoutWeekRepository = workoutWeekRepository;
        this.workoutDayService = workoutDayService;
        this.localDateWeekUtil = localDateWeekUtil;
        this.localeMessageUtil = localeMessageUtil;
    }

    @Override
    public WorkoutWeek getCurrentWorkoutWeekByUserId(Long userId) {
        log.trace("Get current WorkoutWeek by user-id {}", userId);

        WorkoutWeek workoutWeek = workoutWeekRepository.findByUserIdAndCurrent(userId, true).orElseThrow(EntityNotFoundException::new);
        if (!localDateWeekUtil.isCurrentWeek(workoutWeek.getWeekStartDate(), workoutWeek.getWeekEndDate())) {
            createFromOldWorkoutWeek(workoutWeek);
            workoutWeek = workoutWeekRepository.findByUserIdAndCurrent(userId, true).orElseThrow(EntityNotFoundException::new);
        }
        return workoutWeek;
    }

    @Override
    @Transactional
    public void create(WorkoutWeek workoutWeek) throws EntityAlreadyExistsException {
        log.trace("Create WorkoutWeek: {}", workoutWeek);
        save(workoutWeek);

        for (int i = 0; i < 7; i++) {
            workoutDayService.save(WorkoutDay.builder()
                    .workoutWeek(workoutWeek)
                    .date(workoutWeek.getWeekStartDate().plusDays(i))
                    .build());
        }
    }

    private void createFromOldWorkoutWeek(WorkoutWeek oldWorkoutWeek) {
        log.trace("Create from old WorkoutWeek: {}", oldWorkoutWeek);
        WorkoutWeek workoutWeek = WorkoutWeek.builder()
                .user(oldWorkoutWeek.getUser())
                .weekStartDate(localDateWeekUtil.getFirstDateOfWeekFromDate(LocalDate.now()))
                .weekEndDate(localDateWeekUtil.getLastDateOfWeekFromDate(LocalDate.now()))
                .build();
        save(workoutWeek);

        oldWorkoutWeek.setCurrent(false);
        save(oldWorkoutWeek);

        workoutDayService.createAllFromOldWorkoutWeek(oldWorkoutWeek, workoutWeek);
    }

    private void save(WorkoutWeek workoutWeek) {
        log.trace("Save WorkoutWeek: {}", workoutWeek);
        workoutWeekRepository.save(workoutWeek);
    }
}
