package com.mykhailotiutiun.repcounterbot.service.Impl;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutDayRepository;
import com.mykhailotiutiun.repcounterbot.service.LocaleMessageService;
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
    private final LocaleMessageService localeMessageService;

    public WorkoutDayServiceImpl(WorkoutDayRepository workoutDayRepository, WorkoutExerciseService workoutExerciseService, LocaleMessageService localeMessageService) {
        this.workoutDayRepository = workoutDayRepository;
        this.workoutExerciseService = workoutExerciseService;
        this.localeMessageService = localeMessageService;
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

        for (int i = 0; i < workoutDays.size(); i++) {
            WorkoutDay workoutDayOld = workoutDays.get(i);
            WorkoutDay workoutDay = new WorkoutDay(newWorkoutWeek, workoutDayOld.getName(), newWorkoutWeek.getWeekStartDate().plusDays(i), workoutDayOld.getIsWorkoutDay());
            save(workoutDay);

            workoutExerciseService.createAllFromOldWorkoutDay(workoutDayOld, workoutDay);
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
        SendMessage sendMessage = new SendMessage(chatId, workoutDay.print(localeMessageService.getMessage("print.workout-day.is-rest-day", chatId), localeMessageService.getMessage("print.workout-day.type-not-set", chatId), localeMessageService.getLocalTag(chatId)));

        sendMessage.setReplyMarkup(getInlineKeyboardMarkupForWorkoutDay(workoutDay, workoutExerciseService.getWorkoutExerciseByWorkoutDay(workoutDay), chatId));

        return sendMessage;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkupForWorkoutDay(WorkoutDay workoutDay, List<WorkoutExercise> workoutExercises, String chatId) {
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

        // Add exercise
        if ((workoutDay.isWorkoutDay() != null) && workoutDay.isWorkoutDay()) {
            List<InlineKeyboardButton> rowCreateWorkoutExercise = new ArrayList<>();
            InlineKeyboardButton buttonCreateWorkoutExercise = new InlineKeyboardButton(localeMessageService.getMessage("reply.workout-day.keyboard.add-exercise", chatId));
            buttonCreateWorkoutExercise.setCallbackData("/create-request-WorkoutExercise:" + workoutDay.getId());
            rowCreateWorkoutExercise.add(buttonCreateWorkoutExercise);
            keyboard.add(rowCreateWorkoutExercise);
        }

        List<InlineKeyboardButton> lastRow = new ArrayList<>();

        //Change name
        if (workoutDay.getIsWorkoutDay() != null && workoutDay.getIsWorkoutDay()) {
            InlineKeyboardButton setNameButton = new InlineKeyboardButton(localeMessageService.getMessage("reply.workout-day.keyboard.set-name-request", chatId));
            setNameButton.setCallbackData("/set-name-request-WorkoutDay:" + workoutDay.getId());
            lastRow.add(setNameButton);
        }

        //Create workout
        if (workoutDay.getIsWorkoutDay() == null || !workoutDay.getIsWorkoutDay()) {
            InlineKeyboardButton setNameButton = new InlineKeyboardButton(localeMessageService.getMessage("reply.workout-day.keyboard.create-workout-request", chatId));
            setNameButton.setCallbackData("/set-name-request-WorkoutDay:" + workoutDay.getId());
            lastRow.add(setNameButton);
        }

        //Set rest day
        if (workoutDay.getIsWorkoutDay() == null || workoutDay.getIsWorkoutDay()) {
            InlineKeyboardButton setRestDayButton = new InlineKeyboardButton(localeMessageService.getMessage("reply.workout-day.keyboard.set-as-rest-day", chatId));
            setRestDayButton.setCallbackData("/set-rest-WorkoutDay:" + workoutDay.getId());
            lastRow.add(setRestDayButton);
        }

        keyboard.add(lastRow);

        return new InlineKeyboardMarkup(keyboard);
    }
}
