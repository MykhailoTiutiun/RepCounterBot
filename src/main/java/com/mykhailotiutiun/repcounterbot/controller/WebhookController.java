package com.mykhailotiutiun.repcounterbot.controller;

import com.mykhailotiutiun.repcounterbot.botapi.RepCounterBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RestController
public class WebhookController {

    private final RepCounterBot repCounterBot;

    public WebhookController(RepCounterBot repCounterBot) {
        this.repCounterBot = repCounterBot;
    }

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        return repCounterBot.onWebhookUpdateReceived(update);
    }

}
