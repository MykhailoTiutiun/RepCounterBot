package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.cache.CurrentBotStateCache;
import com.mykhailotiutiun.repcounterbot.cache.SelectedWorkoutDayCache;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.message.WorkoutWeekMessageGenerator;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class WorkoutDayMessageHandler implements MessageHandler {

    private final WorkoutDayService workoutDayService;
    private final WorkoutWeekService workoutWeekService;
    private final CurrentBotStateCache currentBotStateCache;
    private final SelectedWorkoutDayCache selectedWorkoutDayCache;
    private final WorkoutWeekMessageGenerator workoutWeekMessageGenerator;

    public WorkoutDayMessageHandler(WorkoutDayService workoutDayService, WorkoutWeekService workoutWeekService, CurrentBotStateCache currentBotStateCache, SelectedWorkoutDayCache selectedWorkoutDayCache, WorkoutWeekMessageGenerator workoutWeekMessageGenerator) {
        this.workoutDayService = workoutDayService;
        this.workoutWeekService = workoutWeekService;
        this.currentBotStateCache = currentBotStateCache;
        this.selectedWorkoutDayCache = selectedWorkoutDayCache;
        this.workoutWeekMessageGenerator = workoutWeekMessageGenerator;
    }

    @Override
    public BotApiMethod<?> handleMessage(Message message) {
        workoutDayService.setName(selectedWorkoutDayCache.getSelectedWorkoutDay(message.getChatId().toString()), message.getText());

        currentBotStateCache.setChatDataCurrentBotState(message.getChatId().toString(), ChatState.MAIN_MENU);
        return workoutWeekMessageGenerator.getCurrentWorkoutWeekSendMessage(message.getChatId().toString());
    }

    @Override
    public MessageHandlerType getHandlerType() {
        return MessageHandlerType.WORKOUT_DAY_HANDLER;
    }
}
