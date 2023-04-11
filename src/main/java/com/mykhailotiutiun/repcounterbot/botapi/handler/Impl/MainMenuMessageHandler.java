package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.service.MainMenuService;
import com.mykhailotiutiun.repcounterbot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MainMenuMessageHandler implements MessageHandler {

    private final MainMenuService mainMenuService;
    private final UserService userService;

    public MainMenuMessageHandler(MainMenuService mainMenuService, UserService userService) {
        this.mainMenuService = mainMenuService;
        this.userService = userService;
    }

    @Override
    public SendMessage handleMessage(Message message) {
        try {
            userService.create(new User(message.getFrom().getId(), message.getFrom().getFirstName()));
        } catch (EntityAlreadyExistsException ignored){

        }
        return mainMenuService.getMainMenuMessage(message.getChatId().toString(), "Привіт " + message.getFrom().getFirstName());
    }

    @Override
    public MessageHandlerType getHandlerType() {
        return MessageHandlerType.MAIN_MENU;
    }
}
