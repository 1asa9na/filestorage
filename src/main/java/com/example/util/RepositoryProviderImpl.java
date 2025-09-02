package com.example.util;

import com.example.model.Event;
import com.example.model.File;
import com.example.model.User;
import com.example.repository.GenericRepository;
import jakarta.servlet.ServletContext;

/**
 * Implementation of the RepositoryProvider interface.
 */

public class RepositoryProviderImpl implements RepositoryProvider {

    private GenericRepository<User, Integer> userRepository;
    private GenericRepository<Event, Integer> eventRepository;
    private GenericRepository<File, Integer> fileRepository;

    public RepositoryProviderImpl(ServletContext context) {
        context.setAttribute("REPO_PRVDR", this);
    }

    @Override
    public GenericRepository<User, Integer> getUserRepository() {
        return userRepository;
    }

    @Override
    public GenericRepository<Event, Integer> getEventRepository() {
        return eventRepository;
    }

    @Override
    public GenericRepository<File, Integer> getFileRepository() {
        return fileRepository;
    }

    @Override
    public void setUserRepository(GenericRepository<User, Integer> repository) {
        this.userRepository = repository;
    }

    @Override
    public void setEventRepository(GenericRepository<Event, Integer> repository) {
        this.eventRepository = repository;
    }

    @Override
    public void setFileRepository(GenericRepository<File, Integer> repository) {
        this.fileRepository = repository;
    }
}
