package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CurrentWeekMessageHandler implements MessageHandler {

    private final WorkoutWeekService workoutWeekService;
    private final WorkoutDayService workoutDayService;

    public CurrentWeekMessageHandler(WorkoutWeekService workoutWeekService, WorkoutDayService workoutDayService) {
        this.workoutWeekService = workoutWeekService;
        this.workoutDayService = workoutDayService;
    }

    @Override
    public SendMessage handleMessage(Message message) {
        return workoutWeekService.getCurrentWorkoutWeekSendMessage(message.getChatId().toString());
    }

    @Override
    public MessageHandlerType getHandlerType() {
        return MessageHandlerType.CURRENT_WEEK_HANDLER;
    }


}
