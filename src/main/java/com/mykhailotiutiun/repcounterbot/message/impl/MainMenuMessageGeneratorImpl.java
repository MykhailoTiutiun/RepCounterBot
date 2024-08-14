package com.mykhailotiutiun.repcounterbot.message.impl;

import com.mykhailotiutiun.repcounterbot.message.MainMenuMessageGenerator;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainMenuMessageGeneratorImpl implements MainMenuMessageGenerator {

    private final LocaleMessageUtil localeMessageUtil;

    public MainMenuMessageGeneratorImpl(LocaleMessageUtil localeMessageUtil) {
        this.localeMessageUtil = localeMessageUtil;
    }

    @Override
    public SendMessage getMainMenuMessage(String chatId, String firstName) {
        return createMessageWithKeyboard(chatId, localeMessageUtil.getMessage("reply.main-menu.greeting", chatId) + firstName, getMainMenuKeyboardMarkup(chatId));
    }

    @Override
    public EditMessageText getAreYouSureMessage(String chatId, Integer messageId, String okCallback, String cancelCallback) {
        EditMessageText editMessageText = new EditMessageText(localeMessageUtil.getMessage("reply.are-you-sure", chatId));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);

        InlineKeyboardButton okButton = InlineKeyboardButton.builder().text(localeMessageUtil.getMessage("reply.are-you-sure.keyboard.ok", chatId)).callbackData(okCallback).build();
        InlineKeyboardButton cancelButton = InlineKeyboardButton.builder().text(localeMessageUtil.getMessage("reply.are-you-sure.keyboard.cancel", chatId)).callbackData(cancelCallback).build();

        editMessageText.setReplyMarkup(InlineKeyboardMarkup.builder().keyboardRow(List.of(okButton, cancelButton)).build());

        return editMessageText;
    }

    @Override
    public InlineKeyboardMarkup getBackButtonInlineKeyboard(String chatId, String backButtonCallbackData) {
        InlineKeyboardButton backButton = new InlineKeyboardButton(localeMessageUtil.getMessage("reply.keyboard.back", chatId));
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
        row1.add(new KeyboardButton(localeMessageUtil.getMessage("reply.main-menu.keyboard.recent-week", chatId)));
        row2.add(new KeyboardButton(localeMessageUtil.getMessage("reply.main-menu.keyboard.change-lang", chatId)));
        keyboard.add(row1);
        keyboard.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
