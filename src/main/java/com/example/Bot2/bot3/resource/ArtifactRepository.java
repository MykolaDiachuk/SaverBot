package com.example.Bot2.bot3.resource;

import com.example.Bot2.bot3.models.Artifact;
import com.example.Bot2.bot3.models.Folder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ArtifactRepository {
    @Autowired
    @Lazy
    DialogService dialogService;


    private final Map<Long, Map<String, Folder>> userFolders = new HashMap<>();
    public final Map<Long, List<String>> folderNames = new HashMap<>();

    public final Map<Long, String> nameOfFolder = new HashMap<>();


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

    public Artifact createArtifact(Update update) {
        Artifact artifact = new Artifact();
        artifact.setChatId(update.getMessage().getChatId());
        artifact.setUserId(update.getMessage().getFrom().getId());
        artifact.setMessageId(update.getMessage().getMessageId());
        return artifact;
    }

    public boolean saveArtifact(Update update, Long userId) {
        Artifact artifact = createArtifact(update);
        Folder folder = getFolder(update.getMessage().getFrom().getId(), nameOfFolder.get(userId));
        if (folder == null) {
            dialogService.sendMessage(artifact.getChatId(), "Назву папки не вказано");
        } else {
            if (folder.getArtifacts() == null) {
                List<Artifact> artifacts = new ArrayList<>();
                artifacts.add(artifact);
                folder.setArtifacts(artifacts);
            } else{
                folder.getArtifacts().add(artifact);
            }
            createFolder(update.getMessage().getFrom().getId(), folder);
            return true;
        }
        return false;
    }



    public boolean isExist(String nameFolder, Long userId) {
        if (folderNames.isEmpty()) {
            return false;
        } else if (folderNames.get(userId) == null) {
            return false;
        }else return folderNames.get(userId).contains(nameFolder);
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
    }


    public void removeArtifact(Long userId, int i) {
        Artifact artifact = getFolder(userId,nameOfFolder.get(userId)).getArtifacts().get(i);
        dialogService.sendMessage(artifact.getChatId(),"Видалено" + Emojis.DUMP.get());
        userFolders.get(userId).get(nameOfFolder.get(userId)).getArtifacts().remove(artifact);

    }


}
