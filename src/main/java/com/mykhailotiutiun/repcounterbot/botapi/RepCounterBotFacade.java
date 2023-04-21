package com.mykhailotiutiun.repcounterbot.botapi;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RepCounterBotFacade {

    final Map<MessageHandlerType, MessageHandler> messageHandlers = new HashMap<>();
    final Map<CallbackHandlerType, CallbackQueryHandler> callbackQueryHandlers = new HashMap<>();

    final ChatDataCache chatDataCache;

    public RepCounterBotFacade(List<MessageHandler> messageHandlers, List<CallbackQueryHandler> callbackQueryHandlers, ChatDataCache chatDataCache) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerType(), handler));
        callbackQueryHandlers.forEach(handler -> this.callbackQueryHandlers.put(handler.getHandlerType(), handler));
        this.chatDataCache = chatDataCache;
    }

    public SendMessage handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            return handleCallbackQuery(update.getCallbackQuery());
        } else {
            return handleMessage(update.getMessage());
        }

    }

    private SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        CallbackQueryHandler callbackQueryHandler = choseCallbackQueryHandler(callbackQuery);
        if (callbackQueryHandler == null) {
            return new SendMessage(callbackQuery.getFrom().getId().toString(), "Invalid Message");
        }

        log.trace("CallbackQuery request from @{}, with text {}", callbackQuery.getFrom().getUserName(), callbackQuery.getData());
        return callbackQueryHandler.handleCallbackQuery(callbackQuery);
    }

    private CallbackQueryHandler choseCallbackQueryHandler(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().contains("WorkoutDay")) {
            chatDataCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.MAIN_MENU);
            return callbackQueryHandlers.get(CallbackHandlerType.WORKOUT_DAY_HANDLER);
        }
        else if (callbackQuery.getData().contains("WorkoutExercise")) {
            chatDataCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.MAIN_MENU);
            return callbackQueryHandlers.get(CallbackHandlerType.WORKOUT_EXERCISE_HANDLER);
        } else if (callbackQuery.getData().contains("WorkoutSet")) {
            chatDataCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.MAIN_MENU);
            return callbackQueryHandlers.get(CallbackHandlerType.WORKOUT_SET_HANDLER);
        }

        return null;
    }


    private SendMessage handleMessage(Message message) {
        MessageHandler messageHandler = choseMessageHandler(message);
        if (messageHandler == null) {
            return new SendMessage(message.getChatId().toString(), "Invalid Message");
        }

        log.trace("Message request from @{}, with text {}", message.getFrom().getUserName(), message.getText());
        return messageHandler.handleMessage(message);
    }

    private MessageHandler choseMessageHandler(Message message) {
        if (message.getText() == null) {
            return null;
        }

        switch (message.getText()) {
            case ("/start"): {
                chatDataCache.setChatDataCurrentBotState(message.getChatId().toString(), ChatState.MAIN_MENU);
                return messageHandlers.get(MessageHandlerType.MAIN_MENU_HANDLER);
            }
            case ("Поточний тиждень тренувань"): {
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
