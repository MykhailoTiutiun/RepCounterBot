package com.mykhailotiutiun.repcounterbot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface MainMenuService {
    SendMessage getMainMenuMessage(String chatId, String firstName);
    EditMessageText getAreYouSureMessage(String chatId, Integer messageId, String okCallback, String cancelCallback);
    InlineKeyboardMarkup getBackButtonInlineKeyboard(String chatId, String backButtonCallbackData);
}
