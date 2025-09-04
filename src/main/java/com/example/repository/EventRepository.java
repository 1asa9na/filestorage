package com.example.repository;

import com.example.model.Event;
import java.util.List;

/**
 * Repository interface for managing Event entities.
 */

public interface EventRepository extends GenericRepository<Event, Integer> {

    List<Event> findByUserId(Integer userId) throws RepositoryException;

    Event save(Integer userId, Integer fileId) throws RepositoryException;

    Event update(Integer userId, Integer fileId) throws RepositoryException;
}
