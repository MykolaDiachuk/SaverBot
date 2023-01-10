package com.example.Bot2.bot3.resource;

import com.example.Bot2.Config.Const;
import com.example.Bot2.Config.DBHandler;
import com.example.Bot2.bot3.models.Artifact;
import com.example.Bot2.bot3.models.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ArtifactRepository {
    @Autowired
    @Lazy
    DialogService dialogService;


    DBHandler dbHandler = new DBHandler();

    private final Map<Long, Map<String, Folder>> userFolders = fillUserFolders(createFolderFromDB());
    public Map<Long, List<String>> folderNames = fillFolderNames();
    public Map<Long, String> nameOfFolder = fillNameOfFolder();


    public void createFolder(Long userId, Folder folder) {
        Map<String, Folder> folders = userFolders.get(userId);
        if (folders == null) {
            folders = new HashMap<>();
        }
        if (folders.containsKey(folder.getName())) {
            folders.replace(folder.getName(), folder);
        } else {
            folders.put(folder.getName(), folder);
            List<String> temp;
            if (!folderNames.containsKey(userId)) {
                temp = new ArrayList<>();
            } else {
                temp = folderNames.get(userId);
            }
            temp.add(folder.getName());
            folderNames.put(userId, temp);

        }
        userFolders.put(userId, folders);
        nameOfFolder.put(userId, folder.getName());


    }

    public Artifact createArtifact(Integer messageId, Long chatId, Long userId) {
        Artifact artifact = new Artifact();
        artifact.setChatId(chatId);
        artifact.setUserId(userId);
        artifact.setMessageId(messageId);
        return artifact;
    }

    public Map<Long, List<String>> fillFolderNames() {
        Map<Long, List<String>> nameOfFolder = new HashMap<>();

        try {
            List<Long> userId = dbHandler.selectUserId();
            for (Long i : userId) {
                List<String> name = dbHandler.selectFoldersNames(i);
                nameOfFolder.put(i, name);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return nameOfFolder;
    }

    public List<Artifact> createArtifactFromDB(Long chatId, Long userId, List<Integer> messageId) {
        List<Artifact> artifacts = new ArrayList<>();

        for (Integer i : messageId) {
            Artifact artifact = createArtifact(i, chatId, userId);
            artifacts.add(artifact);
        }

        return artifacts;
    }

    public List<Folder> createFolderFromDB() {

        ResultSet resultSet;
        try {
            resultSet = dbHandler.selectTable();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<Folder> folders = new ArrayList<>();


        try {
            while (resultSet.next()) {
                Folder folder = new Folder();
                String name = resultSet.getString(Const.NAME);
                Long userId = resultSet.getLong(Const.USER_ID);
                Long chatId = resultSet.getLong(Const.CHAT_ID);

                folder.setName(name);
                folder.setUserId(userId);
                try {
                    folder.setArtifacts(createArtifactFromDB(chatId, userId, dbHandler.selectArtifact(name, userId)));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                folders.add(folder);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return folders;
    }

    public Map<Long, Map<String, Folder>> fillUserFolders(List<Folder> folders) {

        Map<Long, Map<String, Folder>> userFolders = new HashMap<>();
        if(!folders.isEmpty()){
            Map<String, Folder> temp = new HashMap<>();
            Long userId = folders.get(0).getUserId();
            for (Folder i : folders) {
                if (!userId.equals(i.getUserId()) ) {
                    userFolders.put(userId, temp);
                    temp = new HashMap<>();
                    userId = i.getUserId();
                    temp.put(i.getName(), i);
                } else {
                    temp.put(i.getName(), i);
                }
            }
            userFolders.put(userId, temp);
        }

        return userFolders;
    }

    public Map<Long, String> fillNameOfFolder() {
        Map<Long, String> nameOfFolder = new HashMap<>();
        try {
            List<Long> usersId = dbHandler.selectUserId();
            for (Long i : usersId) {
                for (String j : dbHandler.selectFoldersNames(i)) {
                    nameOfFolder.put(i, j);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return nameOfFolder;
    }


    public boolean saveArtifact(Update update, Long userId) {
        Artifact artifact = createArtifact(update.getMessage().getMessageId(), update.getMessage().getChatId(), userId);
        Folder folder = getFolder(update.getMessage().getFrom().getId(), nameOfFolder.get(userId));
        if (folder == null) {
            dialogService.sendMessage(artifact.getChatId(), "Назву папки не вказано");
        } else {
            if (folder.getArtifacts() == null) {
                List<Artifact> artifacts = new ArrayList<>();
                artifacts.add(artifact);
                folder.setArtifacts(artifacts);
            } else {
                folder.getArtifacts().add(artifact);
            }
            createFolder(update.getMessage().getFrom().getId(), folder);

            try {
                DBHandler dbHandler = new DBHandler();
                dbHandler.addToDB(folder.getName(), userId, artifact.getMessageId(), artifact.getChatId(),
                        update.getMessage().getFrom().getUserName() + " / "+update.getMessage().getFrom().getFirstName()
                                + " " + update.getMessage().getFrom().getLastName());
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }


    public boolean isExist(String nameFolder, Long userId) { // перевірка на існування папки
        if (folderNames.isEmpty()) {
            return false;
        } else if (folderNames.get(userId) == null) {
            return false;
        } else return folderNames.get(userId).contains(nameFolder);
    }


    public Folder getFolder(Long userId, String nameOfFolder) {
        Map<String, Folder> folders = userFolders.get(userId);
        if (folders == null) {
            folders = new HashMap<>();
        }
        return folders.get(nameOfFolder);
    }

    public void removeFolder(Long userId, String nameOfFolder) {
        userFolders.get(userId).remove(nameOfFolder);
        folderNames.get(userId).remove(nameOfFolder);

        try {
            DBHandler dbHandler = new DBHandler();
            dbHandler.deleteFolderFromDB(nameOfFolder, userId);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void removeArtifact(Long userId, Integer massageId) {
        List<Artifact> artifacts = getFolder(userId, nameOfFolder.get(userId)).getArtifacts();
        for (Artifact a : artifacts) {
            if (a.getMessageId().equals(massageId)) {
                dialogService.sendMessage(a.getChatId(), "Видалено" + Emojis.DUMP.get());
                userFolders.get(userId).get(nameOfFolder.get(userId)).getArtifacts().remove(a);

                try {
                    DBHandler dbHandler = new DBHandler();
                    dbHandler.deleteArtifactFromDB(nameOfFolder.get(userId), userId, a.getMessageId());
                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }


    }


}
