package com.mykhailotiutiun.repcounterbot.cache;

import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatDataCache {

    private final Map<String, ChatState> usersBotStates = new HashMap<>();
    private final Map<String, String> selectedWorkoutDays = new HashMap<>();

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

    public void setSelectedWorkoutDay(String chatId, String workoutDayId){
        selectedWorkoutDays.put(chatId, workoutDayId);
    }

    public String getSelectedWorkoutDay(String chatId) {
        return selectedWorkoutDays.get(chatId);
    }

}
