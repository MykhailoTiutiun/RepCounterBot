package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

public interface WorkoutDayService {
    WorkoutDay getWorkoutDayById(String id);

    List<WorkoutDay> getAllWorkoutDaysByWorkoutWeek(WorkoutWeek workoutWeek);

    List<WorkoutDay> getAllWorkoutDays();

    void createAllFromOldWorkoutWeek(WorkoutWeek oldWorkoutWeek, WorkoutWeek newWorkoutWeek);

    void save(WorkoutDay workoutWeek);

    void setWorkoutDayName(String workoutDayId, String name);

    void setRestWorkoutDay(String workoutDayId);

    void deleteById(String id);

    EditMessageText getSelectWorkoutDayEditMessage(String chatId, Integer messageId, String workoutDayId);
}
