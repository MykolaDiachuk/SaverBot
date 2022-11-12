package com.example.Bot2.bot3.incomingMessage;

import com.example.Bot2.bot3.models.Folder;
import com.example.Bot2.bot3.resource.ArtifactRepository;
import com.example.Bot2.bot3.resource.DialogService;
import com.example.Bot2.bot3.resource.Keyboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

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
            case "Зберегти до папки":
                dialogService.sendMessage(update.getMessage().getChatId(), "Назва папки в яку зберегти:");
                break;
            case "Що зберегти (файл, ссилка, фото...)":
                break;
            case "Додати до папки":
                dialogService.sendKeyboard(update.getMessage().getChatId(), "Оберіть папку",
                        Keyboard.getMenuToAdd(artifactRepository.folderNames.get(update.getMessage().getFrom().getId())));
                break;
            case "Знайти":
                if (!artifactRepository.folderNames.containsKey(update.getMessage().getFrom().getId())
                        || artifactRepository.folderNames.get(update.getMessage().getFrom().getId()).isEmpty()) {
                    dialogService.sendMessage(update.getMessage().getChatId(), "У вас немає папок");
                } else dialogService.sendKeyboard(update.getMessage().getChatId(), "Ваші папки",
                        Keyboard.getFolderMenu(artifactRepository.folderNames.get(update.getMessage().getFrom().getId())));
                break;
            default:
                Folder folder;
                if (artifactRepository.isExist(update.getMessage().getText(),update.getMessage().getFrom().getId())) {
                    folder = artifactRepository.getFolder(update.getMessage().getFrom().getId(), update.getMessage().getText());
                } else {
                    folder = new Folder();
                    folder.setName(update.getMessage().getText());
                    folder.setUserId(update.getMessage().getFrom().getId());
                }
                dialogService.sendMessage(update.getMessage().getChatId(), "Що зберегти (файл, ссилка, фото...)");
                artifactRepository.createFolder(update.getMessage().getFrom().getId(), folder);

        }
    }
}

