package com.mykhailotiutiun.repcounterbot.config;

import com.mykhailotiutiun.repcounterbot.botapi.RepCounterBot;
import com.mykhailotiutiun.repcounterbot.botapi.RepCounterBotFacade;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
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
    public RepCounterBot repCounterBot(RepCounterBotFacade repCounterBotFacade) {

        RepCounterBot repCounterBot = new RepCounterBot(repCounterBotFacade);
        repCounterBot.setBotUsername(telegramConfig.getBotName());
        repCounterBot.setBotToken(telegramConfig.getBotToken());
        repCounterBot.setBotPath(telegramConfig.getWebhookPath());

        return repCounterBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}