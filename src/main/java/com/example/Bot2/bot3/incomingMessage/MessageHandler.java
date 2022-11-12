package com.example.Bot2.bot3.incomingMessage;

import com.example.Bot2.bot3.models.Folder;
import com.example.Bot2.bot3.resource.ArtifactRepository;
import com.example.Bot2.bot3.resource.DialogService;
import com.example.Bot2.servise.Keyboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class MessageHandler implements Handler {

    private final DialogService dialogService;

    private final ArtifactRepository artifactRepository;

    @Autowired
    public MessageHandler(DialogService dialogService, ArtifactRepository artifactRepository) {
        this.dialogService = dialogService;
        this.artifactRepository = artifactRepository;
    }


    @Override
    public void handle(Update update) {

        String text = update.getMessage().getText();
        switch (text) {
            case "Зберегти до бібліотеки":
                dialogService.sendMessage(update.getMessage().getChatId(), "Назва папки в яку зберегти:");
                break;
            case "Що зберегти (файл, ссилка, фото...)":
                break;
            case "Знайти":
                dialogService.sendKeyboard(update.getMessage().getChatId(), "Ваші папки",
                        Keyboard.getFolderMenu(artifactRepository.folderNames));
                break;
            default:
                Folder folder = new Folder();
                folder.setName(update.getMessage().getText());
                folder.setUserId(update.getMessage().getFrom().getId());
                artifactRepository.createFolder(update.getMessage().getFrom().getId(), folder);
                dialogService.sendMessage(update.getMessage().getChatId(), "Що зберегти (файл, ссилка, фото...)");
                    /*for (String folderName : artifactRepository.folderNames) {
                        dialogService.sendMessage(update.getMessage().getChatId(),folderName);
                    }*/
        }
    }
}

