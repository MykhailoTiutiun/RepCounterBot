package com.mykhailotiutiun.repcounterbot.service.Impl;

import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.service.LocaleMessageService;
import com.mykhailotiutiun.repcounterbot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
public class LocaleMessageServiceImpl implements LocaleMessageService {

    private final MessageSource messageSource;
    private final ChatDataCache chatDataCache;
    private final UserService userService;

    public LocaleMessageServiceImpl(MessageSource messageSource, ChatDataCache chatDataCache, @Lazy UserService userService) {
        this.messageSource = messageSource;
        this.chatDataCache = chatDataCache;
        this.userService = userService;
    }

    @Override
    public String getMessage(String messageCode, String chatId) {
        return messageSource.getMessage(messageCode, null, Locale.forLanguageTag(getLocalTag(chatId)));
    }

    @Override
    public String getLocalTag(String chatId) {
        String localeTag = chatDataCache.getUserSelectedLanguage(chatId);
        if (localeTag == null) {
            localeTag = userService.getUserById(Long.valueOf(chatId)).getLocalTag();
            chatDataCache.setUserSelectedLanguage(chatId, localeTag);
        }
        return localeTag;
    }

}
