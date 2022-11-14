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
        String text = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();

        if (!Keyboard.isCalledToAdd && !Keyboard.isCalledToRemoveFolder && !Keyboard.isCalledToRemoveArtifact) {
            artifactRepository.nameOfFolder.put(userId, text);
            dialogService.forwardArtifacts(userId, chatId);
        } else if (Keyboard.isCalledToAdd && !Keyboard.isCalledToRemoveFolder && !Keyboard.isCalledToRemoveArtifact) {
            artifactRepository.nameOfFolder.put(userId,text);
            dialogService.sendMessage(chatId, "Що зберегти (файл, ссилка, фото...)");
        } else if (!Keyboard.isCalledToAdd && Keyboard.isCalledToRemoveFolder && !Keyboard.isCalledToRemoveArtifact) {
            artifactRepository.removeFolder(chatId, text);
            dialogService.sendMessage(chatId, "Папку " + '"' + text + '"' + " видалено");
        } else if (!Keyboard.isCalledToAdd && !Keyboard.isCalledToRemoveFolder && Keyboard.isCalledToRemoveArtifact){
           /* artifactRepository.removeArtifact(update,update.getCallbackQuery().getFrom().getId(),update.getCallbackQuery().getData());
            dialogService.sendMessage(update.getCallbackQuery().getMessage().getChatId(), "Повідомлення видалено");*/
            artifactRepository.nameOfFolder.put(userId, text);
            dialogService.forwardArtifacts(userId, chatId);
        }
    }


}
