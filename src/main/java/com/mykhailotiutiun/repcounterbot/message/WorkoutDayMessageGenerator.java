package com.mykhailotiutiun.repcounterbot.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface WorkoutDayMessageGenerator {

    SendMessage getSelectWorkoutDaySendMessage(String chatId, Long workoutDayId);
    EditMessageText getSelectWorkoutDayEditMessage(String chatId, Integer messageId, Long workoutDayId);
}
