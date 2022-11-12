package com.example.Bot2.bot3.incomingMessage;

import com.example.Bot2.bot3.resource.ArtifactRepository;
import com.example.Bot2.bot3.resource.DialogService;
import com.example.Bot2.bot3.resource.Keyboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class EntityHandler implements Handler {
    @Autowired
    DialogService dialogService;
    @Autowired
    ArtifactRepository artifactRepository;

    @Override
    public void handle(Update update) {

        if (update.getMessage().getText().equals("/start")) {
            dialogService.sendKeyboard(update.getMessage().getChatId(), "Привіт, це бот для зберігання інформації",
                    Keyboard.getMainMenu());
        } else artifactRepository.saveArtifact(update,update.getMessage().getFrom().getId());

    }
}



