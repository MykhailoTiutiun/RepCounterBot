package com.mykhailotiutiun.repcounterbot.botapi;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.cache.CurrentBotStateCache;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RepCounterBotFacade {

    private final Map<MessageHandlerType, MessageHandler> messageHandlers = new HashMap<>();
    private final Map<CallbackHandlerType, CallbackQueryHandler> callbackQueryHandlers = new HashMap<>();

    private final LocaleMessageUtil localeMessageUtil;
    private final CurrentBotStateCache currentBotStateCache;

    public RepCounterBotFacade(List<MessageHandler> messageHandlers, List<CallbackQueryHandler> callbackQueryHandlers, LocaleMessageUtil localeMessageUtil, CurrentBotStateCache currentBotStateCache) {
        this.localeMessageUtil = localeMessageUtil;
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerType(), handler));
        callbackQueryHandlers.forEach(handler -> this.callbackQueryHandlers.put(handler.getHandlerType(), handler));
        this.currentBotStateCache = currentBotStateCache;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            return handleCallbackQuery(update.getCallbackQuery());
        } else {
            return handleMessage(update.getMessage());
        }
    }

    private BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        CallbackQueryHandler callbackQueryHandler = choseCallbackQueryHandler(callbackQuery);
        if (callbackQueryHandler == null) {
            return new SendMessage(callbackQuery.getFrom().getId().toString(), localeMessageUtil.getMessage("reply.error", callbackQuery.getFrom().getId().toString()));
        }

        log.trace("CallbackQuery request from @{}, with text {}", callbackQuery.getFrom().getUserName(), callbackQuery.getData());
        return callbackQueryHandler.handleCallbackQuery(callbackQuery);
    }

    private CallbackQueryHandler choseCallbackQueryHandler(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().contains("Main")) {
            currentBotStateCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.MAIN_MENU);
            return callbackQueryHandlers.get(CallbackHandlerType.MAIN_MENU_HANDLER);
        } else if(callbackQuery.getData().contains("WorkoutWeek")) {
            currentBotStateCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.MAIN_MENU);
            return callbackQueryHandlers.get(CallbackHandlerType.WORKOUT_WEEK_HANDLER);
        } else if (callbackQuery.getData().contains("WorkoutDay")) {
            currentBotStateCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.MAIN_MENU);
            return callbackQueryHandlers.get(CallbackHandlerType.WORKOUT_DAY_HANDLER);
        } else if (callbackQuery.getData().contains("WorkoutExercise")) {
            currentBotStateCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.MAIN_MENU);
            return callbackQueryHandlers.get(CallbackHandlerType.WORKOUT_EXERCISE_HANDLER);
        } else if (callbackQuery.getData().contains("WorkoutSet")) {
            currentBotStateCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.MAIN_MENU);
            return callbackQueryHandlers.get(CallbackHandlerType.WORKOUT_SET_HANDLER);
        }

        return null;
    }


    private BotApiMethod<?> handleMessage(Message message) {
        MessageHandler messageHandler = choseMessageHandler(message);
        if (messageHandler == null) {
            return new SendMessage(message.getChatId().toString(), localeMessageUtil.getMessage("reply.invalid-message", message.getChatId().toString()));
        }

        log.trace("Message request from @{}, with text {}", message.getFrom().getUserName(), message.getText());
        return messageHandler.handleMessage(message);
    }

    private MessageHandler choseMessageHandler(Message message) {
        if (message.getText() == null) {
            return null;
        }

        switch (message.getText()) {
            case ("/start"):
            case ("Choose a language"):
            case ("Обрати мову"): {
                currentBotStateCache.setChatDataCurrentBotState(message.getChatId().toString(), ChatState.MAIN_MENU);
                return messageHandlers.get(MessageHandlerType.MAIN_MENU_HANDLER);
            }
            case ("Поточний тиждень"):
            case ("Current week"): {
                currentBotStateCache.setChatDataCurrentBotState(message.getChatId().toString(), ChatState.MAIN_MENU);
                return messageHandlers.get(MessageHandlerType.CURRENT_WEEK_HANDLER);
            }
            default:
                break;
        }

        switch (currentBotStateCache.getChatDataCurrentBotState(message.getChatId().toString())) {
            case SET_NAME_FOR_WORKOUT_DAY:
                return messageHandlers.get(MessageHandlerType.WORKOUT_DAY_HANDLER);
            case CREATE_WORKOUT_EXERCISE:
            case CHANGE_WORKOUT_EXERCISE_NAME:
                return messageHandlers.get(MessageHandlerType.WORKOUT_EXERCISE_HANDLER);
            case FAST_SETS_SET:
                return messageHandlers.get(MessageHandlerType.WORKOUT_SET_HANDLER);
            default:
                break;
        }

        return null;
    }
}
