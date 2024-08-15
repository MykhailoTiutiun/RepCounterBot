package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.message.WorkoutWeekMessageGenerator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class WorkoutWeekMessageHandler implements MessageHandler {

    private final WorkoutWeekMessageGenerator workoutWeekMessageGenerator;

    public WorkoutWeekMessageHandler(WorkoutWeekMessageGenerator workoutWeekMessageGenerator) {
        this.workoutWeekMessageGenerator = workoutWeekMessageGenerator;
    }

    @Override
    public BotApiMethod<?> handleMessage(Message message) {
        return workoutWeekMessageGenerator.getCurrentWorkoutWeekSendMessage(message.getChatId().toString());
    }

    @Override
    public MessageHandlerType getHandlerType() {
        return MessageHandlerType.CURRENT_WEEK_HANDLER;
    }


}
