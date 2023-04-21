package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class WorkoutDayCallbackQueryHandler implements CallbackQueryHandler {

    private final WorkoutDayService workoutDayService;
    private final WorkoutWeekService workoutWeekService;
    private final WorkoutExerciseService workoutExerciseService;
    private final ChatDataCache chatDataCache;

    public WorkoutDayCallbackQueryHandler(WorkoutDayService workoutDayService, WorkoutWeekService workoutWeekService, WorkoutExerciseService workoutExerciseService, ChatDataCache chatDataCache) {
        this.workoutDayService = workoutDayService;
        this.workoutWeekService = workoutWeekService;
        this.workoutExerciseService = workoutExerciseService;
        this.chatDataCache = chatDataCache;
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().startsWith("/select")) {
            return workoutDayService.getSelectWorkoutDaySendMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getData().split(":")[1]);
        } else if (callbackQuery.getData().startsWith("/set-name-request")) {
            return handelSetNameRequest(callbackQuery);
        } else if (callbackQuery.getData().startsWith("/set-rest")) {
            return handelSetRest(callbackQuery);
        }

        return null;
    }

    @Override
    public CallbackHandlerType getHandlerType() {
        return CallbackHandlerType.WORKOUT_DAY_HANDLER;
    }

    private SendMessage handelSetRest(CallbackQuery callbackQuery) {
        workoutDayService.setRestWorkoutDay(callbackQuery.getData().split(":")[1]);
        return workoutWeekService.getCurrentWorkoutWeekSendMessage(callbackQuery.getFrom().getId().toString());
    }

    private SendMessage handelSetNameRequest(CallbackQuery callbackQuery) {
        chatDataCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.SET_NAME_FOR_WORKOUT_DAY);
        chatDataCache.setSelectedWorkoutDay(callbackQuery.getFrom().getId().toString(), callbackQuery.getData().split(":")[1]);

        return new SendMessage(callbackQuery.getFrom().getId().toString(), "Введіть назву тренування");
    }




}
