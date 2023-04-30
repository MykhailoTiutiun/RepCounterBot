package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.service.LocaleMessageService;
import com.mykhailotiutiun.repcounterbot.service.MainMenuService;
import com.mykhailotiutiun.repcounterbot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainMenuMessageHandler implements MessageHandler {

    private final MainMenuService mainMenuService;
    private final UserService userService;
    private final LocaleMessageService localeMessageService;

    public MainMenuMessageHandler(MainMenuService mainMenuService, UserService userService, LocaleMessageService localeMessageService) {
        this.mainMenuService = mainMenuService;
        this.userService = userService;
        this.localeMessageService = localeMessageService;
    }

    @Override
    public BotApiMethod<?> handleMessage(Message message) {
        switch (message.getText()) {
            case ("/start"):
                return handleStart(message);
            case ("Choose a language"):
            case ("Обрати мову"):
                return handleChooseLang(message);
        }
        return null;
    }

    private SendMessage handleStart(Message message) {
        try {
            userService.create(new User(message.getFrom().getId(), message.getFrom().getFirstName()));
        } catch (EntityAlreadyExistsException ignored) {
        }
        return mainMenuService.getMainMenuMessage(message.getChatId().toString(), message.getFrom().getFirstName());
    }

    private SendMessage handleChooseLang(Message message) {
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), localeMessageService.getMessage("reply.main-menu.keyboard.change-lang", message.getChatId().toString()));
        sendMessage.setReplyMarkup(getKeyboardForLang(message.getChatId().toString()));
        return sendMessage;
    }

    private InlineKeyboardMarkup getKeyboardForLang(String chatId) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton engButton = new InlineKeyboardButton("English");
        InlineKeyboardButton ukrButton = new InlineKeyboardButton("Українська");

        engButton.setCallbackData(String.format("/set-lang-Main:%s:en-US", chatId));
        ukrButton.setCallbackData(String.format("/set-lang-Main:%s:uk-UA", chatId));

        row.add(engButton);
        row.add(ukrButton);

        keyboard.add(row);
        return new InlineKeyboardMarkup(keyboard);
    }


    @Override
    public MessageHandlerType getHandlerType() {
        return MessageHandlerType.MAIN_MENU_HANDLER;
    }
}
