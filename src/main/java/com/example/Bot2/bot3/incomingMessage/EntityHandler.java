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
    private static final String hello = "Привіт, цей бот для зберігання інформації. Натисніть \"Зберегти до папки\", щоб створити папку та надішліть" +
            "одне чи декілька повідомлень для збереження. ВАЖЛИВО, повідомлення, які зберігаються до папки, не мають бути текстом. " +
            "Щоб знайти потрібну вам папку та  її вміст натисніть \"Знайти\" і виберіть папку.";

    @Override
    public void handle(Update update) {

        if (update.getMessage().getText().equals("/start")) {
            dialogService.sendKeyboard(update.getMessage().getChatId(), hello,
                    Keyboard.getMainMenu());
        } else artifactRepository.saveArtifact(update, update.getMessage().getFrom().getId());

    }
}



