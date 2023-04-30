package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

public interface WorkoutWeekService {
    WorkoutWeek getWorkoutWeekById(String id);

    WorkoutWeek getCurrentWorkoutWeekByUserId(Long userId);

    List<WorkoutWeek> getAllWorkoutWeeks();

    void create(WorkoutWeek workoutWeek) throws EntityAlreadyExistsException;

    void createFromOldWorkoutWeek(WorkoutWeek oldWorkoutWeek);

    void save(WorkoutWeek workoutWeek);

    void deleteById(String id);

    SendMessage getCurrentWorkoutWeekSendMessage(String chatId);

    EditMessageText getCurrentWorkoutWeekEditMessage(String chatId, Integer messageId);
}
