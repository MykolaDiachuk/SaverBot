package com.example.Bot2.bot3.resource;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {


    //головна клавіатура
    public static ReplyKeyboardMarkup getMainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Зберегти до папки");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Знайти");
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

   static List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
    static InlineKeyboardButton inlineKeyboardButton;

    public static InlineKeyboardMarkup getFolderMenu( List<String> folderNames) {
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
        return inlineKeyboardMarkup;
    }
}
