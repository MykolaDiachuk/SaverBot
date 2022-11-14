package com.example.Bot2.bot3.incomingMessage;

import com.example.Bot2.bot3.resource.ArtifactRepository;
import com.example.Bot2.bot3.resource.DialogService;
import com.example.Bot2.bot3.resource.Keyboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class ManyOtherHandlers implements Handler{
    @Autowired
    ArtifactRepository artifactRepository;
    @Autowired
    DialogService dialogService;

    @Override
    public void handle(Update update) {
        if (Keyboard.isCalledToRemoveArtifact){
           /* artifactRepository.removeArtifact(update,update.getMessage().getFrom().getId());
            dialogService.sendMessage(update.getMessage().getChatId(),"Видалено")*/;
        }else {
            artifactRepository.saveArtifact(update,update.getMessage().getFrom().getId());
            dialogService.sendMessage(update.getMessage().getChatId(),"Збережено");
        }


    }
}
