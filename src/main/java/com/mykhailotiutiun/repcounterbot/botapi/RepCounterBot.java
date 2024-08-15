package com.mykhailotiutiun.repcounterbot.botapi;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RepCounterBot extends TelegramWebhookBot {

    String botPath;
    String botUsername;
    String botToken;

    private final RepCounterBotFacade repCounterBotFacade;

    public RepCounterBot(RepCounterBotFacade repCounterBotFacade) {
        this.repCounterBotFacade = repCounterBotFacade;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return repCounterBotFacade.handleUpdate(update);
    }
}
