package com.mykhailotiutiun.repcounterbot.service;

public interface LocaleMessageService {
    String getMessage(String messageCode, String chatId);

    String getLocalTag(String chatId);
}
