package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.service.LocaleMessageService;
import com.mykhailotiutiun.repcounterbot.service.MainMenuService;
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
    private final MainMenuService mainMenuService;
    private final ChatDataCache chatDataCache;
    private final LocaleMessageService localeMessageService;

    public WorkoutDayCallbackQueryHandler(WorkoutDayService workoutDayService, WorkoutWeekService workoutWeekService, MainMenuService mainMenuService, ChatDataCache chatDataCache, LocaleMessageService localeMessageService) {
        this.workoutDayService = workoutDayService;
        this.workoutWeekService = workoutWeekService;
        this.mainMenuService = mainMenuService;
        this.chatDataCache = chatDataCache;
        this.localeMessageService = localeMessageService;
    }

    @Override
    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().startsWith("/select")) {
            return workoutDayService.getSelectWorkoutDayEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), callbackQuery.getData().split(":")[1]);
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
        chatDataCache.setChatDataCurrentBotState(chatId, ChatState.SET_NAME_FOR_WORKOUT_DAY);
        chatDataCache.setSelectedMessageId(chatId, callbackQuery.getMessage().getMessageId());
        chatDataCache.setSelectedWorkoutDay(chatId, callbackQuery.getData().split(":")[1]);

        EditMessageText editMessageText = new EditMessageText(localeMessageService.getMessage("reply.workout-day.set-name-request", chatId));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setReplyMarkup(mainMenuService.getBackButtonInlineKeyboard(chatId, "/select-WorkoutDay:" + callbackQuery.getData().split(":")[1]));

        return editMessageText;
    }


    private EditMessageText handelSetRestRequest(CallbackQuery callbackQuery){
        String dayId = callbackQuery.getData().split(":")[1];
        return mainMenuService.getAreYouSureMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId(), "/set-rest-WorkoutDay:" + dayId, "/select-WorkoutDay:" + dayId);
    }

    private EditMessageText handelSetRest(CallbackQuery callbackQuery) {
        workoutDayService.setRestWorkoutDay(callbackQuery.getData().split(":")[1]);
        return workoutWeekService.getCurrentWorkoutWeekEditMessage(callbackQuery.getFrom().getId().toString(), callbackQuery.getMessage().getMessageId());
    }


}
