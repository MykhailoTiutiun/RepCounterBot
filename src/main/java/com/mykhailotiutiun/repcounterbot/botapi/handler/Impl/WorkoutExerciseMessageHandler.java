package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.cache.CurrentBotStateCache;
import com.mykhailotiutiun.repcounterbot.cache.SelectedWorkoutDayCache;
import com.mykhailotiutiun.repcounterbot.cache.SelectedWorkoutExerciseCache;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.message.WorkoutDayMessageGenerator;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class WorkoutExerciseMessageHandler implements MessageHandler {

    private final CurrentBotStateCache currentBotStateCache;
    private final WorkoutDayService workoutDayService;
    private final WorkoutExerciseService workoutExerciseService;
    private final SelectedWorkoutExerciseCache selectedWorkoutExerciseCache;
    private final SelectedWorkoutDayCache selectedWorkoutDayCache;
    private final WorkoutDayMessageGenerator workoutDayMessageGenerator;

    public WorkoutExerciseMessageHandler(CurrentBotStateCache currentBotStateCache, WorkoutDayService workoutDayService, WorkoutExerciseService workoutExerciseService, SelectedWorkoutExerciseCache selectedWorkoutExerciseCache, SelectedWorkoutDayCache selectedWorkoutDayCache, WorkoutDayMessageGenerator workoutDayMessageGenerator) {
        this.currentBotStateCache = currentBotStateCache;
        this.workoutDayService = workoutDayService;
        this.workoutExerciseService = workoutExerciseService;
        this.selectedWorkoutExerciseCache = selectedWorkoutExerciseCache;
        this.selectedWorkoutDayCache = selectedWorkoutDayCache;
        this.workoutDayMessageGenerator = workoutDayMessageGenerator;
    }

    @Override
    public BotApiMethod<?> handleMessage(Message message) {
        switch (currentBotStateCache.getChatDataCurrentBotState(message.getChatId().toString())){
            case CREATE_WORKOUT_EXERCISE: return handleCreateWorkoutExercise(message);
            case CHANGE_WORKOUT_EXERCISE_NAME: return handleChangeName(message);
        }

        return null;
    }

    @Override
    public MessageHandlerType getHandlerType() {
        return MessageHandlerType.WORKOUT_EXERCISE_HANDLER;
    }

    private SendMessage handleCreateWorkoutExercise(Message message) {
        currentBotStateCache.setChatDataCurrentBotState(message.getChatId().toString(), ChatState.MAIN_MENU);

        workoutExerciseService.create(WorkoutExercise.builder()
                .name(message.getText())
                .workoutDay(workoutDayService.getById(Long.valueOf(selectedWorkoutDayCache.getSelectedWorkoutDay(message.getChatId().toString()))))
                .build());

        return workoutDayMessageGenerator.getSelectWorkoutDaySendMessage(message.getChatId().toString(), Long.valueOf(selectedWorkoutDayCache.getSelectedWorkoutDay(message.getChatId().toString())));
    }

    private SendMessage handleChangeName(Message message){
        currentBotStateCache.setChatDataCurrentBotState(message.getChatId().toString(), ChatState.MAIN_MENU);

        workoutExerciseService.setName(Long.valueOf(selectedWorkoutExerciseCache.getSelectedWorkoutExercise(message.getChatId().toString())), message.getText());

        return workoutDayMessageGenerator.getSelectWorkoutDaySendMessage(message.getChatId().toString(), Long.valueOf(selectedWorkoutDayCache.getSelectedWorkoutDay(message.getChatId().toString())));
    }
}
