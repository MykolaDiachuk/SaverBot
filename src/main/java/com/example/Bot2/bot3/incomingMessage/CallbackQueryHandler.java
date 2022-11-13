package com.example.Bot2.bot3.incomingMessage;

import com.example.Bot2.bot3.models.Artifact;
import com.example.Bot2.bot3.models.Folder;
import com.example.Bot2.bot3.resource.ArtifactRepository;
import com.example.Bot2.bot3.resource.DialogService;
import com.example.Bot2.bot3.resource.Keyboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CallbackQueryHandler implements Handler {
    @Autowired
    ArtifactRepository artifactRepository;

    @Autowired
    DialogService dialogService;


    @Override
    public void handle(Update update) {
        if (!Keyboard.isCalledToAdd && !Keyboard.isCalledToRemoveFolder && !Keyboard.isCalledToRemoveArtifact) {
            artifactRepository.nameOfFolder.put(update.getCallbackQuery().getFrom().getId(), update.getCallbackQuery().getData());
            dialogService.forwardArtifacts(update);


        } else if (Keyboard.isCalledToAdd && !Keyboard.isCalledToRemoveFolder && !Keyboard.isCalledToRemoveArtifact) {
            artifactRepository.nameOfFolder.put(update.getCallbackQuery().getFrom().getId(), update.getCallbackQuery().getData());
            dialogService.sendMessage(update.getCallbackQuery().getMessage().getChatId(), "Що зберегти (файл, ссилка, фото...)");
        } else if (!Keyboard.isCalledToAdd && Keyboard.isCalledToRemoveFolder && !Keyboard.isCalledToRemoveArtifact) {
            artifactRepository.removeFolder(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getData());
            dialogService.sendMessage(update.getCallbackQuery().getMessage().getChatId(),
                    "Папку " + '"' + update.getCallbackQuery().getData() + '"' + " видалено");
        } else if (!Keyboard.isCalledToAdd && !Keyboard.isCalledToRemoveFolder && Keyboard.isCalledToRemoveArtifact){
           /* artifactRepository.removeArtifact(update,update.getCallbackQuery().getFrom().getId(),update.getCallbackQuery().getData());
            dialogService.sendMessage(update.getCallbackQuery().getMessage().getChatId(), "Повідомлення видалено");*/
            artifactRepository.nameOfFolder.put(update.getCallbackQuery().getFrom().getId(), update.getCallbackQuery().getData());
            dialogService.forwardArtifacts(update);
        }
    }


}
