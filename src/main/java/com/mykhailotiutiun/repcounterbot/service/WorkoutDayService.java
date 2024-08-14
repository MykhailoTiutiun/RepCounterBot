package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

public interface WorkoutDayService {
    WorkoutDay getById(String id);
    List<WorkoutDay> getAllByWorkoutWeek(WorkoutWeek workoutWeek);

    void createAllFromOldWorkoutWeek(WorkoutWeek oldWorkoutWeek, WorkoutWeek newWorkoutWeek);
    void save(WorkoutDay workoutDay);
    void setName(String workoutDayId, String name);
    void setRestWorkoutDay(String workoutDayId);

}
