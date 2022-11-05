package com.example.Bot2.bot3.resource;

import com.example.Bot2.bot3.models.Folder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ArtifactRepository {

    private final Map<String, Map<String, Folder>> userFolders = new HashMap<>();
    private final List<String> folderNames = new ArrayList<>();

    public void createFolder(String userId, Folder folder){
        Map<String, Folder> folders = userFolders.get(userId);
        if (folders == null) {
            folders = new HashMap<>();
        }
        folders.put(folder.getName(), folder);
        userFolders.put(userId, folders);
        folderNames.add(folder.getName());
    }

    public Map<String, Folder> getFolders(String userId) {
        Map<String, Folder> folders = userFolders.get(userId);
        if (folders == null) {
            return new HashMap<>();
        }
        return folders;
    }

    public Folder getFolder(String userId, String folderName) {
        return getFolders(userId).get(folderName);
    }

}
