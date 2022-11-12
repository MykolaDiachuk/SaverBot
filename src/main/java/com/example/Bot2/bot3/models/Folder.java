package com.example.Bot2.bot3.models;

import lombok.Data;

import java.util.List;

@Data
public class Folder {
    private String name;

    private Long userId;

    private List<Artifact> artifacts;
}
