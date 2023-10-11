package com.example.body.botlogic.incomingMessage;

import com.example.body.botlogic.resource.ArtifactRepository;
import com.example.body.botlogic.resource.DialogService;
import com.example.body.botlogic.resource.Emojis;

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
