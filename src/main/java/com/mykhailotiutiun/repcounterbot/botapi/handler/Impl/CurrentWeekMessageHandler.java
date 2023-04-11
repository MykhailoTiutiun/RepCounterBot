package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class CurrentWeekMessageHandler implements MessageHandler {

    private final WorkoutWeekService workoutWeekService;
    private final WorkoutDayService workoutDayService;

    public CurrentWeekMessageHandler(WorkoutWeekService workoutWeekService, WorkoutDayService workoutDayService) {
        this.workoutWeekService = workoutWeekService;
        this.workoutDayService = workoutDayService;
    }

    @Override
    public SendMessage handleMessage(Message message) {
        WorkoutWeek currentWorkoutWeek;
        try {
            currentWorkoutWeek = workoutWeekService.getWorkoutWeekByUserIdAndLocalDate(message.getFrom().getId(), LocalDate.now());
        } catch (EntityNotFoundException e){
            return new SendMessage(message.getChatId().toString(), "Щось пішло не так");
        }

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), String.format("Поточний тиждень тренувань з %s, по %s", currentWorkoutWeek.getWeekStartDate().format(DateTimeFormatter.ISO_DATE), currentWorkoutWeek.getWeekEndDate().format(DateTimeFormatter.ISO_DATE)));

        sendMessage.setReplyMarkup(getInlineKeyboardForWeek(workoutDayService.getWorkoutDaysByWorkoutWeekId(currentWorkoutWeek)));

        return sendMessage;
    }

    @Override
    public MessageHandlerType getHandlerType() {
        return MessageHandlerType.CURRENT_WEEK;
    }


    private InlineKeyboardMarkup getInlineKeyboardForWeek(List<WorkoutDay> workoutDays){
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        workoutDays.forEach(workoutDay -> {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton(workoutDay.print());
            keyboardButton.setCallbackData("/select-WorkoutDay:" + workoutDay.getId());

            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
            inlineKeyboardButtons.add(keyboardButton);

            keyboard.add(inlineKeyboardButtons);
        });

        return new InlineKeyboardMarkup(keyboard);
    }


}
