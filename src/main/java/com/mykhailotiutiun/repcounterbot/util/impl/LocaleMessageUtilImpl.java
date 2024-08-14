package com.mykhailotiutiun.repcounterbot.util.impl;

import com.mykhailotiutiun.repcounterbot.cache.SelectedLanguageCache;
import com.mykhailotiutiun.repcounterbot.service.UserService;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocaleMessageUtilImpl implements LocaleMessageUtil {

    private final MessageSource messageSource;
    private final SelectedLanguageCache selectedLanguageCache;
    private final UserService userService;

    public LocaleMessageUtilImpl(MessageSource messageSource, SelectedLanguageCache selectedLanguageCache, UserService userService) {
        this.messageSource = messageSource;
        this.selectedLanguageCache = selectedLanguageCache;
        this.userService = userService;
    }

    @Override
    public String getMessage(String messageCode, String chatId) {
        return messageSource.getMessage(messageCode, null, Locale.forLanguageTag(getLocalTag(chatId)));
    }

    @Override
    public String getLocalTag(String chatId) {
        String localeTag = selectedLanguageCache.getSelectedLanguage(chatId);
        if (localeTag == null) {
            localeTag = userService.getById(Long.valueOf(chatId)).getLocalTag();
            selectedLanguageCache.setSelectedLanguage(chatId, localeTag);
        }
        return localeTag;
    }

}
