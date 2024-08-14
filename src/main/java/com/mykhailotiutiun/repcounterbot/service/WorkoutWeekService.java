package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface WorkoutWeekService {

    WorkoutWeek getCurrentWorkoutWeekByUserId(Long userId);

    void create(WorkoutWeek workoutWeek) throws EntityAlreadyExistsException;
}
