package com.example.Bot2.servise;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Library {
    String nameOfFolder;


    List<String> folder = new ArrayList<>(); // лист з назвами папок
    List< java.io.File> result = new ArrayList<>(); // лист з різними обєктами. як я вже дізнався, він не має бути типу Message,
    // але я не можу поки добитися чогось іншого
    HashMap<String, List< java.io.File>> libraryOfMessage = new HashMap<>();

    //запис у перший лист. потрібно для того, щоб можна було знайти kеy для мапи.
    public void preLibrarian(Message message) {

        folder.add(message.getText());
        nameOfFolder = message.getText();
    }

    // запис нового ключа зі значенням
    public void librarian( java.io.File message) {
        result.add(message);
        libraryOfMessage.put(nameOfFolder, result);

    }

    //метод для запису у існуючий ключ нове значення.
    // Наприклад вже був ключ "математика" зі значенням List<> у якому зберігалася книжка і я додав  до "Математика" ще одну книжку.
    // Тепер у листі будуть дві книжки.
    public void librarian2( java.io.File message) {
        List< java.io.File> addNewObject = libraryOfMessage.get(nameOfFolder);
        addNewObject.add(message);
        libraryOfMessage.put(nameOfFolder, addNewObject);

    }
}
