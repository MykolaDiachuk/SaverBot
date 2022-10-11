package com.example.Bot2.servise;


import com.example.Bot2.Config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
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

            Message updateMessage = update.getMessage();
            long chatId = update.getMessage().getChatId();
            String name = update.getMessage().getChat().getFirstName();

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
                        preLibrarian(updateMessage);
                        message.setReplyMarkup(getSaveMenu());

                    } else if (updateMessage.hasAnimation() || updateMessage.hasAudio() ||
                            updateMessage.hasContact() || updateMessage.hasDocument() || updateMessage.hasPhoto()
                            || updateMessage.hasVideo() || updateMessage.hasVoice()) {

                        librarian(updateMessage);
                        findObject(chatId,result);
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


    private ReplyKeyboardMarkup getSaveMenu() {

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Що зберегти (файл, ссилка, фото...)");
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(row1);
        replyKeyboardMarkup.setKeyboard(rows);

        return replyKeyboardMarkup;
    }

    //запис у першу мапу. потрібно для того, щоб по chatId отримати kay для другої мапи
    public void preLibrarian(Message message) {
        folder.add(message.getText());
        nameOfFolder = message.getText();
    }

    // запис у другу головну мапу
    public void librarian(Message message) {
        result.add(message);
        library.put(nameOfFolder, result);
    }

    // пошук об'єктів, повертає список обєктів
    public void preFindObject(Long chatId) {
        sendObject(chatId, folder);

    }

    public void findObject(Long chatId, List<Message> result) {
        int a = result.size();
        for (int i = 0; i!=a; i++){
            message.setChatId(chatId);
            message.setEntities((List<MessageEntity>) result.get(i));
            try {
                execute(message);
            } catch (TelegramApiException e) {

            }
        }

    }


    //userId -> massage
    // userid:metemateka -> List<messageId>
    //
}
