package com.example.Bot2.bot3.resource;

import com.example.Bot2.bot3.models.Artifact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class Keyboard {

    //головна клавіатура
    public static ReplyKeyboardMarkup getMainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Зберегти повідомлення");
        row1.add("Зберегти до папки");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Видалити повідомлення");
        row2.add("Видалити папку");
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Знайти");
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    private static List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
    private static InlineKeyboardButton inlineKeyboardButton;
    public static boolean isCalledToAdd = false;
    public static boolean isCalledToRemoveFolder = false;
    public static boolean isCalledToRemoveArtifact = false;

    public static boolean isCalledRemoveThisArtifact = true;

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
        isCalledToRemoveArtifact = false;
        isCalledRemoveThisArtifact = false;
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
        isCalledRemoveThisArtifact = false;
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
        isCalledRemoveThisArtifact = false;

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getMenuToRemoveArtifact(List<String> folderNames) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        for (int i = rowList.size(); i < folderNames.size(); i++) {
            inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("Видалити з " + '"' + folderNames.get(i) + '"');
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
        isCalledRemoveThisArtifact = false;

        return inlineKeyboardMarkup;
    }


    @Autowired
    @Lazy
    DialogService dialogService;

    public InlineKeyboardMarkup removeThisArtifact( Artifact artifact) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();

        dialogService.forwardMessage(artifact.getChatId(), artifact.getChatId(), artifact.getMessageId());

        inlineKeyboardButton.setText("Видалити");
        inlineKeyboardButton.setCallbackData(String.valueOf(artifact.getMessageId()));
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton);
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}


