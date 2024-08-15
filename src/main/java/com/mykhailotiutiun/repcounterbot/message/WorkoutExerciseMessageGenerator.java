package com.mykhailotiutiun.repcounterbot.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface WorkoutExerciseMessageGenerator {

    SendMessage getWorkoutExerciseSendMessage(String chatId, Long workoutExerciseId);
    EditMessageText getWorkoutExerciseEditMessage(String chatId, Integer messageId, Long workoutExerciseId, Boolean isOnEditPage);
}
