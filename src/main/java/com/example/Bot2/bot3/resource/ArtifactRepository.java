package com.example.Bot2.bot3.resource;

import com.example.Bot2.bot3.models.Artifact;
import com.example.Bot2.bot3.models.Folder;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ArtifactRepository {


    private final Map<Long, Map<String, Folder>> userFolders = new HashMap<>();
    public final List<String> folderNames = new ArrayList<>();

    public String nameOfFolder;


    public void createFolder(Long userId, Folder folder) {
        Map<String, Folder> folders = userFolders.get(userId);
        if (folders == null) {
            folders = new HashMap<>();
        }
        if (folders.containsKey(folder.getName())) {
            folders.replace(folder.getName(), folder);
        } else {
            folders.put(folder.getName(), folder);
            folderNames.add(folder.getName());
        }
        userFolders.put(userId, folders);
        nameOfFolder = folder.getName();
    }

    public void saveArtifact(Update update){
        Artifact artifact = new Artifact();
        artifact.setChatId(update.getMessage().getChatId());
        artifact.setUserId(update.getMessage().getFrom().getId());
        artifact.setMessageId(update.getMessage().getMessageId());
        Folder folder = getFolder(update.getMessage().getFrom().getId(), nameOfFolder);
        if(folder.getArtifacts()==null){
            List<Artifact> artifacts = new ArrayList<>();
            artifacts.add(artifact);
            folder.setArtifacts(artifacts);
        }else folder.getArtifacts().add(artifact);
        createFolder(update.getMessage().getFrom().getId(),folder);
    }


    public Map<String, Folder> getFolders(Long userId) {
        Map<String, Folder> folders = userFolders.get(userId);
        if (folders == null) {
            return new HashMap<>();
        }
        return folders;
    }

    public Folder getFolder(Long userId, String nameOfFolder) {
        return getFolders(userId).get(nameOfFolder);
    }


}
