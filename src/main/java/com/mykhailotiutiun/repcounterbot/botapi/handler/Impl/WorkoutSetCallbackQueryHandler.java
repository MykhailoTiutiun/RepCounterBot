package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.service.LocaleMessageService;
import com.mykhailotiutiun.repcounterbot.service.MainMenuService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class WorkoutSetCallbackQueryHandler implements CallbackQueryHandler {

    private final ChatDataCache chatDataCache;
    private final LocaleMessageService localeMessageService;
    private final MainMenuService mainMenuService;

    public WorkoutSetCallbackQueryHandler(ChatDataCache chatDataCache, LocaleMessageService localeMessageService, MainMenuService mainMenuService) {
        this.chatDataCache = chatDataCache;
        this.localeMessageService = localeMessageService;
        this.mainMenuService = mainMenuService;
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
        chatDataCache.setChatDataCurrentBotState(chatId, ChatState.FAST_SETS_SET);
        chatDataCache.setSelectedMessageId(chatId, callbackQuery.getMessage().getMessageId());
        chatDataCache.setSelectedWorkoutExercise(chatId, callbackQuery.getData().split(":")[1]);

        EditMessageText editMessageText = new EditMessageText(localeMessageService.getMessage("reply.workout-set.set", chatId));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setReplyMarkup(mainMenuService.getBackButtonInlineKeyboard(chatId, "/select-WorkoutExercise:" + callbackQuery.getData().split(":")[1]));
        return editMessageText;
    }
}
