package com.example.Bot2.servise;


import com.example.Bot2.Config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
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


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();

            String name = update.getMessage().getChat().getFirstName();
            Message updateMessage = update.getMessage();
            long chatId = update.getMessage().getChatId();



            switch (updateMessage.getText()) {
                case "/start":
                    message.setReplyMarkup(keyboard.getMainMenu());
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
                        if(!library.folder.contains(message.getText())){
                           library.preLibrarian(updateMessage);
                        }else library.nameOfFolder = updateMessage.getText();


                        sendMsg(chatId, "Що зберегти (файл, ссилка, фото...)");

                    } else if (updateMessage.hasDocument()) {
                        // sendDoc(chatId,;
                        if(library.libraryOfMessage.containsKey(library.nameOfFolder)){
                            library.librarian2(updateMessage);
                        }else library.librarian(updateMessage);
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


    // пошук у першому лисі
    public void preFindObject(Long chatId) {
        sendObject(chatId, library.folder);

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

    //userId -> massage
    // userid:metemateka -> List<messageId>


}






