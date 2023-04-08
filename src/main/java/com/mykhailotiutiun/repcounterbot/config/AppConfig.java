package com.mykhailotiutiun.repcounterbot.config;

import com.mykhailotiutiun.repcounterbot.botapi.RepCounterBot;
import com.mykhailotiutiun.repcounterbot.botapi.RepCounterBotFacade;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AppConfig {

    private final TelegramConfig telegramConfig;

    public AppConfig(TelegramConfig telegramConfig) {
        this.telegramConfig = telegramConfig;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public RepCounterBot RZDTelegramBot(RepCounterBotFacade repCounterBotFacade) {

        RepCounterBot rzdTelegramBot = new RepCounterBot(repCounterBotFacade);
        rzdTelegramBot.setBotUsername(telegramConfig.getBotName());
        rzdTelegramBot.setBotToken(telegramConfig.getBotToken());
        rzdTelegramBot.setBotPath(telegramConfig.getWebhookPath());

        return rzdTelegramBot;
    }
}