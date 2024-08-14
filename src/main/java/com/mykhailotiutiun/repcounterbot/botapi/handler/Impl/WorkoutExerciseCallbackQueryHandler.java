package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.cache.SelectedWorkoutExerciseCache;
import com.mykhailotiutiun.repcounterbot.cache.CurrentBotStateCache;
import com.mykhailotiutiun.repcounterbot.cache.SelectedWorkoutDayCache;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.message.WorkoutDayMessageGenerator;
import com.mykhailotiutiun.repcounterbot.message.WorkoutExerciseMessageGenerator;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import com.mykhailotiutiun.repcounterbot.message.MainMenuMessageGenerator;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class WorkoutExerciseCallbackQueryHandler implements CallbackQueryHandler {

    private final SelectedWorkoutExerciseCache selectedWorkoutExerciseCache;
    private final WorkoutExerciseService workoutExerciseService;
    private final LocaleMessageUtil localeMessageUtil;
    private final MainMenuMessageGenerator mainMenuMessageGenerator;
    private final CurrentBotStateCache currentBotStateCache;
    private final SelectedWorkoutDayCache selectedWorkoutDayCache;
    private final WorkoutDayMessageGenerator workoutDayMessageGenerator;
    private final WorkoutExerciseMessageGenerator workoutExerciseMessageGenerator;


    public WorkoutExerciseCallbackQueryHandler(SelectedWorkoutExerciseCache selectedWorkoutExerciseCache, WorkoutExerciseService workoutExerciseService, LocaleMessageUtil localeMessageUtil, MainMenuMessageGenerator mainMenuMessageGenerator, CurrentBotStateCache currentBotStateCache, SelectedWorkoutDayCache selectedWorkoutDayCache, WorkoutDayMessageGenerator workoutDayMessageGenerator, WorkoutExerciseMessageGenerator workoutExerciseMessageGenerator) {
        this.selectedWorkoutExerciseCache = selectedWorkoutExerciseCache;
        this.workoutExerciseService = workoutExerciseService;
        this.localeMessageUtil = localeMessageUtil;
        this.mainMenuMessageGenerator = mainMenuMessageGenerator;
        this.currentBotStateCache = currentBotStateCache;
        this.selectedWorkoutDayCache = selectedWorkoutDayCache;
        this.workoutDayMessageGenerator = workoutDayMessageGenerator;
        this.workoutExerciseMessageGenerator = workoutExerciseMessageGenerator;
    }

    @Override
    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().startsWith("/create-request")) {
            return handleCreateRequest(callbackQuery);
        } else if (callbackQuery.getData().startsWith("/select")) {
            return handleSelect(callbackQuery);
        } else if (callbackQuery.getData().startsWith("/edit")) {
            return handleEdit(callbackQuery);
        } else if(callbackQuery.getData().startsWith("/move-up")) {
            return moveUp(callbackQuery);
        } else if(callbackQuery.getData().startsWith("/move-down")) {
            return moveDown(callbackQuery);
        } else if(callbackQuery.getData().startsWith("/change-name-request")) {
            return handleChangeNameRequest(callbackQuery);
        } else if (callbackQuery.getData().startsWith("/delete-request")) {
            return handleDeleteRequest(callbackQuery);
        } else if (callbackQuery.getData().startsWith("/delete")) {
            return handleDelete(callbackQuery);
        }
        return null;
    }

    @Override
    public CallbackHandlerType getHandlerType() {
        return CallbackHandlerType.WORKOUT_EXERCISE_HANDLER;
    }

    private EditMessageText handleCreateRequest(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getFrom().getId().toString();
        currentBotStateCache.setChatDataCurrentBotState(chatId, ChatState.CREATE_WORKOUT_EXERCISE);
        selectedWorkoutDayCache.setSelectedWorkoutDay(chatId, callbackQuery.getData().split(":")[1]);

        EditMessageText editMessageText = new EditMessageText(localeMessageUtil.getMessage("reply.workout-exercise.enter-the-name", chatId));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setReplyMarkup(mainMenuMessageGenerator.getBackButtonInlineKeyboard(chatId , "/select-WorkoutDay:" + callbackQuery.getData().split(":")[1]));

        return editMessageText;
    }




    private EditMessageText handleSelect(CallbackQuery callbackQuery) {
        return workoutExerciseMessageGenerator.getWorkoutExerciseEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), callbackQuery.getData().split(":")[1], false);
    }

    private EditMessageText handleEdit(CallbackQuery callbackQuery) {
        return workoutExerciseMessageGenerator.getWorkoutExerciseEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), callbackQuery.getData().split(":")[1], true);
    }

    private EditMessageText moveUp(CallbackQuery callbackQuery){
        WorkoutExercise workoutExercise = workoutExerciseService.getById(callbackQuery.getData().split(":")[1]);
        workoutExerciseService.moveUp(callbackQuery.getData().split(":")[1]);
        return workoutDayMessageGenerator.getSelectWorkoutDayEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), workoutExercise.getWorkoutDay().getId());
    }

    private EditMessageText moveDown(CallbackQuery callbackQuery){
        workoutExerciseService.moveDown(callbackQuery.getData().split(":")[1]);
        WorkoutExercise workoutExercise = workoutExerciseService.getById(callbackQuery.getData().split(":")[1]);
        return workoutDayMessageGenerator.getSelectWorkoutDayEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), workoutExercise.getWorkoutDay().getId());
    }

    private EditMessageText handleChangeNameRequest(CallbackQuery callbackQuery){
        String chatId = callbackQuery.getFrom().getId().toString();
        currentBotStateCache.setChatDataCurrentBotState(chatId, ChatState.CHANGE_WORKOUT_EXERCISE_NAME);
        selectedWorkoutExerciseCache.setSelectedWorkoutExercise(chatId, callbackQuery.getData().split(":")[1]);

        EditMessageText editMessageText = new EditMessageText(localeMessageUtil.getMessage("reply.workout-exercise.enter-the-name", chatId));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());

        editMessageText.setReplyMarkup(mainMenuMessageGenerator.getBackButtonInlineKeyboard(chatId, "/edit-WorkoutExercise:" + callbackQuery.getData().split(":")[1]));
        return editMessageText;
    }

    private EditMessageText handleDeleteRequest(CallbackQuery callbackQuery){
        String exerciseId = callbackQuery.getData().split(":")[1];
        return mainMenuMessageGenerator.getAreYouSureMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), "/delete-WorkoutExercise:" + exerciseId, "/select-WorkoutExercise:" + exerciseId);
    }

    private EditMessageText handleDelete(CallbackQuery callbackQuery) {
        WorkoutExercise workoutExercise = workoutExerciseService.getById(callbackQuery.getData().split(":")[1]);
        String workoutDayId = workoutExercise.getWorkoutDay().getId();
        workoutExerciseService.deleteById(workoutExercise.getId());

        return workoutDayMessageGenerator.getSelectWorkoutDayEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), workoutDayId);
    }

}
