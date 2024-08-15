package com.mykhailotiutiun.repcounterbot.cache;

import com.mykhailotiutiun.repcounterbot.constants.ChatState;

public interface CurrentBotStateCache {

    void setChatDataCurrentBotState(String chatId, ChatState chatState);

    ChatState getChatDataCurrentBotState(String chatId);
}
