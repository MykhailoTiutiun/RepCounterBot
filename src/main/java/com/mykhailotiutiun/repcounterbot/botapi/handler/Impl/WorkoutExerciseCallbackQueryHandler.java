package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.service.LocaleMessageService;
import com.mykhailotiutiun.repcounterbot.service.MainMenuService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class WorkoutExerciseCallbackQueryHandler implements CallbackQueryHandler {

    private final ChatDataCache chatDataCache;
    private final WorkoutExerciseService workoutExerciseService;
    private final WorkoutDayService workoutDayService;
    private final LocaleMessageService localeMessageService;
    private final MainMenuService mainMenuService;


    public WorkoutExerciseCallbackQueryHandler(ChatDataCache chatDataCache, WorkoutExerciseService workoutExerciseService, WorkoutDayService workoutDayService, LocaleMessageService localeMessageService, MainMenuService mainMenuService) {
        this.chatDataCache = chatDataCache;
        this.workoutExerciseService = workoutExerciseService;
        this.workoutDayService = workoutDayService;
        this.localeMessageService = localeMessageService;
        this.mainMenuService = mainMenuService;
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
        chatDataCache.setChatDataCurrentBotState(chatId, ChatState.CREATE_WORKOUT_EXERCISE);
        chatDataCache.setSelectedMessageId(chatId, callbackQuery.getMessage().getMessageId());
        chatDataCache.setSelectedWorkoutDay(chatId, callbackQuery.getData().split(":")[1]);

        EditMessageText editMessageText = new EditMessageText(localeMessageService.getMessage("reply.workout-exercise.enter-the-name", chatId));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setReplyMarkup(mainMenuService.getBackButtonInlineKeyboard(chatId , "/select-WorkoutDay:" + callbackQuery.getData().split(":")[1]));

        return editMessageText;
    }




    private EditMessageText handleSelect(CallbackQuery callbackQuery) {
        return workoutExerciseService.getWorkoutExerciseEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), callbackQuery.getData().split(":")[1], false);
    }

    private EditMessageText handleEdit(CallbackQuery callbackQuery) {
        return workoutExerciseService.getWorkoutExerciseEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), callbackQuery.getData().split(":")[1], true);
    }

    private EditMessageText moveUp(CallbackQuery callbackQuery){
        WorkoutExercise workoutExercise = workoutExerciseService.getWorkoutExerciseById(callbackQuery.getData().split(":")[1]);
        workoutExerciseService.moveUpWorkoutExercise(callbackQuery.getData().split(":")[1]);
        return workoutDayService.getSelectWorkoutDayEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), workoutExercise.getWorkoutDay().getId());
    }

    private EditMessageText moveDown(CallbackQuery callbackQuery){
        workoutExerciseService.moveDownWorkoutExercise(callbackQuery.getData().split(":")[1]);
        WorkoutExercise workoutExercise = workoutExerciseService.getWorkoutExerciseById(callbackQuery.getData().split(":")[1]);
        return workoutDayService.getSelectWorkoutDayEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), workoutExercise.getWorkoutDay().getId());
    }

    private EditMessageText handleChangeNameRequest(CallbackQuery callbackQuery){
        String chatId = callbackQuery.getFrom().getId().toString();
        chatDataCache.setChatDataCurrentBotState(chatId, ChatState.CHANGE_WORKOUT_EXERCISE_NAME);
        chatDataCache.setSelectedWorkoutExercise(chatId, callbackQuery.getData().split(":")[1]);

        EditMessageText editMessageText = new EditMessageText(localeMessageService.getMessage("reply.workout-exercise.enter-the-name", chatId));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());

        editMessageText.setReplyMarkup(mainMenuService.getBackButtonInlineKeyboard(chatId, "/edit-WorkoutExercise:" + callbackQuery.getData().split(":")[1]));
        return editMessageText;
    }

    private EditMessageText handleDeleteRequest(CallbackQuery callbackQuery){
        String exerciseId = callbackQuery.getData().split(":")[1];
        return mainMenuService.getAreYouSureMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), "/delete-WorkoutExercise:" + exerciseId, "/select-WorkoutExercise:" + exerciseId);
    }

    private EditMessageText handleDelete(CallbackQuery callbackQuery) {
        WorkoutExercise workoutExercise = workoutExerciseService.getWorkoutExerciseById(callbackQuery.getData().split(":")[1]);
        String workoutDayId = workoutExercise.getWorkoutDay().getId();
        workoutExerciseService.deleteById(workoutExercise.getId());

        return workoutDayService.getSelectWorkoutDayEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), workoutDayId);
    }

}
