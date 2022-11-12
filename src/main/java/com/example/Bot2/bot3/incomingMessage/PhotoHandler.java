package com.example.Bot2.bot3.incomingMessage;

import com.example.Bot2.bot3.resource.ArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class PhotoHandler implements Handler{
    @Autowired
    ArtifactRepository artifactRepository;

    @Override
    public void handle(Update update) {
        artifactRepository.saveArtifact(update,update.getMessage().getFrom().getId());
    }
}
