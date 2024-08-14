package com.mykhailotiutiun.repcounterbot.message.impl;

import com.mykhailotiutiun.repcounterbot.message.WorkoutExerciseMessageGenerator;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkoutExerciseMessageGeneratorImpl implements WorkoutExerciseMessageGenerator {

    private final LocaleMessageUtil localeMessageUtil;
    private final WorkoutExerciseService workoutExerciseService;

    public WorkoutExerciseMessageGeneratorImpl(LocaleMessageUtil localeMessageUtil, WorkoutExerciseService workoutExerciseService) {
        this.localeMessageUtil = localeMessageUtil;
        this.workoutExerciseService = workoutExerciseService;
    }

    @Override
    public SendMessage getWorkoutExerciseSendMessage(String chatId, String workoutExerciseId) {
        WorkoutExercise workoutExercise = workoutExerciseService.getById(workoutExerciseId);

        SendMessage sendMessage = new SendMessage(chatId, workoutExercise.printForWorkoutExercise(localeMessageUtil.getMessage("print.workout-exercise.for-workout-exercise-reply", chatId), localeMessageUtil.getMessage("print.workout-set.pattern", chatId)));
        sendMessage.setReplyMarkup(getInlineKeyboardMarkupForWorkoutExercise(chatId, workoutExercise));
        return sendMessage;
    }

    @Override
    public EditMessageText getWorkoutExerciseEditMessage(String chatId, Integer messageId, String workoutExerciseId, Boolean isOnEditPage) {
        WorkoutExercise workoutExercise = workoutExerciseService.getById(workoutExerciseId);

        EditMessageText editMessageText = new EditMessageText(workoutExercise.printForWorkoutExercise(localeMessageUtil.getMessage("print.workout-exercise.for-workout-exercise-reply", chatId), localeMessageUtil.getMessage("print.workout-set.pattern", chatId)));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);

        if(isOnEditPage){
            editMessageText.setReplyMarkup(getInlineKeyboardMarkupForEditWorkoutExercise(chatId, workoutExercise));
        } else {
            editMessageText.setReplyMarkup(getInlineKeyboardMarkupForWorkoutExercise(chatId, workoutExercise));
        }
        return editMessageText;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkupForWorkoutExercise(String chatId, WorkoutExercise workoutExercise) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        workoutExercise.getWorkoutSets().forEach(workoutSet -> {
            keyboard.add(getButtonRow(workoutSet.print(localeMessageUtil.getMessage("print.workout-set.pattern", chatId)), "/select-WorkoutSet:" + workoutSet.getId()));
        });

        keyboard.add(getButtonRow(localeMessageUtil.getMessage("reply.workout-exercise.keyboard.set-sets-request", chatId), "/set-request-WorkoutSet:" + workoutExercise.getId()));
        keyboard.add(getButtonRow(localeMessageUtil.getMessage("reply.workout-exercise.keyboard.edit", chatId), "/edit-WorkoutExercise:" + workoutExercise.getId()));
        keyboard.add(getButtonRow(localeMessageUtil.getMessage("reply.keyboard.back", chatId), "/select-WorkoutDay:" + workoutExercise.getWorkoutDay().getId()));

        return new InlineKeyboardMarkup(keyboard);
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkupForEditWorkoutExercise(String chatId, WorkoutExercise workoutExercise) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        if(workoutExercise.getNumber() > 1){
            keyboard.add(getButtonRow(localeMessageUtil.getMessage("reply.keyboard.move-up", chatId), "/move-up-request-WorkoutExercise:" + workoutExercise.getId()));
        }

        if(workoutExercise.getNumber() < workoutExerciseService.getLatestNumberByWorkoutDay(workoutExercise.getWorkoutDay())){
            keyboard.add(getButtonRow(localeMessageUtil.getMessage("reply.keyboard.move-down", chatId), "/move-down-request-WorkoutExercise:" + workoutExercise.getId()));
        }
        keyboard.add(getButtonRow(localeMessageUtil.getMessage("reply.change-name", chatId), "/change-name-request-WorkoutExercise:" + workoutExercise.getId()));
        keyboard.add(getButtonRow(localeMessageUtil.getMessage("reply.delete", chatId), "/delete-request-WorkoutExercise:" + workoutExercise.getId()));
        keyboard.add(getButtonRow(localeMessageUtil.getMessage("reply.keyboard.back", chatId), "/select-WorkoutExercise:" + workoutExercise.getId()));

        return new InlineKeyboardMarkup(keyboard);
    }

    private List<InlineKeyboardButton> getButtonRow(String buttonText, String buttonCallback){
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton(buttonText);
        button.setCallbackData(buttonCallback);
        buttonRow.add(button);
        return buttonRow;
    }
}
