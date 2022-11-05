package com.example.Bot2.bot3.resource;

import com.example.Bot2.Config.BotConfig;
import com.example.Bot2.bot3.incomingMessage.Document;
import com.example.Bot2.bot3.incomingMessage.Entity;
import com.example.Bot2.bot3.incomingMessage.IncomingMessage;
import com.example.Bot2.bot3.incomingMessage.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBotResource extends TelegramLongPollingBot {

    private final BotConfig config;

    public TelegramBotResource(BotConfig config) {
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

    @Override
    public void onUpdateReceived(Update update) {
        IncomingMessage incomingMessage = new IncomingMessage();

        incomingMessage.setUpdates(new Message());
        incomingMessage.executeUpdate(update);

        incomingMessage.setUpdates(new Document());
        incomingMessage.executeUpdate(update);

        incomingMessage.setUpdates(new Entity());
        incomingMessage.executeUpdate(update);



    }


}
