package com.example.repository.hibernate;

import com.example.model.File;
import com.example.repository.FileRepository;
import jakarta.persistence.EntityManagerFactory;

/**
 * Hibernate implementation of file repository.
 */

public class HibernateFileRepositoryImpl extends HibernateRepository<File, Integer> implements FileRepository {

    public HibernateFileRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, File.class);
    }
}
