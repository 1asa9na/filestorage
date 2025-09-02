package com.example.util;

import com.example.model.Event;
import com.example.model.File;
import com.example.model.User;
import com.example.repository.GenericRepository;
import com.example.repository.HibernateRepository;
import com.google.gson.GsonBuilder;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * ServletContextListener implementation for initializing the File Storage application.
 */

@WebListener
public class FileStorageServletContextListener implements ServletContextListener {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("filestorage");
    /**
     * Context initialization for the File Storage application.
     */

    public void contextInitialized(ServletContextEvent sce) {
        GenericRepository<User, Integer> ur
            = new HibernateRepository<User, Integer>(emf, User.class);
        GenericRepository<Event, Integer> er
            = new HibernateRepository<Event, Integer>(emf, Event.class);
        GenericRepository<File, Integer> fr
            = new HibernateRepository<File, Integer>(emf, File.class);
        RepositoryProvider provider = new RepositoryProviderImpl(sce.getServletContext());
        provider.setUserRepository(ur);
        provider.setEventRepository(er);
        provider.setFileRepository(fr);
        GsonBuilder gsonBuilder = new GsonBuilder();
        sce.getServletContext().setAttribute("GSON_BLDR", gsonBuilder);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
