package com.mykhailotiutiun.repcounterbot.service;


import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutWeekRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WorkoutWeekService {

    private final WorkoutWeekRepository workoutWeekRepository;
    private final WorkoutDayService workoutDayService;
    private final LocalDateWeekService localDateWeekService;


    public WorkoutWeekService(WorkoutWeekRepository workoutWeekRepository, WorkoutDayService workoutDayService, LocalDateWeekService localDateWeekService) {
        this.workoutWeekRepository = workoutWeekRepository;
        this.workoutDayService = workoutDayService;
        this.localDateWeekService = localDateWeekService;
    }

    public WorkoutWeek getWorkoutWeekById(String id){
        log.trace("Get WorkoutWeek with id: {}", id);
        return workoutWeekRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public WorkoutWeek getWorkoutWeekByUserIdAndLocalDate(Long userId, LocalDate localDate) {
        log.trace("Get WorkoutWeek with user-id {} and local-date {}", userId, localDate.toString());
        return workoutWeekRepository.findByUserIdAndWeekStartDate(userId, localDateWeekService.getFirstDateOfWeekFromDate(localDate)).orElseThrow(EntityNotFoundException::new);
    }

    public List<WorkoutWeek> getAllWorkoutWeeks(){
        log.trace("Get all WorkoutWeeks");
        return workoutWeekRepository.findAll();
    }

    public void create(WorkoutWeek workoutWeek) throws EntityAlreadyExistsException {
        log.trace("Create WorkoutWeek: {}", workoutWeek);
        save(workoutWeek);

        for(int i = 0; i < 7; i++){
            workoutDayService.save(new WorkoutDay(workoutWeek, null, workoutWeek.getWeekStartDate().plusDays(i), null));
        }
    }

    public void save(WorkoutWeek workoutWeek){
        log.trace("Save WorkoutWeek: {}", workoutWeek);
        workoutWeekRepository.save(workoutWeek);
    }

    public void deleteById(String id){
        log.trace("Delete WorkoutWeek with id: {}", id);
        workoutWeekRepository.deleteById(id);
    }




    public SendMessage getCurrentWorkoutWeekSendMessage(String chatId){
        WorkoutWeek currentWorkoutWeek;
        try {
            currentWorkoutWeek = getWorkoutWeekByUserIdAndLocalDate(Long.valueOf(chatId), LocalDate.now());
        } catch (EntityNotFoundException e){
            return new SendMessage(chatId, "Щось пішло не так");
        }

        SendMessage sendMessage = new SendMessage(chatId, String.format("Поточний тиждень тренувань з %s, по %s", currentWorkoutWeek.getWeekStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), currentWorkoutWeek.getWeekEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));

        sendMessage.setReplyMarkup(getInlineKeyboardForWeek(workoutDayService.getWorkoutDaysByWorkoutWeekId(currentWorkoutWeek)));

        return sendMessage;
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
