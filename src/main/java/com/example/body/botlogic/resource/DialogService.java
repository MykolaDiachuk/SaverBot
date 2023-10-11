package com.example.body.botlogic.resource;

import com.example.body.botlogic.models.Artifact;
import com.example.body.botlogic.models.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class DialogService {


    private final Keyboard keyboard;
    private final TelegramBotResource telegramBotResource;
    private final ArtifactRepository artifactRepository;


    @Autowired
    public DialogService(Keyboard keyboard, TelegramBotResource telegramBotResource, ArtifactRepository artifactRepository) {
        this.keyboard = keyboard;
        this.telegramBotResource = telegramBotResource;
        this.artifactRepository = artifactRepository;
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
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendKeyboard(Long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            telegramBotResource.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void forwardArtifacts(Long userId, Long chatId) {
        Folder folder = artifactRepository.getFolder(userId,
                artifactRepository.nameOfFolder.get(userId));
        if (folder.getArtifacts() == null) {
            List<Artifact> artifacts = new ArrayList<>();
            folder.setArtifacts(artifacts);
        }
        if (folder.getArtifacts().isEmpty()) {
            sendMessage(chatId, "Папка пуста");
        } else {
            sendMessage(chatId,
                    "Вміст папки " + '"' + artifactRepository.nameOfFolder.get(userId) + '"');
            List<Artifact> artifacts = folder.getArtifacts();
            for (Artifact artifact : artifacts) {
                forwardMessage(artifact.getChatId(), artifact.getChatId(), artifact.getMessageId());
            }
        }
    }


    public void sendKeyboardArtifact(Long chatId, String text, Long userId) {
        Folder folder = artifactRepository.getFolder(userId,
                artifactRepository.nameOfFolder.get(userId));
        if (folder.getArtifacts() == null) {
            sendMessage(chatId, "Папка пуста");
        } else {
            List<Artifact> artifacts = folder.getArtifacts();

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(text);

            for (Artifact artifact : artifacts) {
                sendMessage.setReplyMarkup(keyboard.removeThisArtifact(artifact));
                try {
                    telegramBotResource.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }


}
