package com.example.body.botlogic.incomingMessage;


import com.example.body.Config.Const;
import com.example.body.botlogic.resource.ArtifactRepository;
import com.example.body.botlogic.resource.DialogService;
import com.example.body.botlogic.resource.Emojis;
import com.example.body.botlogic.resource.Keyboard;
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
            dialogService.sendMessage(chatId, Const.TO_SEND);
        } else if (!Keyboard.isCalledToAdd && Keyboard.isCalledToRemoveFolder && !Keyboard.isCalledToRemoveArtifact) {
            artifactRepository.removeFolder(chatId, text);
            dialogService.sendMessage(chatId, "Папку " + '"' + text + '"' + " видалено " + Emojis.DUMP.get());
        } else {
            artifactRepository.nameOfFolder.put(userId, text);
            dialogService.sendKeyboardArtifact(chatId, Emojis.UP.get(), userId);
            Keyboard.isCalledRemoveThisArtifact = true;
        }
    }
}



