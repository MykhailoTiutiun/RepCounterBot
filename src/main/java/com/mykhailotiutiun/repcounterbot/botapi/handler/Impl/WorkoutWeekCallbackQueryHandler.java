package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.message.WorkoutWeekMessageGenerator;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class WorkoutWeekCallbackQueryHandler implements CallbackQueryHandler {

    private final WorkoutWeekMessageGenerator workoutWeekMessageGenerator;

    public WorkoutWeekCallbackQueryHandler(WorkoutWeekMessageGenerator workoutWeekMessageGenerator) {
        this.workoutWeekMessageGenerator = workoutWeekMessageGenerator;
    }

    @Override
    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        return workoutWeekMessageGenerator.getCurrentWorkoutWeekEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId());
    }

    @Override
    public CallbackHandlerType getHandlerType() {
        return CallbackHandlerType.WORKOUT_WEEK_HANDLER;
    }
}
