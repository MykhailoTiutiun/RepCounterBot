package com.mykhailotiutiun.repcounterbot.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface WorkoutWeekMessageGenerator {

    SendMessage getCurrentWorkoutWeekSendMessage(String chatId);
    EditMessageText getCurrentWorkoutWeekEditMessage(String chatId, Integer messageId);
}
