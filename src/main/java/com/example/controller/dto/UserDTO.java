package com.example.controller.dto;

import com.example.model.User;

/**
 * Data Transfer Object for User.
 */

public class UserDTO {
    private Integer id;
    private String name;

    public UserDTO(User user) {
        id = user.getId();
        name = user.getName();
    }

    public UserDTO() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
