package com.example.repository.hibernate;

import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityManagerFactory;

/**
 * Hibernate implementation of user repository.
 */

public class HibernateUserRepositoryImpl extends HibernateRepository<User, Integer> implements UserRepository {

    public HibernateUserRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, User.class);
    }
}
