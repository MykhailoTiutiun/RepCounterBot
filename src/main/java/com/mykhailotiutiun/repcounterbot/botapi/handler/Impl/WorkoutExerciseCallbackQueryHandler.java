package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.service.LocaleMessageService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class WorkoutExerciseCallbackQueryHandler implements CallbackQueryHandler {

    private final ChatDataCache chatDataCache;
    private final WorkoutExerciseService workoutExerciseService;
    private final WorkoutDayService workoutDayService;
    private final LocaleMessageService localeMessageService;


    public WorkoutExerciseCallbackQueryHandler(ChatDataCache chatDataCache, WorkoutExerciseService workoutExerciseService, WorkoutDayService workoutDayService, LocaleMessageService localeMessageService) {
        this.chatDataCache = chatDataCache;
        this.workoutExerciseService = workoutExerciseService;
        this.workoutDayService = workoutDayService;
        this.localeMessageService = localeMessageService;
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().startsWith("/create-request")) {
            return handleCreateRequest(callbackQuery);
        } else if (callbackQuery.getData().startsWith("/select")) {
            return handleSelect(callbackQuery);
        } else if (callbackQuery.getData().startsWith("/delete")) {
            return handleDelete(callbackQuery);
        }
        return null;
    }

    @Override
    public CallbackHandlerType getHandlerType() {
        return CallbackHandlerType.WORKOUT_EXERCISE_HANDLER;
    }

    private SendMessage handleCreateRequest(CallbackQuery callbackQuery) {
        chatDataCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.CREATE_WORKOUT_EXERCISE);
        chatDataCache.setSelectedWorkoutDay(callbackQuery.getFrom().getId().toString(), callbackQuery.getData().split(":")[1]);

        return new SendMessage(callbackQuery.getFrom().getId().toString(), localeMessageService.getMessage("reply.workout-exercise.create-request", callbackQuery.getFrom().getId().toString()));
    }

    private SendMessage handleSelect(CallbackQuery callbackQuery) {
       return workoutExerciseService.getWorkoutExerciseMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getData().split(":")[1]);
    }

    private SendMessage handleDelete(CallbackQuery callbackQuery) {
        WorkoutExercise workoutExercise = workoutExerciseService.getWorkoutExerciseById(callbackQuery.getData().split(":")[1]);
        String workoutDayId = workoutExercise.getWorkoutDay().getId();
        workoutExerciseService.deleteById(workoutExercise.getId());

        return workoutDayService.getSelectWorkoutDaySendMessage(callbackQuery.getFrom().getId().toString(), workoutDayId);
    }

}
