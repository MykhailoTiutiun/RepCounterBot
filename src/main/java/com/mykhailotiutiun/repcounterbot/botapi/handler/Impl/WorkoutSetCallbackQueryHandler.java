package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.cache.SelectedWorkoutExerciseCache;
import com.mykhailotiutiun.repcounterbot.cache.CurrentBotStateCache;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import com.mykhailotiutiun.repcounterbot.message.MainMenuMessageGenerator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class WorkoutSetCallbackQueryHandler implements CallbackQueryHandler {

    private final SelectedWorkoutExerciseCache selectedWorkoutExerciseCache;
    private final LocaleMessageUtil localeMessageUtil;
    private final MainMenuMessageGenerator mainMenuMessageGenerator;
    private final CurrentBotStateCache currentBotStateCache;

    public WorkoutSetCallbackQueryHandler(SelectedWorkoutExerciseCache selectedWorkoutExerciseCache, LocaleMessageUtil localeMessageUtil, MainMenuMessageGenerator mainMenuMessageGenerator, CurrentBotStateCache currentBotStateCache) {
        this.selectedWorkoutExerciseCache = selectedWorkoutExerciseCache;
        this.localeMessageUtil = localeMessageUtil;
        this.mainMenuMessageGenerator = mainMenuMessageGenerator;
        this.currentBotStateCache = currentBotStateCache;
    }

    @Override
    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().startsWith("/set-request")) {
            return handleFastSetsSetRequest(callbackQuery);
        }
        return null;
    }

    @Override
    public CallbackHandlerType getHandlerType() {
        return CallbackHandlerType.WORKOUT_SET_HANDLER;
    }

    private EditMessageText handleFastSetsSetRequest(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getFrom().getId().toString();
        currentBotStateCache.setChatDataCurrentBotState(chatId, ChatState.FAST_SETS_SET);
        selectedWorkoutExerciseCache.setSelectedWorkoutExercise(chatId, callbackQuery.getData().split(":")[1]);

        EditMessageText editMessageText = new EditMessageText(localeMessageUtil.getMessage("reply.workout-set.set", chatId));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setReplyMarkup(mainMenuMessageGenerator.getBackButtonInlineKeyboard(chatId, "/select-WorkoutExercise:" + callbackQuery.getData().split(":")[1]));
        return editMessageText;
    }
}
