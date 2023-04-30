package com.mykhailotiutiun.repcounterbot.botapi.handler;

import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageHandler {

    BotApiMethod<?> handleMessage(Message message);

    MessageHandlerType getHandlerType();

}
