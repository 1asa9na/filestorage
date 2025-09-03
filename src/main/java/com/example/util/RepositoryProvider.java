package com.example.util;

import com.example.repository.EventRepository;
import com.example.repository.FileRepository;
import com.example.repository.UserRepository;

/**
 * Interface for providing repository instances.
 */

public interface RepositoryProvider {

    UserRepository getUserRepository();

    EventRepository getEventRepository();

    FileRepository getFileRepository();

    void setUserRepository(UserRepository repository);

    void setEventRepository(EventRepository repository);

    void setFileRepository(FileRepository repository);
}
