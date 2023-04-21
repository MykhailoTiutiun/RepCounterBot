package com.mykhailotiutiun.repcounterbot.service.Impl;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutExerciseRepository;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

    public WorkoutExerciseServiceImpl(WorkoutExerciseRepository workoutExerciseRepository, WorkoutSetService workoutSetService) {
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.workoutSetService = workoutSetService;
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

        workoutExercises.forEach(workoutExercise -> {
            workoutExercise.setId(null);
            workoutExercise.setWorkoutDay(newWorkoutDay);

            save(workoutExercise);
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
    public SendMessage getWorkoutExerciseMessage(String chatId, String workoutExerciseId) {
        WorkoutExercise workoutExercise = getWorkoutExerciseById(workoutExerciseId);

        SendMessage sendMessage = new SendMessage(chatId, workoutExercise.print());
        sendMessage.setReplyMarkup(getInlineKeyboardMarkupForWorkoutExercise(workoutExercise));
        return sendMessage;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkupForWorkoutExercise(WorkoutExercise workoutExercise) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        workoutExercise.getWorkoutSets().forEach(workoutSet -> {
            List<InlineKeyboardButton> setRow = new ArrayList<>();
            InlineKeyboardButton setButton = new InlineKeyboardButton(workoutSet.print());
            setButton.setCallbackData("/select-WorkoutSet:" + workoutSet.getId());
            setRow.add(setButton);
            keyboard.add(setRow);
        });

        List<InlineKeyboardButton> setSetsRequestButtonRow = new ArrayList<>();
        InlineKeyboardButton setSetsRequestButton = new InlineKeyboardButton("Вказати сети");
        setSetsRequestButton.setCallbackData("/set-request-WorkoutSet:" + workoutExercise.getId());
        setSetsRequestButtonRow.add(setSetsRequestButton);
        keyboard.add(setSetsRequestButtonRow);

        List<InlineKeyboardButton> deleteButtonRow = new ArrayList<>();
        InlineKeyboardButton deleteButton = new InlineKeyboardButton("Видалити");
        deleteButton.setCallbackData("/delete-WorkoutExercise:" + workoutExercise.getId());
        deleteButtonRow.add(deleteButton);
        keyboard.add(deleteButtonRow);

        return new InlineKeyboardMarkup(keyboard);
    }
}
