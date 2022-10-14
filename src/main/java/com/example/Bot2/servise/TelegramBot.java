package com.example.Bot2.servise;


import com.example.Bot2.Config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;


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

    List<String> folder = new ArrayList<>();
    List<Message> result = new ArrayList<>();
    HashMap<String, List<Message>> library = new HashMap<>(); // головна мапа


    String nameOfFolder;
    Message someMessage;
    SendMessage message = new SendMessage();
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();

            String name = update.getMessage().getChat().getFirstName();
            Message updateMessage = update.getMessage();
            long chatId = update.getMessage().getChatId();
            String fileId = updateMessage.getDocument().getFileId();


            switch (updateMessage.getText()) {
                case "/start":
                    message.setReplyMarkup(getMainMenu());
                    startCommandReceived(chatId, name);

                    break;
                case "Зберегти до бібліотеки":
                    sendMsg(chatId, "Назва папки в яку зберегти:");

                    break;
                case "Що зберегти (файл, ссилка, фото...)":
                    break;
                case "Знайти":
                    break;
                default:
                    if (updateMessage.hasText()) {
                        if(!folder.contains(message.getText())){
                            preLibrarian(updateMessage);
                        }else nameOfFolder = updateMessage.getText();

                        sendMsg(chatId, "Що зберегти (файл, ссилка, фото...)");

                    } else if (updateMessage.hasDocument()) {
                        // sendDoc(chatId,;
                        if(library.containsKey(nameOfFolder)){
                            librarian2(updateMessage);
                        }else librarian(updateMessage);
                        // findObject(chatId,result);
                    }
            }
        }
    }

    private void startCommandReceived(Long chatId, String name) {

        String answer = "Hi, " + name + ", nice to meet you!";
        sendMsg(chatId, answer);
    }

    //Надсилання списку
    private void sendObject(Long chatId, List<String> messageToSand) {

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

    //головна клавіатура
    private ReplyKeyboardMarkup getMainMenu() {


        KeyboardRow row1 = new KeyboardRow();
        row1.add("Зберегти до бібліотеки");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Знайти");
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }


    //запис у перший лист. потрібно для того, щоб можна було знайти kеy для другої мапи.
    public void preLibrarian(Message message) {

        folder.add(message.getText());
        nameOfFolder = message.getText();
    }

    // запис у  головну мапу
    public void librarian(Message message) {
        result.add(message);
        library.put(nameOfFolder, result);
        someMessage = message;
    }

    public void librarian2(Message message) {
        List<Message> addNewObject = library.get(nameOfFolder);
        addNewObject.add(message);
        library.put(nameOfFolder,addNewObject);
        someMessage = message;
    }

    // пошук у першому лисі
    public void preFindObject(Long chatId) {
        sendObject(chatId, folder);

    }


    /* public void findObject(Long chatId) {
         List<Message> object = library.get(nameOfFolder);
         int a = object.size();
         for (int i = 0; i!=a; i++){
             message.setChatId(chatId);


             try {
                 execute(message);
             } catch (TelegramApiException e) {

             }
         }

     }*/
    public void sendDoc(Long chatId, File sendFile) {
        InputFile inputFile = new InputFile(String.valueOf(sendFile));
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(inputFile);
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    //userId -> massage
    // userid:metemateka -> List<messageId>


}






