package com.mykhailotiutiun.repcounterbot.util;

public interface LocaleMessageUtil {
    String getMessage(String messageCode, String chatId);

    String getLocalTag(String chatId);
}
