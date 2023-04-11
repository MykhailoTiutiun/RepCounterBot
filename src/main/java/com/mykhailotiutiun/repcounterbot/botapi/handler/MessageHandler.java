package com.mykhailotiutiun.repcounterbot.botapi.handler;

import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageHandler {

    SendMessage handleMessage(Message message);

    MessageHandlerType getHandlerType();

}
