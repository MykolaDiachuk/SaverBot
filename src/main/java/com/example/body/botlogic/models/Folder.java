package com.example.body.botlogic.models;

import lombok.Data;

import java.util.List;

@Data
public class Folder {
    private String name;

    private Long userId;

    private List<Artifact> artifacts;
}
