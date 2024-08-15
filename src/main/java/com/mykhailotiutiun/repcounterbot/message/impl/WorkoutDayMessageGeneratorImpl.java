package com.mykhailotiutiun.repcounterbot.message.impl;

import com.mykhailotiutiun.repcounterbot.message.WorkoutDayMessageGenerator;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class WorkoutDayMessageGeneratorImpl implements WorkoutDayMessageGenerator {

    private final WorkoutDayService workoutDayService;
    private final LocaleMessageUtil localeMessageUtil;
    private final WorkoutExerciseService workoutExerciseService;

    public WorkoutDayMessageGeneratorImpl(WorkoutDayService workoutDayService, LocaleMessageUtil localeMessageUtil, WorkoutExerciseService workoutExerciseService) {
        this.workoutDayService = workoutDayService;
        this.localeMessageUtil = localeMessageUtil;
        this.workoutExerciseService = workoutExerciseService;
    }

    @Override
    public SendMessage getSelectWorkoutDaySendMessage(String chatId, Long workoutDayId) {
        WorkoutDay workoutDay = workoutDayService.getById(workoutDayId);
        SendMessage sendMessage = new SendMessage(chatId, workoutDay.print(localeMessageUtil.getMessage("print.workout-day.is-rest-day", chatId), localeMessageUtil.getMessage("print.workout-day.type-not-set", chatId), localeMessageUtil.getLocalTag(chatId)));
        sendMessage.setReplyMarkup(getInlineKeyboardMarkupForWorkoutDay(workoutDay, workoutExerciseService.getAllByWorkoutDay(workoutDay), chatId));

        return sendMessage;
    }

    @Override
    public EditMessageText getSelectWorkoutDayEditMessage(String chatId, Integer messageId, Long workoutDayId) {
        WorkoutDay workoutDay = workoutDayService.getById(workoutDayId);
        EditMessageText editMessageText = new EditMessageText(workoutDay.print(localeMessageUtil.getMessage("print.workout-day.is-rest-day", chatId), localeMessageUtil.getMessage("print.workout-day.type-not-set", chatId), localeMessageUtil.getLocalTag(chatId)));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setReplyMarkup(getInlineKeyboardMarkupForWorkoutDay(workoutDay, workoutExerciseService.getAllByWorkoutDay(workoutDay), chatId));

        return editMessageText;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkupForWorkoutDay(WorkoutDay workoutDay, List<WorkoutExercise> workoutExercises, String chatId) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        if (!workoutExercises.isEmpty()) {
            workoutExercises.sort(Comparator.comparingInt(WorkoutExercise::getNumber));

            workoutExercises.forEach(workoutExercise -> {
                List<InlineKeyboardButton> row = new ArrayList<>();

                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(workoutExercise.printForWorkoutDayKeyboard());
                inlineKeyboardButton.setCallbackData("/select-WorkoutExercise:" + workoutExercise.getId());
                row.add(inlineKeyboardButton);

                keyboard.add(row);
            });
        }

        // Add exercise
        if ((workoutDay.isWorkoutDay() != null) && workoutDay.isWorkoutDay()) {
            List<InlineKeyboardButton> rowCreateWorkoutExercise = new ArrayList<>();
            InlineKeyboardButton buttonCreateWorkoutExercise = new InlineKeyboardButton(localeMessageUtil.getMessage("reply.workout-day.keyboard.add-exercise", chatId));
            buttonCreateWorkoutExercise.setCallbackData("/create-request-WorkoutExercise:" + workoutDay.getId());
            rowCreateWorkoutExercise.add(buttonCreateWorkoutExercise);
            keyboard.add(rowCreateWorkoutExercise);
        }

        List<InlineKeyboardButton> lastRow = new ArrayList<>();

        //Change name
        if (workoutDay.getIsWorkoutDay() != null && workoutDay.getIsWorkoutDay()) {
            InlineKeyboardButton setNameButton = new InlineKeyboardButton(localeMessageUtil.getMessage("reply.change-name", chatId));
            setNameButton.setCallbackData("/set-name-request-WorkoutDay:" + workoutDay.getId());
            lastRow.add(setNameButton);
        }

        //Create workout
        if (workoutDay.getIsWorkoutDay() == null || !workoutDay.getIsWorkoutDay()) {
            InlineKeyboardButton setNameButton = new InlineKeyboardButton(localeMessageUtil.getMessage("reply.workout-day.keyboard.create-workout-request", chatId));
            setNameButton.setCallbackData("/set-name-request-WorkoutDay:" + workoutDay.getId());
            lastRow.add(setNameButton);
        }

        //Set rest day
        if (workoutDay.getIsWorkoutDay() == null || workoutDay.getIsWorkoutDay()) {
            InlineKeyboardButton setRestDayButton = new InlineKeyboardButton(localeMessageUtil.getMessage("reply.workout-day.keyboard.set-as-rest-day", chatId));
            setRestDayButton.setCallbackData("/set-rest-request-WorkoutDay:" + workoutDay.getId());
            lastRow.add(setRestDayButton);
        }

        keyboard.add(lastRow);

        List<InlineKeyboardButton> backButtonRow = new ArrayList<>();
        InlineKeyboardButton backButton = new InlineKeyboardButton(localeMessageUtil.getMessage("reply.keyboard.back", chatId));
        backButton.setCallbackData("/select-current-WorkoutWeek");
        backButtonRow.add(backButton);

        keyboard.add(backButtonRow);
        return new InlineKeyboardMarkup(keyboard);
    }
}
