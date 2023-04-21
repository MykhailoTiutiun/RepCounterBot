package com.mykhailotiutiun.repcounterbot.service.Impl;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutDayRepository;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
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
public class WorkoutDayServiceImpl implements WorkoutDayService {

    private final WorkoutDayRepository workoutDayRepository;
    private final WorkoutExerciseService workoutExerciseService;

    public WorkoutDayServiceImpl(WorkoutDayRepository workoutDayRepository, WorkoutExerciseService workoutExerciseService) {
        this.workoutDayRepository = workoutDayRepository;
        this.workoutExerciseService = workoutExerciseService;
    }

    @Override
    public WorkoutDay getWorkoutDayById(String id) {
        log.trace("Get WorkoutDay with id: {}", id);
        return workoutDayRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<WorkoutDay> getAllWorkoutDaysByWorkoutWeek(WorkoutWeek workoutWeek) {
        log.trace("Get WorkoutDays by WorkoutWeek: {}", workoutWeek);
        return workoutDayRepository.findAllByWorkoutWeek(workoutWeek);
    }

    @Override
    public List<WorkoutDay> getAllWorkoutDays() {
        log.trace("Get all WorkoutDays");
        return workoutDayRepository.findAll();
    }

    @Override
    public void createAllFromOldWorkoutWeek(WorkoutWeek oldWorkoutWeek, WorkoutWeek newWorkoutWeek) {
        List<WorkoutDay> workoutDays = getAllWorkoutDaysByWorkoutWeek(oldWorkoutWeek);

        for (WorkoutDay dayNumber : workoutDays) {
            WorkoutDay workoutDay = new WorkoutDay(dayNumber.getWorkoutWeek(), dayNumber.getName(), dayNumber.getDate(), dayNumber.getIsWorkoutDay());
            save(workoutDay);

            workoutExerciseService.createAllFromOldWorkoutDay(dayNumber, workoutDay);
        }


    }

    @Override
    public void save(WorkoutDay workoutWeek) {
        log.trace("Save WorkoutDay: {}", workoutWeek);
        workoutDayRepository.save(workoutWeek);
    }

    @Override
    public void setWorkoutDayName(String workoutDayId, String name) {
        log.trace("Set workout name {} to WorkoutDay by id: {}", name, workoutDayId);

        WorkoutDay workoutDay = getWorkoutDayById(workoutDayId);
        workoutDay.setName(name);
        workoutDay.setIsWorkoutDay(true);

        save(workoutDay);
    }

    @Override
    public void setRestWorkoutDay(String workoutDayId) {
        log.trace("Set rest for WorkoutDay by id: {}", workoutDayId);

        WorkoutDay workoutDay = getWorkoutDayById(workoutDayId);
        workoutDay.setIsWorkoutDay(false);

        save(workoutDay);
    }

    @Override
    public void deleteById(String id) {
        log.trace("Delete WorkoutDay with id: {}", id);
        workoutDayRepository.deleteById(id);
    }


    @Override
    public SendMessage getSelectWorkoutDaySendMessage(String chatId, String workoutDayId) {
        WorkoutDay workoutDay = getWorkoutDayById(workoutDayId);
        SendMessage sendMessage = new SendMessage(chatId, workoutDay.print());

        sendMessage.setReplyMarkup(getInlineKeyboardMarkupForWorkoutDay(workoutDay, workoutExerciseService.getWorkoutExerciseByWorkoutDay(workoutDay)));

        return sendMessage;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkupForWorkoutDay(WorkoutDay workoutDay, List<WorkoutExercise> workoutExercises) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        if (!workoutExercises.isEmpty()) {
            workoutExercises.sort(Comparator.comparingInt(WorkoutExercise::getNumber));

            workoutExercises.forEach(workoutExercise -> {
                List<InlineKeyboardButton> row = new ArrayList<>();

                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(workoutExercise.print());
                inlineKeyboardButton.setCallbackData("/select-WorkoutExercise:" + workoutExercise.getId());

                row.add(inlineKeyboardButton);

                keyboard.add(row);
            });
        }

        if ((workoutDay.isWorkoutDay() != null) && workoutDay.isWorkoutDay()) {
            List<InlineKeyboardButton> rowCreateWorkoutExercise = new ArrayList<>();
            InlineKeyboardButton buttonCreateWorkoutExercise = new InlineKeyboardButton("Додати врправу");
            buttonCreateWorkoutExercise.setCallbackData("/create-request-WorkoutExercise:" + workoutDay.getId());
            rowCreateWorkoutExercise.add(buttonCreateWorkoutExercise);
            keyboard.add(rowCreateWorkoutExercise);
        }

        List<InlineKeyboardButton> lastRow = new ArrayList<>();

        if (workoutDay.getIsWorkoutDay() != null && workoutDay.getIsWorkoutDay()) {
            InlineKeyboardButton setNameButton = new InlineKeyboardButton("Змінити назву");
            setNameButton.setCallbackData("/set-name-request-WorkoutDay:" + workoutDay.getId());
            lastRow.add(setNameButton);
        }


        if (workoutDay.getIsWorkoutDay() == null || !workoutDay.getIsWorkoutDay()) {
            InlineKeyboardButton setNameButton = new InlineKeyboardButton("Створити тренування");
            setNameButton.setCallbackData("/set-name-request-WorkoutDay:" + workoutDay.getId());
            lastRow.add(setNameButton);
        }

        if (workoutDay.getIsWorkoutDay() == null || workoutDay.getIsWorkoutDay()) {
            InlineKeyboardButton setRestDayButton = new InlineKeyboardButton("Зробити днем відпочинку");
            setRestDayButton.setCallbackData("/set-rest-WorkoutDay:" + workoutDay.getId());
            lastRow.add(setRestDayButton);
        }

        keyboard.add(lastRow);

        return new InlineKeyboardMarkup(keyboard);
    }
}
