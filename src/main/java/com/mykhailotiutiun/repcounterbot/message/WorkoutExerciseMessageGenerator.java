package com.mykhailotiutiun.repcounterbot.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface WorkoutExerciseMessageGenerator {

    SendMessage getWorkoutExerciseSendMessage(String chatId, String workoutExerciseId);
    EditMessageText getWorkoutExerciseEditMessage(String chatId, Integer messageId, String workoutExerciseId, Boolean isOnEditPage);
}
