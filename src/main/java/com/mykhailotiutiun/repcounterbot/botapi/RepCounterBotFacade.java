package com.mykhailotiutiun.repcounterbot.botapi;

import com.mykhailotiutiun.repcounterbot.service.MainMenuService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RepCounterBotFacade {

    final MainMenuService mainMenuService;

    public RepCounterBotFacade(MainMenuService mainMenuService) {
        this.mainMenuService = mainMenuService;
    }

    public SendMessage handleUpdate(Update update){
        return mainMenuService.getMainMenuMessage(update.getMessage().getChatId().toString(), "Ok");
    }
}
