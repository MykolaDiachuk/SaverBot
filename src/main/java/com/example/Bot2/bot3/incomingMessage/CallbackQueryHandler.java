package com.example.Bot2.bot3.incomingMessage;

import com.example.Bot2.bot3.models.Artifact;
import com.example.Bot2.bot3.models.Folder;
import com.example.Bot2.bot3.resource.ArtifactRepository;
import com.example.Bot2.bot3.resource.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class CallbackQueryHandler implements Handler {
    @Autowired
    ArtifactRepository artifactRepository;

    @Autowired
    DialogService dialogService;

    @Override
    public void handle(Update update) {

        artifactRepository.nameOfFolder = update.getCallbackQuery().getData();
        dialogService.sendMessage(update.getCallbackQuery().getMessage().getChatId(),
                "Вміст папки " + '"' + artifactRepository.nameOfFolder + '"');
        Folder folder = artifactRepository.getFolder(update.getCallbackQuery().getFrom().getId(), artifactRepository.nameOfFolder);
        List<Artifact> artifacts = folder.getArtifacts();
        for (Artifact artifact : artifacts) {
            dialogService.forwardMessage(artifact.getChatId(), artifact.getChatId(), artifact.getMessageId());
        }
    }
}
