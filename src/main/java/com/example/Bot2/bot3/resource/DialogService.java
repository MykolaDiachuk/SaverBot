package com.example.Bot2.bot3.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class DialogService {


    private final TelegramBotResource telegramBotResource;

    @Autowired
    public DialogService(TelegramBotResource telegramBotResource) {
        this.telegramBotResource = telegramBotResource;
    }


    public void forwardMessage(Long chatId, Long fromChatId, Integer messageId) {
        ForwardMessage forwardMessage = new ForwardMessage();
        forwardMessage.setFromChatId(fromChatId);
        forwardMessage.setChatId(chatId);
        forwardMessage.setMessageId(messageId);
        try {
            telegramBotResource.execute(forwardMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            telegramBotResource.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);

        }
    }

    public void sendKeyboard(Long chatId, String text, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            telegramBotResource.execute(sendMessage);
        } catch (TelegramApiException ignored) {
        }
    }

    public void sendKeyboard(Long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            telegramBotResource.execute(sendMessage);
        } catch (TelegramApiException ignored) {
        }
    }


}
