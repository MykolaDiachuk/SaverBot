package com.example.Bot2.servise;


import com.example.Bot2.Config.BotConfig;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    private Keyboard keyboard = new Keyboard();
    private Library library = new Library();


    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }


    SendMessage message = new SendMessage();


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            String name = update.getMessage().getChat().getFirstName();
            Message updateMessage = update.getMessage();
            long chatId = update.getMessage().getChatId();
            String text = updateMessage.getText() == null ? "" : updateMessage.getText();
            switch (text) {
                case "/start":
                    message.setReplyMarkup(keyboard.getMainMenu());
                    startCommandReceived(chatId, name);

                    break;
                case "Зберегти до бібліотеки":
                    sendMsg(chatId, "Назва папки в яку зберегти:");
                    // повідомлення надіслане після цього піде у default

                    break;
                case "Що зберегти (файл, ссилка, фото...)":
                    break;
                case "Знайти":
                    sendKeyboard(chatId, "Ваші папки", keyboard.getFolderMenu(library.folder));
                    if(update.hasCallbackQuery()){

                    }
                    break;
                default:
                    if (updateMessage.hasText()) { // якщо повідомлення - текст
                        if (!library.folder.contains(message.getText())) { // якщо у листі немає такого обєкту
                            library.preLibrarian(updateMessage);
                        } else library.nameOfFolder = updateMessage.getText();
                        sendMsg(chatId, "Що зберегти (файл, ссилка, фото...)");
                        // preFindObject(chatId); //показує обєкти List<String> folder
                    } else if (updateMessage.hasDocument()) { // якщощо повідомлення документ.
                        GetFile getFile = new GetFile();
                        getFile.setFileId(updateMessage.getDocument().getFileId());
                        String fileName = updateMessage.getDocument().getFileName();
                        String filePath = null;
                        try {
                            File execute = execute(getFile);
                            filePath = execute.getFilePath();

                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        java.io.File file = new java.io.File(fileName);
                        try {
                            file = downloadFile(filePath, file);


                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        if (library.libraryOfMessage.containsKey(library.nameOfFolder)) { // якщо такий ключ вже є
                            library.librarian2(file);
                        } else library.librarian(file);
                        if(update.hasCallbackQuery()){
                            library.nameOfFolder = update.getCallbackQuery().getData();
                            sendMsg(chatId,library.nameOfFolder);//перевірка
                            findObject(chatId, fileName);
                        }



                    }
            }
        }
    }

    private void startCommandReceived(Long chatId, String name) {

        String answer = "Hi, " + name + ", nice to meet you!";
        sendMsg(chatId, answer);
    }

    //Надсилання списку
    private void sendList(Long chatId, List<String> messageToSand) {

        int a = messageToSand.size();
        for (int i = 0; i != a; i++) {
            message.setChatId(chatId);
            message.setText(messageToSand.get(i));
            try {
                execute(message);
            } catch (TelegramApiException e) {

            }
        }


    }

    //надсилання текстового повідомлення
    private void sendMsg(Long chatId, String textToSend) {

        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }

    // не звертай увагу, ще не дописано
    public void sendKeyboard(Long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }

    }

    // мало б надсилати документи, але setDocument() у якості аргументу хоче InputFile, який я не моду отримати просто так
    public void sendDoc(Long chatId, java.io.File file, String fileName) {

        SendDocument sendDocument = new SendDocument();
        sendDocument.setCaption(fileName);
        sendDocument.setChatId(chatId.toString());
        sendDocument.setDocument(new InputFile(file));
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    // пошук у першому лисі
    public void preFindObject(Long chatId) {
        sendList(chatId, library.folder);

    }

    //пробував виводити обєкти листа з документами, але для цього потрібно їх надсилати користувачу.
    public void findObject(Long chatId, String fileName) {
        List<java.io.File> object = library.libraryOfMessage.get(library.nameOfFolder);
        int a = object.size();
        for (int i = 0; i != a; i++) {
            sendDoc(chatId, object.get(i), fileName);
        }

    }

    //userId -> massage
    // userid:metemateka -> List<messageId>


}






