package com.example.controller.dto;

import com.example.model.File;

/**
 * Data Transfer Object for File.
 */

public class FileDTO {
    private Integer id;
    private String filePath;
    private String name;

    /**
     * Default constructor.
     * @param file
     */

    public FileDTO(File file) {
        this.id = file.getId();
        this.filePath = file.getFilePath();
        this.name = file.getName();
    }

    public FileDTO() {
    }

    public Integer getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getName() {
        return name;
    }
}
