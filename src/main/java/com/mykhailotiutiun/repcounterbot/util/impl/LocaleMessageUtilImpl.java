package com.mykhailotiutiun.repcounterbot.util.impl;

import com.mykhailotiutiun.repcounterbot.cache.SelectedLanguageCache;
import com.mykhailotiutiun.repcounterbot.language.SelectedLanguageProvider;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocaleMessageUtilImpl implements LocaleMessageUtil {

    private final MessageSource messageSource;
    private final SelectedLanguageCache selectedLanguageCache;
    private final SelectedLanguageProvider selectedLanguageProvider;

    public LocaleMessageUtilImpl(MessageSource messageSource, SelectedLanguageCache selectedLanguageCache, SelectedLanguageProvider selectedLanguageProvider) {
        this.messageSource = messageSource;
        this.selectedLanguageCache = selectedLanguageCache;
        this.selectedLanguageProvider = selectedLanguageProvider;
    }

    @Override
    public String getMessage(String messageCode, String chatId) {
        return messageSource.getMessage(messageCode, null, Locale.forLanguageTag(getLocalTag(chatId)));
    }

    @Override
    public String getLocalTag(String chatId) {
        String localeTag = selectedLanguageCache.getSelectedLanguage(chatId);
        if (localeTag == null) {
            localeTag = selectedLanguageProvider.getLocaleTag(chatId);
            selectedLanguageCache.setSelectedLanguage(chatId, localeTag);
        }
        return localeTag;
    }

}
