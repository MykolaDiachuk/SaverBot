package com.example.Bot2.bot3.resource;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;



public class DialogService {
    TelegramLongPollingBot execute;

    public void forwardMessage(Long chatId, Long fromChatId, Integer messageId)  {
        ForwardMessage forwardMessage = new ForwardMessage();
        forwardMessage.setFromChatId(fromChatId);
        forwardMessage.setChatId(chatId);
        forwardMessage.setMessageId(messageId);
        try {
            execute.execute(forwardMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}
