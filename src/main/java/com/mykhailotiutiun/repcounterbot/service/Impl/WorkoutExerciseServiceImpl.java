package com.mykhailotiutiun.repcounterbot.service.Impl;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutExerciseRepository;
import com.mykhailotiutiun.repcounterbot.service.LocaleMessageService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutSetService workoutSetService;
    private final LocaleMessageService localeMessageService;


    public WorkoutExerciseServiceImpl(WorkoutExerciseRepository workoutExerciseRepository, WorkoutSetService workoutSetService, LocaleMessageService localeMessageService) {
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.workoutSetService = workoutSetService;
        this.localeMessageService = localeMessageService;
    }

    @Override
    public WorkoutExercise getWorkoutExerciseById(String id) {
        log.trace("Get WorkoutExercise with id: {}", id);
        return sortWorkoutSetsInWorkoutExercise(workoutExerciseRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    private WorkoutExercise sortWorkoutSetsInWorkoutExercise(WorkoutExercise workoutExercise) {
        List<WorkoutSet> workoutSets = workoutExercise.getWorkoutSets();
        workoutSets.sort(Comparator.comparingInt(WorkoutSet::getNumber));
        return workoutExercise;
    }

    @Override
    public Byte getLatestWorkoutExerciseNumberByWorkoutDay(WorkoutDay workoutDay) {
        List<WorkoutExercise> workoutExercises = workoutExerciseRepository.findAllByWorkoutDayOrderByNumberDesc(workoutDay);
        if (workoutExercises.isEmpty()) {
            return 0;
        }

        return workoutExercises.get(0).getNumber();
    }

    @Override
    public List<WorkoutExercise> getWorkoutExerciseByWorkoutDay(WorkoutDay workoutDay) {
        log.trace("Get WorkoutExercises by WorkoutDay: {}", workoutDay);
        return workoutExerciseRepository.findAllByWorkoutDay(workoutDay);
    }

    @Override
    public List<WorkoutExercise> getAllWorkoutExercises() {
        log.trace("Get all WorkoutExercises");
        return workoutExerciseRepository.findAll();
    }

    @Override
    public void create(WorkoutExercise workoutExercise) {
        log.trace("Create WorkoutExercise: {}", workoutExercise);

        workoutExercise.setNumber((byte) (getLatestWorkoutExerciseNumberByWorkoutDay(workoutExercise.getWorkoutDay()) + 1));
        save(workoutExercise);
    }

    @Override
    public void createAllFromOldWorkoutDay(WorkoutDay oldWorkoutDay, WorkoutDay newWorkoutDay) {
        List<WorkoutExercise> workoutExercises = getWorkoutExerciseByWorkoutDay(oldWorkoutDay);

        workoutExercises.forEach(oldWorkoutExercise -> {
            WorkoutExercise newWorkoutExercise = new WorkoutExercise(oldWorkoutExercise.getNumber(), oldWorkoutExercise.getName(), newWorkoutDay, oldWorkoutExercise.getWorkoutSets());
            save(newWorkoutExercise);
        });
    }

    @Override
    public void save(WorkoutExercise workoutExercise) {
        log.trace("Save WorkoutExercise: {}", workoutExercise);
        workoutExerciseRepository.save(workoutExercise);
    }

    @Override
    public void addSetsToWorkoutExercise(String workoutExerciseId, List<WorkoutSet> workoutSets) {
        workoutSets.forEach(workoutSetService::save);

        WorkoutExercise workoutExercise = getWorkoutExerciseById(workoutExerciseId);
        workoutExercise.setWorkoutSets(workoutSets);
        save(workoutExercise);
    }

    @Override
    public void deleteById(String id) {
        log.trace("Delete WorkoutExercise with id: {}", id);
        workoutExerciseRepository.deleteById(id);
    }


    @Override
    public EditMessageText getWorkoutExerciseMessage(String chatId, Integer messageId, String workoutExerciseId) {
        WorkoutExercise workoutExercise = getWorkoutExerciseById(workoutExerciseId);

        EditMessageText editMessageText = new EditMessageText(workoutExercise.printForWorkoutExercise(localeMessageService.getMessage("print.workout-exercise.for-workout-exercise-reply", chatId), localeMessageService.getMessage("print.workout-set.pattern", chatId)));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setReplyMarkup(getInlineKeyboardMarkupForWorkoutExercise(chatId, workoutExercise));
        return editMessageText;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkupForWorkoutExercise(String chatId, WorkoutExercise workoutExercise) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        workoutExercise.getWorkoutSets().forEach(workoutSet -> {
            List<InlineKeyboardButton> setRow = new ArrayList<>();
            InlineKeyboardButton setButton = new InlineKeyboardButton(workoutSet.print(localeMessageService.getMessage("print.workout-set.pattern", chatId)));
            setButton.setCallbackData("/select-WorkoutSet:" + workoutSet.getId());
            setRow.add(setButton);
            keyboard.add(setRow);
        });

        List<InlineKeyboardButton> setSetsRequestButtonRow = new ArrayList<>();
        InlineKeyboardButton setSetsRequestButton = new InlineKeyboardButton(localeMessageService.getMessage("reply.workout-exercise.keyboard.set-sets-request", chatId));
        setSetsRequestButton.setCallbackData("/set-request-WorkoutSet:" + workoutExercise.getId());
        setSetsRequestButtonRow.add(setSetsRequestButton);
        keyboard.add(setSetsRequestButtonRow);

        List<InlineKeyboardButton> deleteButtonRow = new ArrayList<>();
        InlineKeyboardButton deleteButton = new InlineKeyboardButton(localeMessageService.getMessage("reply.delete", chatId));
        deleteButton.setCallbackData("/delete-WorkoutExercise:" + workoutExercise.getId());
        deleteButtonRow.add(deleteButton);
        keyboard.add(deleteButtonRow);

        List<InlineKeyboardButton> backButtonRow = new ArrayList<>();
        InlineKeyboardButton backButton = new InlineKeyboardButton(localeMessageService.getMessage("reply.keyboard.back", chatId));
        backButton.setCallbackData("/select-WorkoutDay:" + workoutExercise.getWorkoutDay().getId());
        backButtonRow.add(backButton);
        keyboard.add(backButtonRow);

        return new InlineKeyboardMarkup(keyboard);
    }
}
