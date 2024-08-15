package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.message.MainMenuMessageGenerator;
import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.service.UserService;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
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

    private final MainMenuMessageGenerator mainMenuMessageGenerator;
    private final UserService userService;
    private final LocaleMessageUtil localeMessageUtil;

    public MainMenuMessageHandler(MainMenuMessageGenerator mainMenuMessageGenerator, UserService userService, LocaleMessageUtil localeMessageUtil) {
        this.mainMenuMessageGenerator = mainMenuMessageGenerator;
        this.userService = userService;
        this.localeMessageUtil = localeMessageUtil;
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
            userService.create(User.builder()
                    .id(message.getFrom().getId())
                    .username(message.getFrom().getFirstName())
                    .localTag("en_EN")
                    .build());
        } catch (EntityAlreadyExistsException ignored) {
        }
        return mainMenuMessageGenerator.getMainMenuMessage(message.getChatId().toString(), message.getFrom().getFirstName());
    }

    private SendMessage handleChooseLang(Message message) {
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), localeMessageUtil.getMessage("reply.main-menu.keyboard.change-lang", message.getChatId().toString()));
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
