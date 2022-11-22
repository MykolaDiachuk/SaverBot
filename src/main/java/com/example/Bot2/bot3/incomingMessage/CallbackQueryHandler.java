package com.example.Bot2.bot3.incomingMessage;


import com.example.Bot2.bot3.resource.ArtifactRepository;
import com.example.Bot2.bot3.resource.DialogService;
import com.example.Bot2.bot3.resource.Emojis;
import com.example.Bot2.bot3.resource.Keyboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class CallbackQueryHandler implements Handler {
    private final ArtifactRepository artifactRepository;
    private final DialogService dialogService;

    @Autowired
    public CallbackQueryHandler(ArtifactRepository artifactRepository, DialogService dialogService) {
        this.artifactRepository = artifactRepository;
        this.dialogService = dialogService;
    }


    @Override
    public void handle(Update update) {
        String text = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();

        if (Keyboard.isCalledRemoveThisArtifact) {
            artifactRepository.removeArtifact(userId, Integer.parseInt(text));
        } else if (!Keyboard.isCalledToAdd && !Keyboard.isCalledToRemoveFolder && !Keyboard.isCalledToRemoveArtifact) {
            artifactRepository.nameOfFolder.put(userId, text);
            dialogService.forwardArtifacts(userId, chatId);
        } else if (Keyboard.isCalledToAdd && !Keyboard.isCalledToRemoveFolder && !Keyboard.isCalledToRemoveArtifact) {
            artifactRepository.nameOfFolder.put(userId, text);
            dialogService.sendMessage(chatId, "Що зберегти (файл, ссилка, фото...)");
        } else if (!Keyboard.isCalledToAdd && Keyboard.isCalledToRemoveFolder && !Keyboard.isCalledToRemoveArtifact) {
            artifactRepository.removeFolder(chatId, text);
            dialogService.sendMessage(chatId, "Папку " + '"' + text + '"' + " видалено " + Emojis.DUMP.get());
        } else if (!Keyboard.isCalledToAdd && !Keyboard.isCalledToRemoveFolder) {
            artifactRepository.nameOfFolder.put(userId, text);
            dialogService.sendKeyboardArtifact(chatId, Emojis.UP.get(), userId);
            Keyboard.isCalledRemoveThisArtifact = true;
        }
    }
}



