package com.mykhailotiutiun.repcounterbot.cache;

public interface SelectedLanguageCache {

    void setSelectedLanguage(String chatId, String localTag);
    String getSelectedLanguage(String chatId);
}
