package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class WorkoutDayMessageHandler implements MessageHandler {

    private final ChatDataCache chatDataCache;
    private final WorkoutDayService workoutDayService;
    private final WorkoutWeekService workoutWeekService;

    public WorkoutDayMessageHandler(ChatDataCache chatDataCache, WorkoutDayService workoutDayService, WorkoutWeekService workoutWeekService) {
        this.chatDataCache = chatDataCache;
        this.workoutDayService = workoutDayService;
        this.workoutWeekService = workoutWeekService;
    }

    @Override
    public SendMessage handleMessage(Message message) {
        workoutDayService.setWorkoutDayName(chatDataCache.getSelectedWorkoutDay(message.getChatId().toString()), message.getText());

        chatDataCache.setChatDataCurrentBotState(message.getChatId().toString(), ChatState.MAIN_MENU);
        return workoutWeekService.getCurrentWorkoutWeekSendMessage(message.getChatId().toString());
    }

    @Override
    public MessageHandlerType getHandlerType() {
        return MessageHandlerType.WORKOUT_DAY_HANDLER;
    }
}
