package com.example.Bot2.bot3.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {
    @Autowired
    private static DialogService dialogService;

    //головна клавіатура
    public static ReplyKeyboardMarkup getMainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Зберегти до папки");
        row1.add("Додати до папки");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Видалити папку");
        row2.add("Видалити повідомлення з папки");
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Знайти");
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    static List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
    static InlineKeyboardButton inlineKeyboardButton;
    public static boolean isCalledToAdd = false;
    public static boolean isCalledToRemoveFolder = false;
    public static boolean isCalledToRemoveArtifact = false;

    public static InlineKeyboardMarkup getFolderMenu(List<String> folderNames) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        for (int i = rowList.size(); i < folderNames.size(); i++) {
            inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(folderNames.get(i));
            inlineKeyboardButton.setCallbackData(folderNames.get(i));
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
            keyboardButtonsRow1.add(inlineKeyboardButton);
            rowList.add(keyboardButtonsRow1);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        inlineKeyboardButton = new InlineKeyboardButton();
        rowList = new ArrayList<>();
        isCalledToAdd = false;
        isCalledToRemoveFolder = false;
        isCalledToRemoveArtifact =false;
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getMenuToAdd(List<String> folderNames) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        for (int i = rowList.size(); i < folderNames.size(); i++) {
            inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("Додати до " + '"' + folderNames.get(i) + '"');
            inlineKeyboardButton.setCallbackData(folderNames.get(i));
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
            keyboardButtonsRow1.add(inlineKeyboardButton);
            rowList.add(keyboardButtonsRow1);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        inlineKeyboardButton = new InlineKeyboardButton();
        rowList = new ArrayList<>();
        isCalledToAdd = true;
        isCalledToRemoveFolder = false;
        isCalledToRemoveArtifact = false;
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getMenuToRemoveFolder(List<String> folderNames) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        for (int i = rowList.size(); i < folderNames.size(); i++) {
            inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("Видалити папку " + '"' + folderNames.get(i) + '"');
            inlineKeyboardButton.setCallbackData(folderNames.get(i));
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
            keyboardButtonsRow1.add(inlineKeyboardButton);
            rowList.add(keyboardButtonsRow1);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        inlineKeyboardButton = new InlineKeyboardButton();
        rowList = new ArrayList<>();

        isCalledToAdd = false;
        isCalledToRemoveFolder = true;
        isCalledToRemoveArtifact = false;

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getMenuToRemoveArtifact(List<String> folderNames) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        for (int i = rowList.size(); i < folderNames.size(); i++) {
            inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("Видалити з папки " + '"' + folderNames.get(i) + '"');
            inlineKeyboardButton.setCallbackData(folderNames.get(i));
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
            keyboardButtonsRow1.add(inlineKeyboardButton);
            rowList.add(keyboardButtonsRow1);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        inlineKeyboardButton = new InlineKeyboardButton();
        rowList = new ArrayList<>();

        isCalledToAdd = false;
        isCalledToRemoveFolder = false;
        isCalledToRemoveArtifact = true;

        return inlineKeyboardMarkup;
    }
}

