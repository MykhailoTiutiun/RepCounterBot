package com.mykhailotiutiun.repcounterbot.botapi.handler;

import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackQueryHandler {

    SendMessage handleCallbackQuery(CallbackQuery callbackQuery);

    CallbackHandlerType getHandlerType();
}
