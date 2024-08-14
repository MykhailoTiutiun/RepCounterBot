package com.mykhailotiutiun.repcounterbot.service.Impl;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutExerciseRepository;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutSetService;
import com.mykhailotiutiun.repcounterbot.util.LocaleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final LocaleMessageUtil localeMessageUtil;


    public WorkoutExerciseServiceImpl(WorkoutExerciseRepository workoutExerciseRepository, WorkoutSetService workoutSetService, LocaleMessageUtil localeMessageUtil) {
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.workoutSetService = workoutSetService;
        this.localeMessageUtil = localeMessageUtil;
    }

    @Override
    public WorkoutExercise getById(String id) {
        log.trace("Get WorkoutExercise by id: {}", id);
        return sortWorkoutSetsInWorkoutExercise(workoutExerciseRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    private WorkoutExercise sortWorkoutSetsInWorkoutExercise(WorkoutExercise workoutExercise) {
        List<WorkoutSet> workoutSets = workoutExercise.getWorkoutSets();
        workoutSets.sort(Comparator.comparingInt(WorkoutSet::getNumber));
        return workoutExercise;
    }

    @Override
    public Byte getLatestNumberByWorkoutDay(WorkoutDay workoutDay) {
        log.trace("Get WorkoutExercise latest number by WorkoutDay: {}", workoutDay);
        List<WorkoutExercise> workoutExercises = workoutExerciseRepository.findAllByWorkoutDayOrderByNumberDesc(workoutDay);
        if (workoutExercises.isEmpty()) {
            return 0;
        }

        return workoutExercises.get(0).getNumber();
    }

    @Override
    public WorkoutExercise getByNumberAndWorkoutDay(Byte number, WorkoutDay workoutDay){
        log.trace("Get WorkoutExercise by number and WorkoutDay: {}, {}", number, workoutDay);
        return workoutExerciseRepository.findByNumberAndWorkoutDay(number, workoutDay).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<WorkoutExercise> getAllByWorkoutDay(WorkoutDay workoutDay) {
        log.trace("Get WorkoutExercises by WorkoutDay: {}", workoutDay);
        return workoutExerciseRepository.findAllByWorkoutDay(workoutDay);
    }

    @Override
    @Transactional
    public void create(WorkoutExercise workoutExercise) {
        log.trace("Create WorkoutExercise: {}", workoutExercise);

        workoutExercise.setNumber((byte) (getLatestNumberByWorkoutDay(workoutExercise.getWorkoutDay()) + 1));
        save(workoutExercise);
    }

    @Override
    @Transactional
    public void createAllFromOldWorkoutDay(WorkoutDay oldWorkoutDay, WorkoutDay newWorkoutDay) {
        log.trace("Create all WorkoutExercise from old WorkoutDay: {}, {}", oldWorkoutDay, newWorkoutDay);
        List<WorkoutExercise> workoutExercises = getAllByWorkoutDay(oldWorkoutDay);

        workoutExercises.forEach(oldWorkoutExercise -> {
            WorkoutExercise newWorkoutExercise = WorkoutExercise.builder()
                    .number(oldWorkoutExercise.getNumber())
                    .name(oldWorkoutExercise.getName())
                    .workoutDay(newWorkoutDay)
                    .prevWorkoutSets(oldWorkoutExercise.getWorkoutSets())
                    .build();
            save(newWorkoutExercise);
        });
    }

    private void save(WorkoutExercise workoutExercise) {
        log.trace("Save WorkoutExercise: {}", workoutExercise);
        workoutExerciseRepository.save(workoutExercise);
    }

    @Override
    @Transactional
    public void setName(String workoutExerciseId, String name){
        WorkoutExercise workoutExercise = getById(workoutExerciseId);
        log.trace("Set name WorkoutExercise: {}, {}", workoutExercise, name);

        workoutExercise.setName(name);
        save(workoutExercise);
    }

    @Override
    @Transactional
    public void moveUp(String workoutExerciseId){
        WorkoutExercise workoutExercise = getById(workoutExerciseId);
        log.trace("Move up WorkoutExercise: {}", workoutExercise);

        WorkoutExercise swappableWorkoutExercise = getByNumberAndWorkoutDay((byte) (workoutExercise.getNumber() - 1), workoutExercise.getWorkoutDay());
        swappableWorkoutExercise.setNumber((byte) (swappableWorkoutExercise.getNumber() + 1));
        save(swappableWorkoutExercise);

        workoutExercise.setNumber((byte) (workoutExercise.getNumber() - 1));
        save(workoutExercise);
    }

    @Override
    @Transactional
    public void moveDown(String workoutExerciseId){
        WorkoutExercise workoutExercise = getById(workoutExerciseId);
        log.trace("Move down WorkoutExercise: {}", workoutExercise);

        WorkoutExercise swappableWorkoutExercise = getByNumberAndWorkoutDay((byte) (workoutExercise.getNumber() + 1), workoutExercise.getWorkoutDay());
        swappableWorkoutExercise.setNumber((byte) (swappableWorkoutExercise.getNumber() - 1));
        save(swappableWorkoutExercise);
        workoutExercise.setNumber((byte) (workoutExercise.getNumber() + 1));
        save(workoutExercise);
    }

    @Override
    @Transactional
    public void addSets(String workoutExerciseId, List<WorkoutSet> workoutSets) {
        WorkoutExercise workoutExercise = getById(workoutExerciseId);
        log.trace("Add WorkoutSets to WorkoutExercise: {}, {}", workoutExercise, workoutSets);

        workoutSets.forEach(workoutSetService::save);

        workoutExercise.setWorkoutSets(workoutSets);
        save(workoutExercise);
    }

    @Override
    public void deleteById(String id) {
        log.trace("Delete WorkoutExercise with id: {}", id);
        workoutExerciseRepository.deleteById(id);
    }
}