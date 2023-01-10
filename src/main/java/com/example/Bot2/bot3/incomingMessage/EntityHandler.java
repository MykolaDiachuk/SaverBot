package com.example.Bot2.bot3.incomingMessage;

import com.example.Bot2.bot3.resource.ArtifactRepository;
import com.example.Bot2.bot3.resource.DialogService;
import com.example.Bot2.bot3.resource.Emojis;
import com.example.Bot2.bot3.resource.Keyboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class EntityHandler implements Handler {
    private final ArtifactRepository artifactRepository;
    private final DialogService dialogService;

    @Autowired
    public EntityHandler(ArtifactRepository artifactRepository, DialogService dialogService) {
        this.artifactRepository = artifactRepository;
        this.dialogService = dialogService;
    }

    private static final String HELLO = "Привіт" + Emojis.WAVE.get() + ", цей бот для зберігання інформації. Натисніть \"Зберегти повідомлення\", щоб створити папку, та надішліть " +
            "одне чи декілька повідомлень для збереження.";


    @Override
    public void handle(Update update) {

        if (update.getMessage().getText().equals("/start")) {
            dialogService.sendKeyboard(update.getMessage().getChatId(), HELLO,
                    Keyboard.getMainMenu());
        } else {
            if (artifactRepository.saveArtifact(update, update.getMessage().getFrom().getId()))
                dialogService.sendMessage(update.getMessage().getChatId(), "Збережено " + Emojis.SAVE.get());
            MessageHandler.isTextSave = false;
        }

    }
}



