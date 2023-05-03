package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.cache.ChatDataCache;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkoutSetMessageHandler implements MessageHandler {

    private final ChatDataCache chatDataCache;
    private final WorkoutExerciseService workoutExerciseService;

    public WorkoutSetMessageHandler(ChatDataCache chatDataCache, WorkoutExerciseService workoutExerciseService) {
        this.chatDataCache = chatDataCache;
        this.workoutExerciseService = workoutExerciseService;
    }

    @Override
    public BotApiMethod<?> handleMessage(Message message) {
        chatDataCache.setChatDataCurrentBotState(message.getChatId().toString(), ChatState.MAIN_MENU);

        return handleFastSetsSetRequest(message);
    }

    @Override
    public MessageHandlerType getHandlerType() {
        return MessageHandlerType.WORKOUT_SET_HANDLER;
    }

    private SendMessage handleFastSetsSetRequest(Message message) {
        List<String> setsString = List.of(message.getText().replaceAll("\\s", "").split(","));
        List<WorkoutSet> workoutSets = new ArrayList<>();

        for (int i = 0; i < setsString.size(); i++) {
            String s = setsString.get(i);
            workoutSets.add(new WorkoutSet(i + 1, Integer.valueOf(s.split(":")[1]), Integer.valueOf(s.split(":")[0])));
        }

        String chatId = message.getChatId().toString();
        workoutExerciseService.addSetsToWorkoutExercise(chatDataCache.getSelectedWorkoutExercise(chatId), workoutSets);

        return workoutExerciseService.getWorkoutExerciseSendMessage(chatId, chatDataCache.getSelectedWorkoutExercise(chatId));
    }
}
