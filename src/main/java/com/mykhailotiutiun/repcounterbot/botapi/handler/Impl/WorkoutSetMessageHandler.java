package com.mykhailotiutiun.repcounterbot.botapi.handler.Impl;

import com.mykhailotiutiun.repcounterbot.botapi.handler.MessageHandler;
import com.mykhailotiutiun.repcounterbot.cache.CurrentBotStateCache;
import com.mykhailotiutiun.repcounterbot.cache.SelectedWorkoutExerciseCache;
import com.mykhailotiutiun.repcounterbot.constants.ChatState;
import com.mykhailotiutiun.repcounterbot.constants.MessageHandlerType;
import com.mykhailotiutiun.repcounterbot.message.WorkoutExerciseMessageGenerator;
import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import com.mykhailotiutiun.repcounterbot.service.WorkoutExerciseService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkoutSetMessageHandler implements MessageHandler {

    private final SelectedWorkoutExerciseCache selectedWorkoutExerciseCache;
    private final WorkoutExerciseService workoutExerciseService;
    private final CurrentBotStateCache currentBotStateCache;
    private final WorkoutExerciseMessageGenerator workoutExerciseMessageGenerator;

    public WorkoutSetMessageHandler(SelectedWorkoutExerciseCache selectedWorkoutExerciseCache, WorkoutExerciseService workoutExerciseService, CurrentBotStateCache currentBotStateCache, WorkoutExerciseMessageGenerator workoutExerciseMessageGenerator) {
        this.selectedWorkoutExerciseCache = selectedWorkoutExerciseCache;
        this.workoutExerciseService = workoutExerciseService;
        this.currentBotStateCache = currentBotStateCache;
        this.workoutExerciseMessageGenerator = workoutExerciseMessageGenerator;
    }

    @Override
    public BotApiMethod<?> handleMessage(Message message) {
        currentBotStateCache.setChatDataCurrentBotState(message.getChatId().toString(), ChatState.MAIN_MENU);

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
            workoutSets.add(WorkoutSet.builder()
                    .number(i + 1)
                    .weight(Integer.valueOf(s.split(":")[0]))
                    .reps(Integer.valueOf(s.split(":")[1]))
                    .build());
        }

        String chatId = message.getChatId().toString();
        workoutExerciseService.addSets(Long.valueOf(selectedWorkoutExerciseCache.getSelectedWorkoutExercise(chatId)), workoutSets);

        return workoutExerciseMessageGenerator.getWorkoutExerciseSendMessage(chatId, Long.valueOf(selectedWorkoutExerciseCache.getSelectedWorkoutExercise(chatId)));
    }
}
