package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.cache.CurrentBotStateCache;
import com.mykhailotiutiun.repcounterbot.cache.SelectedWorkoutDayCache;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.message.WorkoutDayMessageGenerator;
import com.mykhailotiutiun.repcounterbot.message.WorkoutWeekMessageGenerator;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import com.mykhailotiutiun.repcounterbot.message.MainMenuMessageGenerator;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class WorkoutDayCallbackQueryHandler implements CallbackQueryHandler {

    private final WorkoutDayService workoutDayService;
    private final WorkoutWeekService workoutWeekService;
    private final MainMenuMessageGenerator mainMenuMessageGenerator;
    private final LocaleMessageUtil localeMessageUtil;
    private final CurrentBotStateCache currentBotStateCache;
    private final SelectedWorkoutDayCache selectedWorkoutDayCache;
    private final WorkoutDayMessageGenerator workoutDayMessageGenerator;
    private final WorkoutWeekMessageGenerator workoutWeekMessageGenerator;

    public WorkoutDayCallbackQueryHandler(WorkoutDayService workoutDayService, WorkoutWeekService workoutWeekService, MainMenuMessageGenerator mainMenuMessageGenerator, LocaleMessageUtil localeMessageUtil, CurrentBotStateCache currentBotStateCache, SelectedWorkoutDayCache selectedWorkoutDayCache, WorkoutDayMessageGenerator workoutDayMessageGenerator, WorkoutWeekMessageGenerator workoutWeekMessageGenerator) {
        this.workoutDayService = workoutDayService;
        this.workoutWeekService = workoutWeekService;
        this.mainMenuMessageGenerator = mainMenuMessageGenerator;
        this.localeMessageUtil = localeMessageUtil;
        this.currentBotStateCache = currentBotStateCache;
        this.selectedWorkoutDayCache = selectedWorkoutDayCache;
        this.workoutDayMessageGenerator = workoutDayMessageGenerator;
        this.workoutWeekMessageGenerator = workoutWeekMessageGenerator;
    }

    @Override
    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().startsWith("/select")) {
            return workoutDayMessageGenerator.getSelectWorkoutDayEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), callbackQuery.getData().split(":")[1]);
        } else if (callbackQuery.getData().startsWith("/set-name-request")) {
            return handelSetNameRequest(callbackQuery);
        } else if (callbackQuery.getData().startsWith("/set-rest-request")) {
            return handelSetRestRequest(callbackQuery);
        } else if (callbackQuery.getData().startsWith("/set-rest")) {
            return handelSetRest(callbackQuery);
        }

        return null;
    }

    @Override
    public CallbackHandlerType getHandlerType() {
        return CallbackHandlerType.WORKOUT_DAY_HANDLER;
    }

    private EditMessageText handelSetNameRequest(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getFrom().getId().toString();
        currentBotStateCache.setChatDataCurrentBotState(chatId, ChatState.SET_NAME_FOR_WORKOUT_DAY);
        selectedWorkoutDayCache.setSelectedWorkoutDay(chatId, callbackQuery.getData().split(":")[1]);

        EditMessageText editMessageText = new EditMessageText(localeMessageUtil.getMessage("reply.workout-day.set-name-request", chatId));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setReplyMarkup(mainMenuMessageGenerator.getBackButtonInlineKeyboard(chatId, "/select-WorkoutDay:" + callbackQuery.getData().split(":")[1]));

        return editMessageText;
    }


    private EditMessageText handelSetRestRequest(CallbackQuery callbackQuery){
        String dayId = callbackQuery.getData().split(":")[1];
        return mainMenuMessageGenerator.getAreYouSureMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), "/set-rest-WorkoutDay:" + dayId, "/select-WorkoutDay:" + dayId);
    }

    private EditMessageText handelSetRest(CallbackQuery callbackQuery) {
        workoutDayService.setRestWorkoutDay(callbackQuery.getData().split(":")[1]);
        return workoutWeekMessageGenerator.getCurrentWorkoutWeekEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId());
    }


}
