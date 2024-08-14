package com.mykhailotiutiun.repcounterbot.cache.impl;

import com.mykhailotiutiun.repcounterbot.cache.CurrentBotStateCache;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CurrentBotStateCacheImpl implements CurrentBotStateCache {

    private final Map<String, ChatState> usersBotStates = new HashMap<>();

    @Override
    public void setChatDataCurrentBotState(String chatId, ChatState chatState) {
        usersBotStates.put(chatId, chatState);
    }

    @Override
    public ChatState getChatDataCurrentBotState(String chatId) {
        ChatState chatState = usersBotStates.get(chatId);
        if (chatState == null) {
            chatState = ChatState.MAIN_MENU;
        }

        return chatState;
    }
}
