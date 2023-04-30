package com.mykhailotiutiun.repcounterbot.service.Impl;


import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutWeekRepository;
import com.mykhailotiutiun.repcounterbot.service.LocalDateWeekService;
import com.mykhailotiutiun.repcounterbot.service.LocaleMessageService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WorkoutWeekServiceImpl implements WorkoutWeekService {

    private final WorkoutWeekRepository workoutWeekRepository;
    private final WorkoutDayService workoutDayService;
    private final LocalDateWeekService localDateWeekService;
    private final LocaleMessageService localeMessageService;


    public WorkoutWeekServiceImpl(WorkoutWeekRepository workoutWeekRepository, WorkoutDayService workoutDayService, LocalDateWeekService localDateWeekService, LocaleMessageService localeMessageService) {
        this.workoutWeekRepository = workoutWeekRepository;
        this.workoutDayService = workoutDayService;
        this.localDateWeekService = localDateWeekService;
        this.localeMessageService = localeMessageService;
    }

    @Override
    public WorkoutWeek getWorkoutWeekById(String id) {
        log.trace("Get WorkoutWeek with id: {}", id);
        return workoutWeekRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public WorkoutWeek getCurrentWorkoutWeekByUserId(Long userId) {
        log.trace("Get current WorkoutWeek with user-id {}", userId);

        WorkoutWeek workoutWeek = workoutWeekRepository.findByUserIdAndCurrent(userId, true).orElseThrow(EntityNotFoundException::new);
        if (!localDateWeekService.isCurrentWeek(workoutWeek.getWeekStartDate(), workoutWeek.getWeekEndDate())) {
            createFromOldWorkoutWeek(workoutWeek);
            workoutWeek = workoutWeekRepository.findByUserIdAndCurrent(userId, true).orElseThrow(EntityNotFoundException::new);
        }
        return workoutWeek;
    }

    @Override
    public List<WorkoutWeek> getAllWorkoutWeeks() {
        log.trace("Get all WorkoutWeeks");
        return workoutWeekRepository.findAll();
    }

    @Override
    public void create(WorkoutWeek workoutWeek) throws EntityAlreadyExistsException {
        log.trace("Create WorkoutWeek: {}", workoutWeek);
        save(workoutWeek);

        for (int i = 0; i < 7; i++) {
            workoutDayService.save(new WorkoutDay(workoutWeek, null, workoutWeek.getWeekStartDate().plusDays(i), null));
        }
    }

    @Override
    public void createFromOldWorkoutWeek(WorkoutWeek oldWorkoutWeek) {
        WorkoutWeek workoutWeek = new WorkoutWeek(oldWorkoutWeek.getUser(), localDateWeekService.getFirstDateOfWeekFromDate(LocalDate.now()), localDateWeekService.getLastDateOfWeekFromDate(LocalDate.now()));
        save(workoutWeek);

        oldWorkoutWeek.setCurrent(false);
        save(oldWorkoutWeek);

        workoutDayService.createAllFromOldWorkoutWeek(oldWorkoutWeek, workoutWeek);
    }

    @Override
    public void save(WorkoutWeek workoutWeek) {
        log.trace("Save WorkoutWeek: {}", workoutWeek);
        workoutWeekRepository.save(workoutWeek);
    }

    @Override
    public void deleteById(String id) {
        log.trace("Delete WorkoutWeek with id: {}", id);
        workoutWeekRepository.deleteById(id);
    }

    @Override
    public SendMessage getCurrentWorkoutWeekSendMessage(String chatId) {
        WorkoutWeek currentWorkoutWeek;
        try {
            currentWorkoutWeek = getCurrentWorkoutWeekByUserId(Long.valueOf(chatId));
        } catch (EntityNotFoundException e) {
            return new SendMessage(chatId, localeMessageService.getMessage("reply.error", chatId));
        }

        SendMessage sendMessage = new SendMessage(chatId, currentWorkoutWeek.print(localeMessageService.getMessage("print.workout-week", chatId)));

        sendMessage.setReplyMarkup(getInlineKeyboardForWeek(workoutDayService.getAllWorkoutDaysByWorkoutWeek(currentWorkoutWeek), chatId));

        return sendMessage;
    }

    @Override
    public EditMessageText getCurrentWorkoutWeekEditMessage(String chatId, Integer messageId){
        WorkoutWeek currentWorkoutWeek = getCurrentWorkoutWeekByUserId(Long.valueOf(chatId));

        EditMessageText editMessageText =new EditMessageText(currentWorkoutWeek.print(localeMessageService.getMessage("print.workout-week", chatId)));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setReplyMarkup(getInlineKeyboardForWeek(workoutDayService.getAllWorkoutDaysByWorkoutWeek(currentWorkoutWeek), chatId));
        return editMessageText;
    }

    private InlineKeyboardMarkup getInlineKeyboardForWeek(List<WorkoutDay> workoutDays, String chatId) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        workoutDays.forEach(workoutDay -> {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton(workoutDay.print(localeMessageService.getMessage("print.workout-day.is-rest-day", chatId), localeMessageService.getMessage("print.workout-day.type-not-set", chatId), localeMessageService.getLocalTag(chatId)));
            keyboardButton.setCallbackData("/select-WorkoutDay:" + workoutDay.getId());

            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
            inlineKeyboardButtons.add(keyboardButton);

            keyboard.add(inlineKeyboardButtons);
        });

        return new InlineKeyboardMarkup(keyboard);
    }
}
