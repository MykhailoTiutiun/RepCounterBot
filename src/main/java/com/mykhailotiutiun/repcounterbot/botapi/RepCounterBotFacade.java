package com.mykhailotiutiun.repcounterbot.botapi;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.service.LocaleMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
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

    private final LocaleMessageService localeMessageService;
    private final ChatDataCache chatDataCache;

    public RepCounterBotFacade(List<MessageHandler> messageHandlers, List<CallbackQueryHandler> callbackQueryHandlers, LocaleMessageService localeMessageService, ChatDataCache chatDataCache) {
        this.localeMessageService = localeMessageService;
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerType(), handler));
        callbackQueryHandlers.forEach(handler -> this.callbackQueryHandlers.put(handler.getHandlerType(), handler));
        this.chatDataCache = chatDataCache;
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
            return new SendMessage(callbackQuery.getFrom().getId().toString(), localeMessageService.getMessage("reply.error", callbackQuery.getFrom().getId().toString()));
        }

        log.trace("CallbackQuery request from @{}, with text {}", callbackQuery.getFrom().getUserName(), callbackQuery.getData());
        return callbackQueryHandler.handleCallbackQuery(callbackQuery);
    }

    private CallbackQueryHandler choseCallbackQueryHandler(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().contains("Main")) {
            chatDataCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.MAIN_MENU);
            return callbackQueryHandlers.get(CallbackHandlerType.MAIN_MENU_HANDLER);
        } else if(callbackQuery.getData().contains("WorkoutWeek")) {
            chatDataCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.MAIN_MENU);
            return callbackQueryHandlers.get(CallbackHandlerType.WORKOUT_WEEK_HANDLER);
        } else if (callbackQuery.getData().contains("WorkoutDay")) {
            chatDataCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.MAIN_MENU);
            return callbackQueryHandlers.get(CallbackHandlerType.WORKOUT_DAY_HANDLER);
        } else if (callbackQuery.getData().contains("WorkoutExercise")) {
            chatDataCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.MAIN_MENU);
            return callbackQueryHandlers.get(CallbackHandlerType.WORKOUT_EXERCISE_HANDLER);
        } else if (callbackQuery.getData().contains("WorkoutSet")) {
            chatDataCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.MAIN_MENU);
            return callbackQueryHandlers.get(CallbackHandlerType.WORKOUT_SET_HANDLER);
        }

        return null;
    }


    private BotApiMethod<?> handleMessage(Message message) {
        MessageHandler messageHandler = choseMessageHandler(message);
        if (messageHandler == null) {
            return new SendMessage(message.getChatId().toString(), localeMessageService.getMessage("reply.invalid-message", message.getChatId().toString()));
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
                chatDataCache.setChatDataCurrentBotState(message.getChatId().toString(), ChatState.MAIN_MENU);
                return messageHandlers.get(MessageHandlerType.MAIN_MENU_HANDLER);
            }
            case ("Поточний тиждень тренувань"):
            case ("Current workout week"): {
                chatDataCache.setChatDataCurrentBotState(message.getChatId().toString(), ChatState.MAIN_MENU);
                return messageHandlers.get(MessageHandlerType.CURRENT_WEEK_HANDLER);
            }
            default:
                break;
        }

        switch (chatDataCache.getChatDataCurrentBotState(message.getChatId().toString())) {
            case SET_NAME_FOR_WORKOUT_DAY:
                return messageHandlers.get(MessageHandlerType.WORKOUT_DAY_HANDLER);
            case CREATE_WORKOUT_EXERCISE:
                return messageHandlers.get(MessageHandlerType.WORKOUT_EXERCISE_HANDLER);
            case FAST_SETS_SET:
                return messageHandlers.get(MessageHandlerType.WORKOUT_SET_HANDLER);
            default:
                break;
        }

        return null;
    }
}
