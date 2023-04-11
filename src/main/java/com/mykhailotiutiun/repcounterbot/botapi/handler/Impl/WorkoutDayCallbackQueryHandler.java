package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.CallbackQueryHandler;
import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.constants.CallbackHandlerType;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkoutDayCallbackQueryHandler implements CallbackQueryHandler {

    private final WorkoutDayService workoutDayService;
    private final ChatDataCache chatDataCache;

    public WorkoutDayCallbackQueryHandler(WorkoutDayService workoutDayService, ChatDataCache chatDataCache) {
        this.workoutDayService = workoutDayService;
        this.chatDataCache = chatDataCache;
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().startsWith("/select-WorkoutDay")){
            return handleSelectWorkoutDay(callbackQuery);
        } else if (callbackQuery.getData().startsWith("/set-name-request")) {
            return handelSetNameRequest(callbackQuery);
        }

        return null;
    }

    @Override
    public CallbackHandlerType getHandlerType() {
        return CallbackHandlerType.WORKOUT_DAY_HANDLER;
    }

    private SendMessage handleSelectWorkoutDay(CallbackQuery callbackQuery){
        WorkoutDay workoutDay = workoutDayService.getWorkoutDayById(callbackQuery.getData().split(":")[1]);
        SendMessage sendMessage = new SendMessage(callbackQuery.getFrom().getId().toString(), workoutDay.print());

        sendMessage.setReplyMarkup(getInlineKeyboardMarkupForWorkoutDay(workoutDay));

        return sendMessage;
    }

    private SendMessage handelSetNameRequest(CallbackQuery callbackQuery){
        chatDataCache.setChatDataCurrentBotState(callbackQuery.getFrom().getId().toString(), ChatState.SET_NAME_FOR_WORKOUT_DAY);

        return new SendMessage(callbackQuery.getFrom().getId().toString(), "Введіть назву тренування");
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkupForWorkoutDay(WorkoutDay workoutDay){
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> lastRow = new ArrayList<>();

//        if (workoutDay.getIsWorkoutDay() != null && workoutDay.getIsWorkoutDay()) {
//
//
//        }


        if(workoutDay.getIsWorkoutDay() == null || !workoutDay.getIsWorkoutDay()) {
            InlineKeyboardButton setNameButton = new InlineKeyboardButton("Створити тренування");
            setNameButton.setCallbackData("/set-name-request-WorkoutDay:"+ workoutDay.getId());
            lastRow.add(setNameButton);
        }

        if (workoutDay.getIsWorkoutDay() == null || workoutDay.getIsWorkoutDay()){
            InlineKeyboardButton setRestDayButton = new InlineKeyboardButton("Зробити днем відпочинку");
            setRestDayButton.setCallbackData("/set-rest-day-WorkoutDay:"+ workoutDay.getId());
            lastRow.add(setRestDayButton);
        }

        keyboard.add(lastRow);

        return new InlineKeyboardMarkup(keyboard);
    }


}
