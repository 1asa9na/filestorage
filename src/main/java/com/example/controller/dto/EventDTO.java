package com.example.controller.dto;

import com.example.model.Event;

/**
 * Data Transfer Object for Event.
 */

public class EventDTO {
    private Integer id;
    private Integer userId;
    private Integer fileId;
    private String filePath;
    private String fileName;

    /**
     * Constructor for EventDTO.
     * @param event
     */

    public EventDTO(Event event) {
        this.id = event.getId();
        this.userId = event.getUser().getId();
        this.fileId = event.getFile().getId();
        this.filePath = event.getFile().getFilePath();
        this.fileName = event.getFile().getName();
    }

    public EventDTO() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getFileId() {
        return fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }
}
