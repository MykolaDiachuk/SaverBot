package com.example.Bot2.servise;


import com.example.Bot2.Config.BotConfig;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
 private final Serialization serial = new Serialization();
    private final Keyboard keyboard = new Keyboard();
    private final Library library = new Library();


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
    long chatId=0;

    Message updateMsg = null;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        this.updateMsg=update.getMessage();

        if (update.hasMessage()) {
            String name = update.getMessage().getChat().getFirstName();
            Message updateMessage = update.getMessage();
            this.chatId = update.getMessage().getChatId();

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
                    break;
                default:
                    if (updateMessage.hasText()) {// якщо повідомлення - текст
                        if(updateMessage.hasEntities()) {
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setType("text_link");
                            messageEntity.setUrl(updateMessage.getText());
                            messageEntity.setOffset(0);
                            messageEntity.setLength(4);
                            //serial.serialization(messageEntity);
                            //java.io.File file = ResourceUtils.getFile("C:\\Games\\Bot2\\src\\main\\resources\\new.txt");
                            saveObject(messageEntity);

                        }else if (!library.folder.contains(updateMessage.getText())) { // якщо у листі немає такого обєкту
                            library.preLibrarian(updateMessage);
                            sendMsg(chatId, "Що зберегти (файл, ссилка, фото...)");
                        } else { library.nameOfFolder = updateMessage.getText();
                            sendMsg(chatId, "Що зберегти (файл, ссилка, фото...)");
                        }
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
                       // serial.serialization(file);
                        //java.io.File file1 = ResourceUtils.getFile("C:\\Games\\Bot2\\src\\main\\resources\\new.txt");
                         saveObject(file);


                    } else if (updateMessage.hasPhoto()){
                        sendMsg(chatId,"photo");
                    }
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            library.nameOfFolder = data;
            //findObject(update.getCallbackQuery().getMessage().getChatId(), data, update );
            List object = library.libraryOfMessage.get(library.nameOfFolder);
            int a = object.size();
            for (int i = 0; i != a; i++) {
                send(chatId,object.get(i));

            }
            sendMsg(chatId,"d");
        }
    }

    private void startCommandReceived(Long chatId, String name) {

        String answer = "Hi, " + name + ", nice to meet you!";
        sendMsg(chatId, answer);
    }
public void saveObject(Object file){
    if (library.libraryOfMessage.containsKey(library.nameOfFolder)) { // якщо такий ключ вже є
        library.librarian2(file);
    } else library.librarian(file);
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
     private void send(long chatId, List<MessageEntity> entities ){

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setEntities(entities);
        message.setText(entities.toString());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
    public void send(Long chatId, Object file) {

        SendDocument sendDocument = new SendDocument();
        sendDocument.setCaption(((java.io.File)file).getName());
        sendDocument.setChatId(chatId.toString());
        sendDocument.setDocument(new InputFile((java.io.File)file));
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
   /* public void findObject(Long chatId, String fileName, Update update) throws IOException, ClassNotFoundException {
        List object = library.libraryOfMessage.get(library.nameOfFolder);
        int a = object.size();
        for (int i = 0; i != a; i++) {
            if(update.getMessage().hasDocument()){
                send(chatId, (java.io.File)object.get(i), fileName);
            } else if (update.getMessage().hasEntities()){
                List<MessageEntity>entities=new ArrayList<>();
                entities.add((MessageEntity) object.get(i));
                send(chatId, entities);
            }

        }

    }*/




}






