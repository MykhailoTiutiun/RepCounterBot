package com.mykhailotiutiun.repcounterbot.service.Impl;

import com.mykhailotiutiun.repcounterbot.service.LocaleMessageService;
import com.mykhailotiutiun.repcounterbot.service.MainMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MainMenuServiceImpl implements MainMenuService {

    private final LocaleMessageService localeMessageService;

    public MainMenuServiceImpl(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    @Override
    public SendMessage getMainMenuMessage(String chatId, String firstName) {
        return createMessageWithKeyboard(chatId, localeMessageService.getMessage("reply.main-menu.greeting", chatId) + firstName, getMainMenuKeyboardMarkup(chatId));
    }

    @Override
    public InlineKeyboardMarkup getBackButtonInlineKeyboard(String chatId, String backButtonCallbackData) {
        InlineKeyboardButton backButton = new InlineKeyboardButton(localeMessageService.getMessage("reply.keyboard.back", chatId));
        backButton.setCallbackData(backButtonCallbackData);
        return InlineKeyboardMarkup.builder().keyboardRow(List.of(backButton)).build();
    }

    private SendMessage createMessageWithKeyboard(String chatId, String message, ReplyKeyboardMarkup replyKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    private ReplyKeyboardMarkup getMainMenuKeyboardMarkup(String chatId) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add(new KeyboardButton(localeMessageService.getMessage("reply.main-menu.keyboard.recent-week", chatId)));
        row2.add(new KeyboardButton(localeMessageService.getMessage("reply.main-menu.keyboard.change-lang", chatId)));
        keyboard.add(row1);
        keyboard.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
