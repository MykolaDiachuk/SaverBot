package com.example.Bot2.servise;


import com.example.Bot2.Config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
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

    HashMap<Long, String> preLibrary = new HashMap<>(); // перша мапа
    HashMap<String, List<Message>> library = new HashMap<>(); // головна мапа
    SendMessage message = new SendMessage();
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();




    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message updateMessage = update.getMessage();
            long chatId = update.getMessage().getChatId();
            String name = update.getMessage().getChat().getFirstName();



            switch (updateMessage.getText()) {
                case "/start":
                    startCommandReceived(chatId, name);
                    break;
                case "Зберегти до бібліотеки":
                    sendMsg(chatId, "Назва папки в яку зберегти:");

                    break;
                case "Що зберегти (файл, ссилка, фото...)":
                    librarian(chatId, updateMessage);
                    sendObject(chatId, findObject(updateMessage.getText())); // перевірка
                    break;
                case "Що шукаєш?":
                    sendObject(chatId, findObject(updateMessage.getText()));
                    break;
                default:
                    preLibrarian(chatId, updateMessage);
                   sendMsg(chatId,preFindObject(chatId));
                    message.setReplyMarkup(getSaveMenu());


            }

        }


    }



    private void startCommandReceived(Long chatId, String name) {

        String answer = "Hi, " + name + ", nice to meet you!";
        sendMsg(chatId, answer);


    }

    //Надсилання списку
    private void sendObject(Long chatId, List messageToSand) {
        message.setChatId(chatId);
        message.setEntities(messageToSand);
        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }

    //надсилання текстового повідомлення
    private void sendMsg(Long chatId, String textToSend) {
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setReplyMarkup(getMainMenu());
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
    public String preLibrarian(Long chatId, Message message) {
        return preLibrary.put(chatId, message.getText());
    }

    // запис у другу головну мапу
    public void librarian(Long chatId, Message message) {

        String kay = preLibrary.get(chatId);
        library.put(kay, (List<Message>) message);
    }

    // пошук об'єктів, повертає список обєктів
    public List<Message> findObject(String message) {
        return library.get(message);
    }
    public String preFindObject(Long chatId ) {
        return preLibrary.get(chatId);
    }


    //userId -> massage
    // userid:metemateka -> List<messageId>
    //
}
