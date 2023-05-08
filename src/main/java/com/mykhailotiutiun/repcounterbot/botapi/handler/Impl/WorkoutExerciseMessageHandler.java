package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.service.WorkoutDayService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class WorkoutExerciseMessageHandler implements MessageHandler {

    private final ChatDataCache chatDataCache;
    private final WorkoutDayService workoutDayService;
    private final WorkoutExerciseService workoutExerciseService;

    public WorkoutExerciseMessageHandler(ChatDataCache chatDataCache, WorkoutDayService workoutDayService, WorkoutExerciseService workoutExerciseService) {
        this.chatDataCache = chatDataCache;
        this.workoutDayService = workoutDayService;
        this.workoutExerciseService = workoutExerciseService;
    }

    @Override
    public BotApiMethod<?> handleMessage(Message message) {
        switch (chatDataCache.getChatDataCurrentBotState(message.getChatId().toString())){
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
        chatDataCache.setChatDataCurrentBotState(message.getChatId().toString(), ChatState.MAIN_MENU);

        workoutExerciseService.create(new WorkoutExercise(message.getText(), workoutDayService.getWorkoutDayById(chatDataCache.getSelectedWorkoutDay(message.getChatId().toString()))));

        return workoutDayService.getSelectWorkoutDaySendMessage(message.getChatId().toString(), chatDataCache.getSelectedWorkoutDay(message.getChatId().toString()));
    }

    private SendMessage handleChangeName(Message message){
        chatDataCache.setChatDataCurrentBotState(message.getChatId().toString(), ChatState.MAIN_MENU);

        workoutExerciseService.setNameToWorkoutExercise(chatDataCache.getSelectedWorkoutExercise(message.getChatId().toString()), message.getText());

        return workoutDayService.getSelectWorkoutDaySendMessage(message.getChatId().toString(), chatDataCache.getSelectedWorkoutDay(message.getChatId().toString()));
    }
}
