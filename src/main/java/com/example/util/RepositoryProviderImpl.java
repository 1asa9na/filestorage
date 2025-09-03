package com.example.util;

import com.example.repository.EventRepository;
import com.example.repository.FileRepository;
import com.example.repository.UserRepository;
import jakarta.servlet.ServletContext;

/**
 * Implementation of the RepositoryProvider interface.
 */

public class RepositoryProviderImpl implements RepositoryProvider {

    private UserRepository userRepository;
    private EventRepository eventRepository;
    private FileRepository fileRepository;

    public RepositoryProviderImpl(ServletContext context) {
        context.setAttribute("REPO_PRVDR", this);
    }

    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public EventRepository getEventRepository() {
        return eventRepository;
    }

    @Override
    public FileRepository getFileRepository() {
        return fileRepository;
    }

    @Override
    public void setUserRepository(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public void setEventRepository(EventRepository repository) {
        this.eventRepository = repository;
    }

    @Override
    public void setFileRepository(FileRepository repository) {
        this.fileRepository = repository;
    }
}
