package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class WorkoutSetCallbackQueryHandler implements CallbackQueryHandler {

    private final ChatDataCache chatDataCache;

    public WorkoutSetCallbackQueryHandler(ChatDataCache chatDataCache) {
        this.chatDataCache = chatDataCache;
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().startsWith("/set-request")){
            return handleFastSetsSetRequest(callbackQuery);
        }
        return null;
    }

    @Override
    public CallbackHandlerType getHandlerType() {
        return CallbackHandlerType.WORKOUT_SET_HANDLER;
    }

    private SendMessage handleFastSetsSetRequest(CallbackQuery callbackQuery) {
        chatDataCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.FAST_SETS_SET);
        chatDataCache.setSelectedWorkoutExercise(callbackQuery.getFrom().getId().toString(), callbackQuery.getData().split(":")[1]);
        return new SendMessage(callbackQuery.getFrom().getId().toString(), "Використовуйте патерн: \nВага : кількість повторів, вага : кількість повторів, і т.д.");
    }
}
