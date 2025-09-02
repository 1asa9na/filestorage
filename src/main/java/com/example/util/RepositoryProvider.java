package com.example.util;

import com.example.model.Event;
import com.example.model.File;
import com.example.model.User;
import com.example.repository.GenericRepository;

/**
 * Interface for providing repository instances.
 */

public interface RepositoryProvider {

    GenericRepository<User, Integer> getUserRepository();

    GenericRepository<Event, Integer> getEventRepository();

    GenericRepository<File, Integer> getFileRepository();

    void setUserRepository(GenericRepository<User, Integer> repository);

    void setEventRepository(GenericRepository<Event, Integer> repository);

    void setFileRepository(GenericRepository<File, Integer> repository);
}
