package com.example.Bot2.bot3.resource;

import com.example.Bot2.Config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class DialogService {
    @Autowired
    TelegramBotResource telegramBotResource = new TelegramBotResource(new BotConfig());

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


}
