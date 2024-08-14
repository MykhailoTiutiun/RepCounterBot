package com.mykhailotiutiun.repcounterbot.message.impl;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.message.WorkoutWeekMessageGenerator;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkoutWeekMessageGeneratorImpl implements WorkoutWeekMessageGenerator {

    private final LocaleMessageUtil localeMessageUtil;
    private final WorkoutDayService workoutDayService;
    private final WorkoutWeekService workoutWeekService;

    public WorkoutWeekMessageGeneratorImpl(LocaleMessageUtil localeMessageUtil, WorkoutDayService workoutDayService, WorkoutWeekService workoutWeekService) {
        this.localeMessageUtil = localeMessageUtil;
        this.workoutDayService = workoutDayService;
        this.workoutWeekService = workoutWeekService;
    }

    @Override
    public SendMessage getCurrentWorkoutWeekSendMessage(String chatId) {
        WorkoutWeek currentWorkoutWeek;
        try {
            currentWorkoutWeek = workoutWeekService.getCurrentWorkoutWeekByUserId(Long.valueOf(chatId));
        } catch (EntityNotFoundException e) {
            return new SendMessage(chatId, localeMessageUtil.getMessage("reply.error", chatId));
        }

        SendMessage sendMessage = new SendMessage(chatId, currentWorkoutWeek.print(localeMessageUtil.getMessage("print.workout-week", chatId)));

        sendMessage.setReplyMarkup(getInlineKeyboardForWeek(workoutDayService.getAllByWorkoutWeek(currentWorkoutWeek), chatId));

        return sendMessage;
    }

    @Override
    public EditMessageText getCurrentWorkoutWeekEditMessage(String chatId, Integer messageId){
        WorkoutWeek currentWorkoutWeek = workoutWeekService.getCurrentWorkoutWeekByUserId(Long.valueOf(chatId));

        EditMessageText editMessageText =new EditMessageText(currentWorkoutWeek.print(localeMessageUtil.getMessage("print.workout-week", chatId)));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setReplyMarkup(getInlineKeyboardForWeek(workoutDayService.getAllByWorkoutWeek(currentWorkoutWeek), chatId));
        return editMessageText;
    }

    private InlineKeyboardMarkup getInlineKeyboardForWeek(List<WorkoutDay> workoutDays, String chatId) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        workoutDays.forEach(workoutDay -> {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton(workoutDay.print(localeMessageUtil.getMessage("print.workout-day.is-rest-day", chatId), localeMessageUtil.getMessage("print.workout-day.type-not-set", chatId), localeMessageUtil.getLocalTag(chatId)));
            keyboardButton.setCallbackData("/select-WorkoutDay:" + workoutDay.getId());

            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
            inlineKeyboardButtons.add(keyboardButton);

            keyboard.add(inlineKeyboardButtons);
        });

        return new InlineKeyboardMarkup(keyboard);
    }
}
