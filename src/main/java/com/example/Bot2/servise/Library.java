package com.example.Bot2.servise;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Library {
    String nameOfFolder;


    List<String> folder = new ArrayList<>();
    List<Message> result = new ArrayList<>();
    HashMap<String, List<Message>> libraryOfMessage = new HashMap<>();

public String getNameOfFolder(){
    return nameOfFolder;
}
public void setNameOfFolder(String nameOfFolder){
    this.nameOfFolder = nameOfFolder;
}

    //запис у перший лист. потрібно для того, щоб можна було знайти kеy для другої мапи.
    public void preLibrarian(Message message) {

        folder.add(message.getText());
        nameOfFolder = message.getText();
    }

    // запис у  головну мапу
    public void librarian(Message message) {
        result.add(message);
        libraryOfMessage.put(nameOfFolder, result);

    }

    public void librarian2(Message message) {
        List<Message> addNewObject = libraryOfMessage.get(nameOfFolder);
        addNewObject.add(message);
        libraryOfMessage.put(nameOfFolder,addNewObject);

    }
}
