package com.example.Bot2.bot3.incomingMessage;

import com.example.Bot2.bot3.models.Folder;
import com.example.Bot2.bot3.resource.ArtifactRepository;
import com.example.Bot2.bot3.resource.DialogService;
import com.example.Bot2.bot3.resource.Emojis;
import com.example.Bot2.bot3.resource.Keyboard;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageHandler implements Handler {

    private final DialogService dialogService;

    private final ArtifactRepository artifactRepository;

    public static boolean isTextSave = false;

    @Autowired
    public MessageHandler(DialogService dialogService, ArtifactRepository artifactRepository) {
        this.dialogService = dialogService;
        this.artifactRepository = artifactRepository;
    }


    @Override
    public void handle(Update update) {
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();


        switch (text) {
            case "Зберегти повідомлення":
                dialogService.sendMessage(chatId, "Назва папки в яку зберегти:");
                break;
            case "Що зберегти (файл, ссилка, фото...)":
                break;
            case "Зберегти до папки":
                if (!artifactRepository.folderNames.containsKey(userId)
                        || artifactRepository.folderNames.get(userId).isEmpty()) {
                    dialogService.sendMessage(chatId, "У вас немає папок");
                } else {
                    dialogService.sendKeyboard(chatId, "Оберіть папку",
                            Keyboard.getMenuToAdd(artifactRepository.folderNames.get(userId)));
                }
                break;
            case "Видалити папку":
                if (!artifactRepository.folderNames.containsKey(userId)
                        || artifactRepository.folderNames.get(userId).isEmpty()) {
                    dialogService.sendMessage(chatId, "У вас немає папок");
                } else {
                    dialogService.sendKeyboard(chatId, "Оберіть папку для видалення",
                            Keyboard.getMenuToRemoveFolder(artifactRepository.folderNames.get(userId)));
                }
                break;
            case "Видалити повідомлення":
                if (!artifactRepository.folderNames.containsKey(userId)
                        || artifactRepository.folderNames.get(userId).isEmpty()) {
                    dialogService.sendMessage(chatId, "У вас немає папок");
                } else {
                    dialogService.sendKeyboard(chatId, "Оберіть папку у для видалення повідомлення",
                            Keyboard.getMenuToRemoveArtifact(artifactRepository.folderNames.get(userId)));
                }
                break;
            case "Знайти":
                if (!artifactRepository.folderNames.containsKey(userId)
                        || artifactRepository.folderNames.get(userId).isEmpty()) {
                    dialogService.sendMessage(chatId, "У вас немає папок");
                } else dialogService.sendKeyboard(chatId, "Ваші папки",
                        Keyboard.getFolderMenu(artifactRepository.folderNames.get(userId)));
                break;
            default:
                if (!isTextSave){
                    Folder folder;
                    if (artifactRepository.isExist(text, userId)) {
                        folder = artifactRepository.getFolder(userId, text);
                    } else {
                        folder = new Folder();
                        folder.setName(text);
                        folder.setUserId(userId);
                    }
                    artifactRepository.nameOfFolder.put(userId,text);
                    dialogService.sendMessage(chatId, "Що зберегти (файл, ссилка, фото...)");
                    artifactRepository.createFolder(userId, folder);
                    isTextSave = true;
                } else {
                    if (artifactRepository.saveArtifact(update, update.getMessage().getFrom().getId()))
                        dialogService.sendMessage(update.getMessage().getChatId(), "Збережено " + Emojis.SAVE.get() );
                    isTextSave =false;
                }


        }
    }
}

