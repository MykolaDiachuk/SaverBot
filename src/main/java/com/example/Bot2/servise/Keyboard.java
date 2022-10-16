package com.example.Bot2.servise;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {
    // Library library = new Library();

    //головна клавіатура
    public ReplyKeyboardMarkup getMainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
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

    //не звертай уваги. недописаний поки що код
    public InlineKeyboardMarkup getFolderMenu(List<String> folder) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        int a = folder.size();
        for (int i = 0; i != a; i++) {
            inlineKeyboardButton.setText(folder.get(i));
            inlineKeyboardButton.setCallbackData(folder.get(i));
            keyboardButtonsRow1.add(inlineKeyboardButton);
        }

        rowList.add(keyboardButtonsRow1);


        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
