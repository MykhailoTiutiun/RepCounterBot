package com.mykhailotiutiun.repcounterbot.cache.impl;

import com.mykhailotiutiun.repcounterbot.cache.SelectedLanguageCache;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SelectedLanguageCacheImpl implements SelectedLanguageCache {

    private final Map<String, String> selectedLanguage = new HashMap<>();

    @Override
    public void setSelectedLanguage(String chatId, String localTag) {
        selectedLanguage.put(chatId, localTag);
    }

    @Override
    public String getSelectedLanguage(String chatId) {
        return selectedLanguage.get(chatId);
    }
}
