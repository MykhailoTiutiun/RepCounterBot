package com.mykhailotiutiun.repcounterbot.cache;

import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatDataCache {

    private final Map<String, ChatState> usersBotStates = new HashMap<>();
    private final Map<String, Integer> selectedMessageId = new HashMap<>();
    private final Map<String, String> userSelectedLanguage = new HashMap<>();
    private final Map<String, String> selectedWorkoutDays = new HashMap<>();
    private final Map<String, String> selectedWorkoutExercises = new HashMap<>();

    public void setChatDataCurrentBotState(String chatId, ChatState chatState) {
        usersBotStates.put(chatId, chatState);
    }

    public ChatState getChatDataCurrentBotState(String chatId) {
        ChatState chatState = usersBotStates.get(chatId);
        if (chatState == null) {
            chatState = ChatState.MAIN_MENU;
        }

        return chatState;
    }

    public void setSelectedMessageId(String chatId, Integer messageId) {
        selectedMessageId.put(chatId, messageId);
    }

    public Integer getSelectedMessageId(String chatId) {
        return selectedMessageId.get(chatId);
    }

    public void setSelectedWorkoutDay(String chatId, String workoutDayId) {
        selectedWorkoutDays.put(chatId, workoutDayId);
    }

    public void setUserSelectedLanguage(String chatId, String localTag) {
        userSelectedLanguage.put(chatId, localTag);
    }

    public String getUserSelectedLanguage(String chatId) {
        return userSelectedLanguage.get(chatId);
    }

    public String getSelectedWorkoutDay(String chatId) {
        return selectedWorkoutDays.get(chatId);
    }

    public void setSelectedWorkoutExercise(String chatId, String workoutExerciseId) {
        selectedWorkoutExercises.put(chatId, workoutExerciseId);
    }

    public String getSelectedWorkoutExercise(String chatId) {
        return selectedWorkoutExercises.get(chatId);
    }

}
