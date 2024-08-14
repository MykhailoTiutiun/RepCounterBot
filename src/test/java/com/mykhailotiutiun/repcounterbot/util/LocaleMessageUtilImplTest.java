package com.mykhailotiutiun.repcounterbot.util;

import com.mykhailotiutiun.repcounterbot.cache.SelectedLanguageCache;
import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.service.UserService;
import com.mykhailotiutiun.repcounterbot.util.impl.LocaleMessageUtilImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocaleMessageUtilImplTest {

    @Mock
    private SelectedLanguageCache selectedLanguageCache;
    @Mock
    private MessageSource messageSource;
    @Mock
    private UserService userService;
    @InjectMocks
    private LocaleMessageUtilImpl localeMessageUtil;

    @Test
    public void getMessage(){
        when(messageSource.getMessage("test.code", null, Locale.forLanguageTag("en-EN"))).thenReturn("OK");
        when(selectedLanguageCache.getSelectedLanguage("1")).thenReturn("en-EN");
        assertEquals("OK", localeMessageUtil.getMessage("test.code", "1"));
    }

    @Test
    public void getLocalTag(){
        when(selectedLanguageCache.getSelectedLanguage("1")).thenReturn("en-EN");
        assertEquals("en-EN", localeMessageUtil.getLocalTag("1"));

        when(selectedLanguageCache.getSelectedLanguage("1")).thenReturn(null);
        when(userService.getById(1L)).thenReturn(User.builder().localTag("en-EN").build());
        assertEquals("en-EN", localeMessageUtil.getLocalTag("1"));
        verify(selectedLanguageCache).setSelectedLanguage("1", "en-EN");
    }

}
