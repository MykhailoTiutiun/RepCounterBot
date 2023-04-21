package com.mykhailotiutiun.repcounterbot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MainMenuService {
    SendMessage getMainMenuMessage(String chatId, String message);
}
