package com.example.Bot2.bot3.incomingMessage;

import com.example.Bot2.bot3.resource.ArtifactRepository;
import com.example.Bot2.bot3.resource.DialogService;
import com.example.Bot2.bot3.resource.Emojis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ManyOtherHandlers implements Handler {

    private final ArtifactRepository artifactRepository;
    private final DialogService dialogService;

    @Autowired
    public ManyOtherHandlers(ArtifactRepository artifactRepository, DialogService dialogService) {
        this.artifactRepository = artifactRepository;
        this.dialogService = dialogService;
    }

    @Override
    public void handle(Update update) {
        if (artifactRepository.saveArtifact(update, update.getMessage().getFrom().getId()))
            dialogService.sendMessage(update.getMessage().getChatId(), "Збережено " + Emojis.SAVE.get());
        MessageHandler.isTextSave = false;
    }
}
