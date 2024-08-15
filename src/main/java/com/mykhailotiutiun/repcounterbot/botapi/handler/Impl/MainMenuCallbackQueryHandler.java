package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.message.MainMenuMessageGenerator;
import com.mykhailotiutiun.repcounterbot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class MainMenuCallbackQueryHandler implements CallbackQueryHandler {

    private final MainMenuMessageGenerator mainMenuMessageGenerator;
    private final UserService userService;

    public MainMenuCallbackQueryHandler(MainMenuMessageGenerator mainMenuMessageGenerator, UserService userService) {
        this.mainMenuMessageGenerator = mainMenuMessageGenerator;
        this.userService = userService;
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        // 0 - command, 1 - userId, 2 - lang
        String[] splintedMessage = callbackQuery.getData().split(":");
        userService.setUserLang(Long.valueOf(splintedMessage[1]), splintedMessage[2]);
        return mainMenuMessageGenerator.getMainMenuMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getFrom().getFirstName());
    }

    @Override
    public CallbackHandlerType getHandlerType() {
        return CallbackHandlerType.MAIN_MENU_HANDLER;
    }
}
